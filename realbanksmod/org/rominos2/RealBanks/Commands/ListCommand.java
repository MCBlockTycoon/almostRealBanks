/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.rominos2.RealBanks.Commands;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.rominos2.RealBanks.api.Banks.Bank;
import org.rominos2.RealBanks.api.Banks.BankAccount;
import org.rominos2.RealBanks.api.Banks.WorldManager;
import org.rominos2.RealBanks.api.Settings.BankSettings;
import org.rominos2.RealBanks.api.Settings.LanguageSettings.SentenceType;
import org.rominos2.RealBanks.api.Settings.WorldSettings;

public class ListCommand {
	public static void listBanks(Player player, int page) {
		WorldManager manager = org.rominos2.RealBanks.RealBanks.getInstance()
				.getManager(player.getWorld());
		int linesPerPage = manager.getProperties().getLinesPerPage();
		player.sendMessage(ChatColor.DARK_GREEN
				+ "===[ "
				+ ChatColor.YELLOW
				+ org.rominos2.RealBanks.RealBanks.getInstance().getLang(
						LanguageSettings.SentenceType.LIST,
						"List.Banks",
						"RealBanks List in <world>, Page <page>/<total>",
						new String[] {
								player.getWorld().getName(),
								page,
								1 + manager.getBanks().length
										/ (linesPerPage + 1) })
				+ ChatColor.DARK_GREEN + " ]===");
		player.sendMessage(ChatColor.GREEN
				+ "["
				+ org.rominos2.RealBanks.RealBanks.getInstance().getLang(
						LanguageSettings.SentenceType.NORMAL, "List.Available",
						"Available", null)
				+ "]"
				+ ChatColor.BLUE
				+ "["
				+ org.rominos2.RealBanks.RealBanks.getInstance().getLang(
						LanguageSettings.SentenceType.NORMAL,
						"List.Subscribed", "Subscribed", null)
				+ "]"
				+ ChatColor.RED
				+ "["
				+ org.rominos2.RealBanks.RealBanks.getInstance().getLang(
						LanguageSettings.SentenceType.NORMAL,
						"List.NotAvailable", "NotAvailable", null) + "]");

		for (int i = (page - 1) * linesPerPage; (i < manager.getBanks().length)
				&& (i < page * linesPerPage); ++i) {
			Bank bank = manager.getBanks()[i];
			ChatColor color;
			ChatColor color;
			if (bank.getAccount(player) != null) {
				color = ChatColor.BLUE;
			} else {
				ChatColor color;
				if ((bank.isFull())
						|| ((!(bank.getProperties().getEnterPermission()
								.isEmpty())) && (!(org.rominos2.RealBanks.RealBanks
								.getInstance().askPermissions(player, bank
								.getProperties().getEnterPermission(), false))))) {
					color = ChatColor.RED;
				} else {
					color = ChatColor.GREEN;
				}
			}
			player.sendMessage("- "
					+ color
					+ bank.getName()
					+ manager.getProperties().getColor()
					+ ", "
					+ org.rominos2.RealBanks.RealBanks.getInstance().getLang(
							LanguageSettings.SentenceType.NORMAL,
							"List.Chests", "Chests", null)
					+ ": "
					+ ((bank.getChests().length == 0) ? ChatColor.RED : "")
					+ bank.getChests().length
					+ manager.getProperties().getColor()
					+ ", "
					+ org.rominos2.RealBanks.RealBanks.getInstance().getLang(
							LanguageSettings.SentenceType.NORMAL,
							"List.Accounts", "Accounts", null) + ": "
					+ bank.getAccounts().length + "/"
					+ bank.getProperties().getAccountsLimit());
		}
	}

	public static void listPlayersInBank(Player player, String bankName,
			int page) {
		WorldManager manager = org.rominos2.RealBanks.RealBanks.getInstance()
				.getManager(player.getWorld());
		int linesPerPage = manager.getProperties().getLinesPerPage();

		Bank bank = manager.getBank(bankName);
		if (bank == null) {
			player.sendMessage(RealBanksCommands.getLang(manager,
					LanguageSettings.SentenceType.BANK, "Bank.NotFound",
					"<bank> Bank does not exists.", new String[] { bankName }));
			return;
		}

		player.sendMessage(ChatColor.DARK_GREEN
				+ "===[ "
				+ ChatColor.YELLOW
				+ org.rominos2.RealBanks.RealBanks
						.getInstance()
						.getLang(
								LanguageSettings.SentenceType.BANK_LIST,
								"List.Clients",
								"Clients List in <bank> in <world>, Page <page>/<total>",
								new String[] {
										bank.getName(),
										player.getWorld().getName(),
										page,
										1 + manager.getBanks().length
												/ (linesPerPage + 1) })
				+ ChatColor.DARK_GREEN + " ]===");
		for (int i = (page - 1) * linesPerPage; (i < bank.getAccounts().length)
				&& (i < page * linesPerPage); ++i)
			player.sendMessage(bank.getAccounts()[i].getPlayer().getName());
	}
}