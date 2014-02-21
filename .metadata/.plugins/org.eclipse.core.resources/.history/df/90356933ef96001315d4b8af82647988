/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.rominos2.RealBanks.Commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.rominos2.RealBanks.api.Banks.Bank;
import org.rominos2.RealBanks.api.Banks.WorldManager;
import org.rominos2.RealBanks.api.Settings.LanguageSettings.SentenceType;

public class HelpCommand {
	public static void listCommands(Player player) {
		player.sendMessage(ChatColor.DARK_GREEN
				+ "===[ "
				+ ChatColor.YELLOW
				+ org.rominos2.RealBanks.RealBanks.getInstance().getLang(
						LanguageSettings.SentenceType.NORMAL, "Command.Title",
						"RealBanks Commands", null) + ChatColor.DARK_GREEN
				+ " ]===");
		if (canSeeManagerCommands(player)) {
			player.sendMessage(ChatColor.BLUE
					+ "/realbanks manager"
					+ ChatColor.YELLOW
					+ " : "
					+ org.rominos2.RealBanks.RealBanks.getInstance().getLang(
							LanguageSettings.SentenceType.NORMAL,
							"Command.Manager", "See the Manager Commands.",
							null));
		}
		if (org.rominos2.RealBanks.RealBanks.getInstance().askPermissions(
				player, "realbanks.create", false)) {
			player.sendMessage(ChatColor.DARK_GREEN
					+ "/realbanks create [bank]"
					+ ChatColor.YELLOW
					+ " : "
					+ org.rominos2.RealBanks.RealBanks.getInstance().getLang(
							LanguageSettings.SentenceType.NORMAL,
							"Command.Create", "Create a RealBank.", null));
		}
		if (org.rominos2.RealBanks.RealBanks.getInstance().askPermissions(
				player, "realbanks.delete", false)) {
			player.sendMessage(ChatColor.DARK_GREEN
					+ "/realbanks delete [bank]"
					+ ChatColor.YELLOW
					+ " : "
					+ org.rominos2.RealBanks.RealBanks.getInstance().getLang(
							LanguageSettings.SentenceType.NORMAL,
							"Command.Delete", "Delete the RealBank.", null));
		}
		if (org.rominos2.RealBanks.RealBanks.getInstance().askPermissions(
				player, "realbanks.subscribe", false)) {
			player.sendMessage(ChatColor.DARK_GREEN
					+ "/realbanks subscribe [bank]"
					+ ChatColor.YELLOW
					+ " : "
					+ org.rominos2.RealBanks.RealBanks.getInstance().getLang(
							LanguageSettings.SentenceType.NORMAL,
							"Command.Subscribe.Self",
							"Subscribe to a RealBank.", null));
			player.sendMessage(ChatColor.DARK_GREEN
					+ "/realbanks unsubscribe [bank]"
					+ ChatColor.YELLOW
					+ " : "
					+ org.rominos2.RealBanks.RealBanks.getInstance().getLang(
							LanguageSettings.SentenceType.NORMAL,
							"Command.Unsubscribe.Self",
							"Unsubscribe from a RealBank.", null));
		}
		if (org.rominos2.RealBanks.RealBanks.getInstance().askPermissions(
				player, "realbanks.send", false)) {
			player.sendMessage(ChatColor.DARK_GREEN
					+ "/realbanks send [bank]"
					+ ChatColor.YELLOW
					+ " : "
					+ org.rominos2.RealBanks.RealBanks.getInstance().getLang(
							LanguageSettings.SentenceType.NORMAL,
							"Command.Send",
							"Send Items in your hand to a RealBank.", null));
		}
		if (org.rominos2.RealBanks.RealBanks.getInstance().askPermissions(
				player, "realbanks.get", false)) {
			player.sendMessage(ChatColor.DARK_GREEN
					+ "/realbanks get [bank] [material] <number>"
					+ ChatColor.YELLOW
					+ " : "
					+ org.rominos2.RealBanks.RealBanks.getInstance().getLang(
							LanguageSettings.SentenceType.NORMAL,
							"Command.Get",
							"Get Items from a RealBank to your hand.", null));
		}
		if (org.rominos2.RealBanks.RealBanks.getInstance().askPermissions(
				player, "realbanks.list.banks", false))
			player.sendMessage(ChatColor.DARK_GREEN
					+ "/realbanks list <page>"
					+ ChatColor.YELLOW
					+ " : "
					+ org.rominos2.RealBanks.RealBanks.getInstance().getLang(
							LanguageSettings.SentenceType.NORMAL,
							"Command.List.Banks",
							"List the RealBanks in this World.", null));
	}

	public static void listManagerCommands(Player player) {
		player.sendMessage(ChatColor.DARK_PURPLE
				+ "===[ "
				+ ChatColor.BLUE
				+ org.rominos2.RealBanks.RealBanks.getInstance().getLang(
						LanguageSettings.SentenceType.NORMAL,
						"Command.ManagerTitle", "RealBanks Manager Commands",
						null) + ChatColor.DARK_PURPLE + " ]===");
		if (org.rominos2.RealBanks.RealBanks.getInstance().askPermissions(
				player, "realbanks.reload", false)) {
			player.sendMessage(ChatColor.DARK_GREEN
					+ "/realbanks reload"
					+ ChatColor.YELLOW
					+ " : "
					+ org.rominos2.RealBanks.RealBanks.getInstance().getLang(
							LanguageSettings.SentenceType.NORMAL,
							"Command.Reload", "Reload the RealBanks plugin.",
							null));
		}
		if ((org.rominos2.RealBanks.RealBanks.getInstance().askPermissions(
				player, "realbanks.connect", false)) || (isBankManager(player))) {
			player.sendMessage(ChatColor.DARK_GREEN
					+ "/realbanks connect [bank]"
					+ ChatColor.YELLOW
					+ " : "
					+ org.rominos2.RealBanks.RealBanks
							.getInstance()
							.getLang(
									LanguageSettings.SentenceType.NORMAL,
									"Command.Connect",
									"Connect the Chest you're looking at to a RealBank.",
									null));
			player.sendMessage(ChatColor.DARK_GREEN
					+ "/realbanks disconnect"
					+ ChatColor.YELLOW
					+ " : "
					+ org.rominos2.RealBanks.RealBanks
							.getInstance()
							.getLang(
									LanguageSettings.SentenceType.NORMAL,
									"Command.Disconnect",
									"Disconnect the Chest you're looking at from a RealBank.",
									null));
		}
		if ((org.rominos2.RealBanks.RealBanks.getInstance().askPermissions(
				player, "realbanks.subscribe.other", false))
				|| (isBankManager(player))) {
			player.sendMessage(ChatColor.DARK_GREEN
					+ "/realbanks subscribe [bank] <client>"
					+ ChatColor.YELLOW
					+ " : "
					+ org.rominos2.RealBanks.RealBanks.getInstance().getLang(
							LanguageSettings.SentenceType.NORMAL,
							"Command.Subscribe.Other",
							"Subscribe a Client to a RealBank.", null));
			player.sendMessage(ChatColor.DARK_GREEN
					+ "/realbanks unsubscribe [bank] <client>"
					+ ChatColor.YELLOW
					+ " : "
					+ org.rominos2.RealBanks.RealBanks.getInstance().getLang(
							LanguageSettings.SentenceType.NORMAL,
							"Command.Unsubscribe.Other",
							"Unsubscribe a Client to a RealBank.", null));
		}
		if ((org.rominos2.RealBanks.RealBanks.getInstance().askPermissions(
				player, "realbanks.modify", false)) || (isBankManager(player))) {
			player.sendMessage(ChatColor.DARK_GREEN
					+ "/realbanks modify [bank] <limit|permission|log|cost> <value>"
					+ ChatColor.YELLOW
					+ " : "
					+ org.rominos2.RealBanks.RealBanks.getInstance().getLang(
							LanguageSettings.SentenceType.NORMAL,
							"Command.Modify", "Change Bank Settings Values.",
							null));
		}
		if ((org.rominos2.RealBanks.RealBanks.getInstance().askPermissions(
				player, "realbanks.edit", false)) || (isBankManager(player))) {
			player.sendMessage(ChatColor.DARK_GREEN
					+ "/realbanks edit [bank] [client]"
					+ ChatColor.YELLOW
					+ " : "
					+ org.rominos2.RealBanks.RealBanks.getInstance().getLang(
							LanguageSettings.SentenceType.NORMAL,
							"Command.Edit",
							"Edit the Contents of the Account of the Client.",
							null));
		}
		if ((org.rominos2.RealBanks.RealBanks.getInstance().askPermissions(
				player, "realbanks.as", false)) || (isBankManager(player))) {
			player.sendMessage(ChatColor.DARK_GREEN
					+ "/realbanks as [bank] [client]"
					+ ChatColor.YELLOW
					+ " : "
					+ org.rominos2.RealBanks.RealBanks
							.getInstance()
							.getLang(
									LanguageSettings.SentenceType.NORMAL,
									"Command.As",
									"Open the chest of the Bank as if you were the Client.",
									null));
		}
		if ((org.rominos2.RealBanks.RealBanks.getInstance().askPermissions(
				player, "realbanks.list.clients", false))
				|| (isBankManager(player))) {
			player.sendMessage(ChatColor.DARK_GREEN
					+ "/realbanks list [bank] <page>"
					+ ChatColor.YELLOW
					+ " : "
					+ org.rominos2.RealBanks.RealBanks.getInstance().getLang(
							LanguageSettings.SentenceType.NORMAL,
							"Command.List.Clients",
							"List the Clients in the Bank.", null));
		}
		if ((org.rominos2.RealBanks.RealBanks.getInstance().askPermissions(
				player, "realbanks.link", false)) || (isBankManager(player))) {
			player.sendMessage(ChatColor.DARK_GREEN
					+ "/realbanks link [world] [bank]"
					+ ChatColor.YELLOW
					+ " : "
					+ org.rominos2.RealBanks.RealBanks.getInstance().getLang(
							LanguageSettings.SentenceType.NORMAL,
							"Command.link",
							"Create a Bank Frontage from another World.", null));
		}
		if ((org.rominos2.RealBanks.RealBanks.getInstance().askPermissions(
				player, "realbanks.unlink", false)) || (isBankManager(player)))
			player.sendMessage(ChatColor.DARK_GREEN
					+ "/realbanks unlink [bank]"
					+ ChatColor.YELLOW
					+ " : "
					+ org.rominos2.RealBanks.RealBanks.getInstance()
							.getLang(LanguageSettings.SentenceType.NORMAL,
									"Command.Unlink",
									"Delete the Bank Frontage.", null));
	}

	private static boolean canSeeManagerCommands(Player player) {
		return ((org.rominos2.RealBanks.RealBanks.getInstance().askPermissions(
				player, "realbanks.connect", false))
				|| (org.rominos2.RealBanks.RealBanks.getInstance()
						.askPermissions(player, "realbanks.reload", false))
				|| (org.rominos2.RealBanks.RealBanks.getInstance()
						.askPermissions(player, "realbanks.modify", false))
				|| (org.rominos2.RealBanks.RealBanks.getInstance()
						.askPermissions(player, "realbanks.edit", false))
				|| (org.rominos2.RealBanks.RealBanks.getInstance()
						.askPermissions(player, "realbanks.as", false))
				|| (org.rominos2.RealBanks.RealBanks.getInstance()
						.askPermissions(player, "realbanks.list.clients", false))
				|| (org.rominos2.RealBanks.RealBanks.getInstance()
						.askPermissions(player, "realbanks.link", false))
				|| (org.rominos2.RealBanks.RealBanks.getInstance()
						.askPermissions(player, "realbanks.unlink", false)) || (isBankManager(player)));
	}

	private static boolean isBankManager(Player player) {
		for (Bank bank : org.rominos2.RealBanks.RealBanks.getInstance()
				.getManager(player.getWorld()).getBanks()) {
			if (org.rominos2.RealBanks.RealBanks.getInstance().askPermissions(
					player, "realbanks.bank." + bank.getName(), false)) {
				return true;
			}
		}
		return false;
	}
}