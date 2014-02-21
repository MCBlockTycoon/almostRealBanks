/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.rominos2.RealBanks.Commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.rominos2.RealBanks.api.Banks.WorldManager;
import org.rominos2.RealBanks.api.Settings.LanguageSettings.SentenceType;
import org.rominos2.RealBanks.api.Settings.WorldSettings;
import org.rominos2.RealBanks.api.Settings.LanguageSettings;

public class RealBanksCommands {
	public static boolean onCommand(Player player, String[] args) {
		if (!(org.rominos2.RealBanks.RealBanks.getInstance()
				.getManager(player.getWorld()).getProperties().isActive())) {
			player.sendMessage(ChatColor.DARK_RED
					+ org.rominos2.RealBanks.RealBanks.getInstance().getLang(
							LanguageSettings.SentenceType.NORMAL,
							"Error.Inactive",
							"RealBanks is inactive in this World.", null));
			return true;
		}

		if (args.length == 0) {
			HelpCommand.listCommands(player);
			return true;
		}
		if ((args.length >= 1) && (args[0].equalsIgnoreCase("manager"))) {
			HelpCommand.listManagerCommands(player);
			return true;
		}

		if ((args.length >= 2) && (args[0].equalsIgnoreCase("create"))) {
			if (org.rominos2.RealBanks.RealBanks.getInstance().askPermissions(
					player, "realbanks.create", true)) {
				CreateDeleteCommands.create(player, args[1], false);
			}
			return true;
		}

		if ((args.length >= 2) && (args[0].equalsIgnoreCase("delete"))) {
			if (org.rominos2.RealBanks.RealBanks.getInstance().askPermissions(
					player, "realbanks.delete", true)) {
				CreateDeleteCommands.delete(player, args[1], false);
			}
			return true;
		}

		if ((args.length >= 2) && (args[0].equalsIgnoreCase("connect"))) {
			if (org.rominos2.RealBanks.RealBanks.getInstance().askPermissions(
					player, "realbanks.connect", args[1], true)) {
				ConnectDisconnectCommands.connect(player, args[1], false);
			}
			return true;
		}

		if ((args.length >= 1) && (args[0].equalsIgnoreCase("disconnect"))) {
			if ((org.rominos2.RealBanks.RealBanks.getInstance().askPermissions(
					player, "realbanks.connect", false))
					|| (ConnectDisconnectCommands
							.isManagerForDisconnection(player))) {
				ConnectDisconnectCommands.disconnect(player);
			}
			return true;
		}

		if ((args.length >= 1) && (args[0].equalsIgnoreCase("list"))) {
			if (org.rominos2.RealBanks.RealBanks.getInstance().askPermissions(
					player, "realbanks.list.banks", true)) {
				if (args.length == 1) {
					ListCommand.listBanks(player, 1);
					return true;
				}
				if ((args.length == 2) && (args[1].matches("[0-9]+"))) {
					ListCommand.listBanks(player, Integer.valueOf(args[1])
							.intValue());
					return true;
				}
			}
			if ((args.length >= 2)
					&& (org.rominos2.RealBanks.RealBanks.getInstance()
							.askPermissions(player, "realbanks.list.clients",
									args[1], true))) {
				if (args.length == 2) {
					ListCommand.listPlayersInBank(player, args[1], 1);
					return true;
				}
				if ((args.length == 3) && (args[2].matches("[0-9]+"))) {
					ListCommand.listPlayersInBank(player, args[1], Integer
							.valueOf(args[2]).intValue());
					return true;
				}
			}
			return true;
		}

		if ((args.length >= 2) && (args[0].equalsIgnoreCase("subscribe"))) {
			if ((args.length == 2)
					&& (org.rominos2.RealBanks.RealBanks.getInstance()
							.askPermissions(player, "realbanks.subscribe.self",
									args[1], true))) {
				SubscribeUnsubscribeCommands.subscribe(player, args[1],
						player.getName(), false);
			}

			if ((args.length >= 3)
					&& (org.rominos2.RealBanks.RealBanks.getInstance()
							.askPermissions(player,
									"realbanks.subscribe.others", args[1], true))) {
				SubscribeUnsubscribeCommands.subscribe(player, args[1],
						args[2], false);
			}

			return true;
		}

		if ((args.length >= 2) && (args[0].equalsIgnoreCase("unsubscribe"))) {
			if ((args.length == 2)
					&& (org.rominos2.RealBanks.RealBanks.getInstance()
							.askPermissions(player,
									"realbanks.unsubscribe.self", args[1], true))) {
				SubscribeUnsubscribeCommands.unsubscribe(player, args[1],
						player.getName(), false);
			}

			if ((args.length >= 3)
					&& (org.rominos2.RealBanks.RealBanks.getInstance()
							.askPermissions(player,
									"realbanks.unsubscribe.others", args[1],
									true))) {
				SubscribeUnsubscribeCommands.unsubscribe(player, args[1],
						args[2], false);
			}

			return true;
		}

		if ((args.length == 1) && (args[0].equalsIgnoreCase("confirm"))) {
			CommandConfirmation.confirm(player);
			return true;
		}

		if ((args.length >= 2) && (args[0].equalsIgnoreCase("modify"))) {
			if (org.rominos2.RealBanks.RealBanks.getInstance().askPermissions(
					player, "realbanks.modify", args[1], true)) {
				return ManagerCommands.modify(player, args);
			}
			return true;
		}

		if ((args.length >= 3) && (args[0].equalsIgnoreCase("edit"))) {
			if (org.rominos2.RealBanks.RealBanks.getInstance().askPermissions(
					player, "realbanks.edit", args[1], true)) {
				ManagerCommands.edit(player, args[1], args[2]);
			}
			return true;
		}

		if ((args.length >= 3) && (args[0].equalsIgnoreCase("as"))) {
			if (org.rominos2.RealBanks.RealBanks.getInstance().askPermissions(
					player, "realbanks.as", args[1], true)) {
				ManagerCommands.as(player, args[1], args[2]);
			}
			return true;
		}

		if ((args.length >= 2) && (args[0].equalsIgnoreCase("send"))) {
			if (org.rominos2.RealBanks.RealBanks.getInstance().askPermissions(
					player, "realbanks.send", args[1], true)) {
				GetSendCommands.send(player, args[1]);
			}
			return true;
		}

		if ((args.length >= 3) && (args[0].equalsIgnoreCase("get"))) {
			if (org.rominos2.RealBanks.RealBanks.getInstance().askPermissions(
					player, "realbanks.get", args[1], true)) {
				int number = 1;
				if (args.length >= 4) {
					if (!(args[3].matches("[0-9]+"))) {
						return false;
					}
					number = Math.max(1, Integer.valueOf(args[3]).intValue());
				}
				GetSendCommands.get(player, args[1], args[2], number);
			}
			return true;
		}

		if ((args.length >= 1) && (args[0].equalsIgnoreCase("reload"))) {
			if (org.rominos2.RealBanks.RealBanks.getInstance().askPermissions(
					player, "realbanks.reload", true)) {
				ManagerCommands.reload(player);
			}
			return true;
		}

		if ((args.length >= 3)
				&& (args[0].equalsIgnoreCase("link"))
				&& (org.rominos2.RealBanks.RealBanks.getInstance()
						.askPermissions(player, "realbanks.link", args[2], true))) {
			LinkUnlinkCommands.link(player, args[1], args[2], false);
			return true;
		}

		if ((args.length >= 2)
				&& (args[0].equalsIgnoreCase("unlink"))
				&& (org.rominos2.RealBanks.RealBanks.getInstance()
						.askPermissions(player, "realbanks.unlink", args[1],
								true))) {
			LinkUnlinkCommands.unlink(player, args[1], false);
			return true;
		}

		return false;
	}

	public static String getLang(WorldManager manager,
			LanguageSettings.SentenceType type, String node, String def,
			String[] args) {
		return manager.getProperties().getColor()
				+ org.rominos2.RealBanks.RealBanks.getInstance().getLang(type,
						node, def, args);
	}
}