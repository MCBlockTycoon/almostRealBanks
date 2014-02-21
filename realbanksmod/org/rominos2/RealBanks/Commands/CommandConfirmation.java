/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.rominos2.RealBanks.Commands;

import java.util.ArrayList;
import java.util.List;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.rominos2.RealBanks.api.Banks.WorldManager;
import org.rominos2.RealBanks.api.Settings.LanguageSettings.SentenceType;

public class CommandConfirmation {
	private Player sender;
	private ConfirmableRealBanksCommand command;
	private String bank;
	private String value;
	private static List<CommandConfirmation> waitings = new ArrayList();

	public CommandConfirmation(Player sender,
			ConfirmableRealBanksCommand command, String bank, String value) {
		this.sender = sender;
		this.command = command;
		this.bank = bank;
		this.value = value;
	}

	public Player getSender() {
		return this.sender;
	}

	public ConfirmableRealBanksCommand getCommand() {
		return this.command;
	}

	public String getBankName() {
		return this.bank;
	}

	public String getValue() {
		return this.value;
	}

	public static void waitForConfirmation(Player player,
			ConfirmableRealBanksCommand command, String bank, String value) {
		CommandConfirmation toRemove = null;
		for (CommandConfirmation wait : waitings) {
			if (wait.getSender().getName().equalsIgnoreCase(player.getName())) {
				toRemove = wait;
				break;
			}
		}
		if (toRemove != null) {
			waitings.remove(toRemove);
		}
		waitings.add(new CommandConfirmation(player, command, bank, value));
	}

	public static void confirm(Player player) {
		for (CommandConfirmation wait : (List) ((ArrayList) waitings).clone())
			if (wait.getSender().getName().equalsIgnoreCase(player.getName())) {
				switch ($SWITCH_TABLE$org$rominos2$RealBanks$Commands$CommandConfirmation$ConfirmableRealBanksCommand()[wait
						.getCommand().ordinal()]) {
				case 1:
					CreateDeleteCommands.create(wait.getSender(),
							wait.getBankName(), true);
					break;
				case 2:
					CreateDeleteCommands.delete(wait.getSender(),
							wait.getBankName(), true);
					break;
				case 3:
					SubscribeUnsubscribeCommands.subscribe(wait.getSender(),
							wait.getBankName(), wait.getValue(), true);
					break;
				case 4:
					SubscribeUnsubscribeCommands.unsubscribe(wait.getSender(),
							wait.getBankName(), wait.getValue(), true);
					break;
				case 5:
					ConnectDisconnectCommands.connect(wait.getSender(),
							wait.getBankName(), true);
					break;
				case 6:
					LinkUnlinkCommands.link(player, wait.getValue(),
							wait.getBankName(), true);
					break;
				case 7:
					LinkUnlinkCommands.unlink(wait.getSender(),
							wait.getBankName(), true);
				}
				waitings.remove(wait);
				return;
			}
	}

	public static boolean checkConfirmedEco(WorldManager manager,
			Player player, String client, String bank, double cost,
			boolean confirmed, ConfirmableRealBanksCommand command) {
		Economy eco = org.rominos2.RealBanks.RealBanks.getInstance()
				.getEconomy();
		String costString = cost
				+ " "
				+ ((cost > 1.0D) ? eco.currencyNamePlural() : eco
						.currencyNameSingular());

		if (!(eco.hasAccount(player.getName()))) {
			player.sendMessage(RealBanksCommands.getLang(manager,
					LanguageSettings.SentenceType.NORMAL, "Eco.NeedAccount",
					"You need an Economy Account to do that.", null));
			return false;
		}

		if (!(eco.has(player.getName(), cost))) {
			player.sendMessage(RealBanksCommands
					.getLang(manager, LanguageSettings.SentenceType.VALUE,
							"Eco.NotEnough", "You need <value> to do that.",
							new String[] { costString }));
			return false;
		}

		if (!(confirmed)) {
			player.sendMessage(ChatColor.DARK_RED
					+ org.rominos2.RealBanks.RealBanks.getInstance().getLang(
							LanguageSettings.SentenceType.VALUE, "Eco.Confirm",
							"WARNING ! You need <value> to do that.",
							new String[] { costString }));
			player.sendMessage(RealBanksCommands.getLang(manager,
					LanguageSettings.SentenceType.NORMAL, "Confirm.Ask",
					"Please Confirm with '/realbanks confirm", null));
			waitForConfirmation(player, command, bank, client);
			return false;
		}

		eco.withdrawPlayer(player.getName(), cost);
		return true;
	}

	public static enum ConfirmableRealBanksCommand {
		CREATE, DELETE, SUBSCRIBE, UNSUBSCRIBE, CONNECT, LINK, UNLINK;
	}
}