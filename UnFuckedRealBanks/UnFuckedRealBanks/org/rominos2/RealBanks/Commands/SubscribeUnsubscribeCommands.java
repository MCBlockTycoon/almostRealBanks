/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.rominos2.RealBanks.Commands;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.rominos2.RealBanks.api.Banks.Bank;
import org.rominos2.RealBanks.api.Banks.WorldManager;
import org.rominos2.RealBanks.api.Events.RealBanksEvent;
import org.rominos2.RealBanks.api.Events.RealBanksPlayerSubscribeEvent;
import org.rominos2.RealBanks.api.Events.RealBanksPlayerUnsubscribeEvent;
import org.rominos2.RealBanks.api.Settings.BankSettings;
import org.rominos2.RealBanks.api.Settings.LanguageSettings.SentenceType;
import org.rominos2.RealBanks.api.Settings.LanguageSettings;

public class SubscribeUnsubscribeCommands {
	public static void subscribe(Player player, String bankName,
			String clientName, boolean confirmed) {
		WorldManager manager = org.rominos2.RealBanks.RealBanks.getInstance()
				.getManager(player.getWorld());
		Bank bank = manager.getBank(bankName);
		if (bank == null) {
			player.sendMessage(RealBanksCommands.getLang(manager,
					LanguageSettings.SentenceType.BANK, "Bank.NotFound",
					"<bank> Bank does not exists.", new String[] { bankName }));
			return;
		}

		OfflinePlayer client = org.rominos2.RealBanks.RealBanks.getInstance()
				.getServer().getOfflinePlayer(clientName);
		if (client == null) {
			player.sendMessage(RealBanksCommands.getLang(manager,
					LanguageSettings.SentenceType.CLIENT, "Client.NotFound",
					"<client> does not exists.", new String[] { clientName }));
		}

		if (bank.getAccount(client) != null) {
			if (player.getName().equalsIgnoreCase(clientName)) {
				player.sendMessage(RealBanksCommands.getLang(manager,
						LanguageSettings.SentenceType.BANK,
						"Bank.Subscribe.Self.Already",
						"You are already registered in the <bank> Bank.",
						new String[] { bank.getName() }));
			} else {
				player.sendMessage(RealBanksCommands.getLang(manager,
						LanguageSettings.SentenceType.BANK_CLIENT,
						"Bank.Subscribe.Other.Already",
						"<client> is already registered in the <bank> Bank.",
						new String[] { bank.getName(), client.getName() }));
			}
			return;
		}

		if (bank.isFull()) {
			player.sendMessage(RealBanksCommands.getLang(manager,
					LanguageSettings.SentenceType.BANK, "Bank.Subscribe.Full",
					"<bank> Bank is Full.", new String[] { bank.getName() }));
			return;
		}

		if ((!(bank.getProperties().getEnterPermission().isEmpty()))
				&& (!(org.rominos2.RealBanks.RealBanks.getInstance()
						.askPermissions(player, bank.getProperties()
								.getEnterPermission(), false)))) {
			player.sendMessage(RealBanksCommands
					.getLang(
							manager,
							LanguageSettings.SentenceType.BANK,
							"Bank.Subscribe.Permissions",
							"You don't have the Permissions to subscribe to the <bank> Bank.",
							new String[] { bank.getName() }));
			return;
		}

		if ((org.rominos2.RealBanks.RealBanks.getInstance().isUsingEconomy())
				&& (bank.getProperties().getEnterCost() > 0.0D)
				&& (((!(player.getName().equalsIgnoreCase(client.getName()))) || (!(org.rominos2.RealBanks.RealBanks
						.getInstance().askPermissions(player,
						"realbanks.eco.subscribe.self", bank.getName(), false)))))
				&& (((player.getName().equalsIgnoreCase(client.getName())) || (!(org.rominos2.RealBanks.RealBanks
						.getInstance().askPermissions(player,
						"realbanks.eco.subscribe.other", bank.getName(), false)))))
				&& (!(CommandConfirmation
						.checkConfirmedEco(
								manager,
								player,
								client.getName(),
								bank.getName(),
								bank.getProperties().getEnterCost(),
								confirmed,
								CommandConfirmation.ConfirmableRealBanksCommand.SUBSCRIBE)))) {
			return;
		}

		bank.addAccount(client);
		if (player.getName().equalsIgnoreCase(client.getName())) {
			player.sendMessage(RealBanksCommands.getLang(manager,
					LanguageSettings.SentenceType.BANK,
					"Bank.Subscribe.Self.Done",
					"You've subscribed to the <bank> Bank.",
					new String[] { bank.getName() }));
		} else {
			player.sendMessage(RealBanksCommands.getLang(manager,
					LanguageSettings.SentenceType.BANK_CLIENT,
					"Bank.Subscribe.Other.Done",
					"You've subscribed <client> to the <bank> Bank.",
					new String[] { bank.getName(), client.getName() }));
		}
		RealBanksEvent event = new RealBanksPlayerSubscribeEvent(bank, player,
				client);
		org.rominos2.RealBanks.RealBanks.getInstance().getServer()
				.getPluginManager().callEvent(event);
	}

	public static void unsubscribe(Player player, String bankName,
			String clientName, boolean confirmed) {
		WorldManager manager = org.rominos2.RealBanks.RealBanks.getInstance()
				.getManager(player.getWorld());
		Bank bank = manager.getBank(bankName);
		if (bank == null) {
			player.sendMessage(RealBanksCommands.getLang(manager,
					LanguageSettings.SentenceType.BANK, "Bank.NotFound",
					"<bank> Bank does not exists.", new String[] { bankName }));
			return;
		}

		OfflinePlayer client = org.rominos2.RealBanks.RealBanks.getInstance()
				.getServer().getOfflinePlayer(clientName);
		if (client == null) {
			player.sendMessage(RealBanksCommands.getLang(manager,
					LanguageSettings.SentenceType.CLIENT, "Client.NotFound",
					"<client> does not exists.", new String[] { clientName }));
		}

		if (bank.getAccount(client) == null) {
			if (player.getName().equalsIgnoreCase(client.getName())) {
				player.sendMessage(RealBanksCommands.getLang(manager,
						LanguageSettings.SentenceType.BANK, "Bank.NotIn.Self",
						"You're not registered in the <bank> Bank.",
						new String[] { bank.getName() }));
			} else {
				player.sendMessage(RealBanksCommands.getLang(manager,
						LanguageSettings.SentenceType.BANK_CLIENT,
						"Bank.NotIn.Other",
						"<client> is not registered in the <bank> Bank.",
						new String[] { bank.getName(), client.getName() }));
			}
			return;
		}

		if (!(confirmed)) {
			if (player.getName().equalsIgnoreCase(client.getName())) {
				player.sendMessage(ChatColor.DARK_RED
						+ org.rominos2.RealBanks.RealBanks
								.getInstance()
								.getLang(
										LanguageSettings.SentenceType.NORMAL,
										"Bank.Unsubscribe.Self.Confirm",
										"WARNING ! Unsubscribing will probably suppress your contents from this bank.",
										null));
			} else {
				player.sendMessage(ChatColor.DARK_RED
						+ org.rominos2.RealBanks.RealBanks
								.getInstance()
								.getLang(
										LanguageSettings.SentenceType.CLIENT,
										"Bank.Unsubscribe.Other.Confirm",
										"WARNING ! Unsubscribing will probably suppress <client>'s contents from this bank.",
										new String[] { client.getName() }));
			}
			player.sendMessage(RealBanksCommands.getLang(manager,
					LanguageSettings.SentenceType.NORMAL, "Confirm.Ask",
					"Please Confirm with '/realbanks confirm", null));
			CommandConfirmation
					.waitForConfirmation(
							player,
							CommandConfirmation.ConfirmableRealBanksCommand.UNSUBSCRIBE,
							bank.getName(), client.getName());
			return;
		}

		bank.removeAccount(client);
		if (player.getName().equalsIgnoreCase(client.getName())) {
			player.sendMessage(RealBanksCommands.getLang(manager,
					LanguageSettings.SentenceType.BANK,
					"Bank.Unsubscribe.Self.Done",
					"You're no more in the <bank> Bank.",
					new String[] { bank.getName() }));
		} else {
			player.sendMessage(RealBanksCommands.getLang(manager,
					LanguageSettings.SentenceType.BANK_CLIENT,
					"Bank.Unsubscribe.Other.Done",
					"<client> is no more in the <bank> Bank.", new String[] {
							bank.getName(), client.getName() }));
		}
		RealBanksEvent event = new RealBanksPlayerUnsubscribeEvent(bank,
				player, client);
		org.rominos2.RealBanks.RealBanks.getInstance().getServer()
				.getPluginManager().callEvent(event);
	}
}