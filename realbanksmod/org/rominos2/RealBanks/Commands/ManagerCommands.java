/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.rominos2.RealBanks.Commands;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.PluginManager;
import org.rominos2.RealBanks.api.Banks.Bank;
import org.rominos2.RealBanks.api.Banks.BankAccount;
import org.rominos2.RealBanks.api.Banks.WorldManager;
import org.rominos2.RealBanks.api.Events.RealBanksBankModificationEvent;
import org.rominos2.RealBanks.api.Events.RealBanksBankModificationEvent.BankModificationType;
import org.rominos2.RealBanks.api.Events.RealBanksEvent;
import org.rominos2.RealBanks.api.Settings.BankSettings;
import org.rominos2.RealBanks.api.Settings.LanguageSettings.SentenceType;

public class ManagerCommands {
	public static boolean modify(Player player, String[] args) {
		WorldManager manager = org.rominos2.RealBanks.RealBanks.getInstance()
				.getManager(player.getWorld());

		Bank bank = manager.getBank(args[1]);
		if (bank == null) {
			player.sendMessage(RealBanksCommands.getLang(manager,
					LanguageSettings.SentenceType.BANK, "Bank.NoFound",
					"<bank> Bank does not exists.", new String[] { args[1] }));
			return true;
		}

		if (args.length == 2) {
			player.sendMessage(ChatColor.DARK_GREEN
					+ "===[ "
					+ ChatColor.YELLOW
					+ org.rominos2.RealBanks.RealBanks.getInstance().getLang(
							LanguageSettings.SentenceType.BANK, "Modify.Bank",
							"<bank> Bank", new String[] { bank.getName() })
					+ ChatColor.DARK_GREEN + " ]===");
			player.sendMessage(RealBanksCommands.getLang(manager,
					LanguageSettings.SentenceType.NORMAL,
					"Modify.AccountLimit.Name", "Account Limit", null)
					+ " : " + bank.getProperties().getAccountsLimit());
			player.sendMessage(RealBanksCommands.getLang(manager,
					LanguageSettings.SentenceType.NORMAL,
					"Modify.EnterPermission.Name", "Enter Permission", null)
					+ " : " + bank.getProperties().getEnterPermission());
			player.sendMessage(RealBanksCommands.getLang(manager,
					LanguageSettings.SentenceType.NORMAL,
					"Modify.LogTransactions.Name", "Log Transactions", null)
					+ " : " + bank.getProperties().isLogTransactions());
			player.sendMessage(RealBanksCommands.getLang(manager,
					LanguageSettings.SentenceType.NORMAL,
					"Modify.EnterCost.Name", "Enter Cost", null)
					+ " : " + bank.getProperties().getEnterCost());
			return true;
		}

		RealBanksEvent event = null;

		if ((args.length >= 4) && (args[2].equalsIgnoreCase("limit"))
				&& (args[3].matches("[\\+]?[0-9]+"))) {
			String oldV = bank.getProperties().getAccountsLimit();
			bank.getProperties().setAccountLimit(
					Integer.valueOf(args[3]).intValue());
			player.sendMessage(RealBanksCommands.getLang(manager,
					LanguageSettings.SentenceType.VALUE,
					"Modify.AccountLimit.Done",
					"Account Limit has been changed to <value>.",
					new String[] { args[3] }));
			event = new RealBanksBankModificationEvent(
					bank,
					player,
					RealBanksBankModificationEvent.BankModificationType.ACCOUNT_LIMIT,
					oldV, args[3]);
		}

		if ((args.length >= 3) && (args[2].equalsIgnoreCase("permission"))) {
			String perm = "";
			if (args.length >= 4) {
				perm = args[3];
			}
			String oldV = bank.getProperties().getEnterPermission();
			bank.getProperties().setEnterPermission(perm);
			player.sendMessage(RealBanksCommands.getLang(manager,
					LanguageSettings.SentenceType.VALUE,
					"Modify.EnterPermission.Done",
					"Enter Permission has been changed to <value>.",
					new String[] { perm }));
			event = new RealBanksBankModificationEvent(
					bank,
					player,
					RealBanksBankModificationEvent.BankModificationType.ENTER_PERMISSION,
					oldV, perm);
		}

		if ((args.length >= 4) && (args[2].equalsIgnoreCase("log"))) {
			boolean log = false;
			if (args[3].equalsIgnoreCase("true")) {
				log = true;
			}
			String oldV = bank.getProperties().isLogTransactions();
			bank.getProperties().setLogTransactions(log);
			player.sendMessage(RealBanksCommands.getLang(manager,
					LanguageSettings.SentenceType.VALUE,
					"Modify.LogTransactions.Done",
					"Log Transactions have been changed to <value>.",
					new String[] { log }));
			event = new RealBanksBankModificationEvent(
					bank,
					player,
					RealBanksBankModificationEvent.BankModificationType.LOG_TRANSACTION,
					oldV, log);
		}

		if ((args.length >= 4)
				&& (args[2].equalsIgnoreCase("cost"))
				&& (((args[3].matches("[\\+]?[0-9]+.[0-9]+")) || (args[3]
						.matches("[\\+]?[0-9]+"))))) {
			double cost;
			double cost;
			if (args[3].contains(".")) {
				cost = Double.valueOf(args[3]).doubleValue();
			} else {
				cost = Integer.valueOf(args[3]).intValue();
			}
			String oldV = bank.getProperties().getEnterCost();
			bank.getProperties().setEnterCost(cost);
			player.sendMessage(RealBanksCommands.getLang(manager,
					LanguageSettings.SentenceType.VALUE,
					"Modify.EnterCost.Done",
					"Enter Cost has been changed to <value>.",
					new String[] { cost }));
			event = new RealBanksBankModificationEvent(
					bank,
					player,
					RealBanksBankModificationEvent.BankModificationType.ENTER_COST,
					oldV, cost);
		}

		if (event != null) {
			org.rominos2.RealBanks.RealBanks.getInstance().getServer()
					.getPluginManager().callEvent(event);
			return true;
		}
		return false;
	}

	public static void edit(Player sender, String bankName, String clientName) {
		WorldManager manager = org.rominos2.RealBanks.RealBanks.getInstance()
				.getManager(sender.getWorld());

		Bank bank = manager.getBank(bankName);
		if (bank == null) {
			sender.sendMessage(RealBanksCommands.getLang(manager,
					LanguageSettings.SentenceType.BANK, "Bank.NotFound",
					"<bank> Bank does not exists.", new String[] { bankName }));
			return;
		}

		OfflinePlayer client = org.rominos2.RealBanks.RealBanks.getInstance()
				.getServer().getOfflinePlayer(clientName);
		if (client == null) {
			sender.sendMessage(RealBanksCommands.getLang(manager,
					LanguageSettings.SentenceType.CLIENT, "Client.NotFound",
					"<client> does not exists.", new String[] { clientName }));
			return;
		}

		BankAccount account = bank.getAccount(client);
		if (account == null) {
			sender.sendMessage(RealBanksCommands.getLang(manager,
					LanguageSettings.SentenceType.BANK_CLIENT,
					"Bank.NotIn.Other",
					"<client> is not registered in the <bank> Bank.",
					new String[] { bank.getName(), client.getName() }));
			return;
		}

		bank.openVirtualTransaction(sender, account, "/rb edit");
	}

	public static void as(Player sender, String bankName, String clientName) {
		WorldManager manager = org.rominos2.RealBanks.RealBanks.getInstance()
				.getManager(sender.getWorld());

		Bank bank = manager.getBank(bankName);
		if (bank == null) {
			sender.sendMessage(RealBanksCommands.getLang(manager,
					LanguageSettings.SentenceType.BANK, "Bank.NotFound",
					"<bank> Bank does not exists.", new String[] { bankName }));
			return;
		}

		OfflinePlayer client = org.rominos2.RealBanks.RealBanks.getInstance()
				.getServer().getOfflinePlayer(clientName);
		if (client == null) {
			sender.sendMessage(RealBanksCommands.getLang(manager,
					LanguageSettings.SentenceType.CLIENT, "Client.NotFound",
					"<client> does not exists.", new String[] { clientName }));
			return;
		}

		BankAccount account = bank.getAccount(client);
		if (account == null) {
			sender.sendMessage(RealBanksCommands.getLang(manager,
					LanguageSettings.SentenceType.BANK_CLIENT,
					"Bank.NotIn.Other",
					"<client> is not registered in the <bank> Bank.",
					new String[] { bank.getName(), client.getName() }));
			return;
		}

		bank.setAlias(sender, client);
		sender.sendMessage(RealBanksCommands.getLang(manager,
				LanguageSettings.SentenceType.BANK_CLIENT, "Bank.As.Done",
				"You now see <bank> Bank accounts as if you were <client>",
				new String[] { bank.getName(), client.getName() }));
	}

	public static void reload(Player sender) {
		org.rominos2.RealBanks.RealBanks
				.getInstance()
				.getPluginLoader()
				.disablePlugin(
						(org.rominos2.RealBanks.RealBanks) org.rominos2.RealBanks.RealBanks
								.getInstance());
		org.rominos2.RealBanks.RealBanks
				.getInstance()
				.getPluginLoader()
				.enablePlugin(
						(org.rominos2.RealBanks.RealBanks) org.rominos2.RealBanks.RealBanks
								.getInstance());
		sender.sendMessage(ChatColor.GREEN
				+ org.rominos2.RealBanks.RealBanks.getInstance().getLang(
						LanguageSettings.SentenceType.NORMAL, "Reload.Done",
						"Reload Complete", null));
	}
}