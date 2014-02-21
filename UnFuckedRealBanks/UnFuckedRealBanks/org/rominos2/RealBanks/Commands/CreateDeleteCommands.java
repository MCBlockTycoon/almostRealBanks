/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.rominos2.RealBanks.Commands;

import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.rominos2.RealBanks.api.Banks.Bank;
import org.rominos2.RealBanks.api.Banks.WorldManager;
import org.rominos2.RealBanks.api.Events.RealBanksBankCreationEvent;
import org.rominos2.RealBanks.api.Events.RealBanksBankDeletionEvent;
import org.rominos2.RealBanks.api.Events.RealBanksEvent;
import org.rominos2.RealBanks.api.Settings.LanguageSettings.SentenceType;
import org.rominos2.RealBanks.api.Settings.WorldSettings;
import org.rominos2.RealBanks.api.Settings.*;
import org.rominos2.RealBanks.*;


public class CreateDeleteCommands {
	public static void create(Player player, String name, boolean confirmed) {
		WorldManager manager = org.rominos2.RealBanks.RealBanks.getInstance()
				.getManager(player.getWorld());
		if (manager.getBank(name) != null) {
			player.sendMessage(RealBanksCommands.getLang(manager,
					LanguageSettings.SentenceType.BANK, "Bank.Create.Already",
					"<bank> Bank already exists.", new String[] { name }));
			return;
		}

		if ((org.rominos2.RealBanks.RealBanks.getInstance().isUsingEconomy())
				&& (manager.getProperties().getCreateCost() > 0.0D)
				&& (!(org.rominos2.RealBanks.RealBanks.getInstance()
						.askPermissions(player, "realbanks.eco.create", false)))
				&& (!(CommandConfirmation.checkConfirmedEco(manager, player,
						null, name, manager.getProperties().getCreateCost(),
						confirmed,
						CommandConfirmation.ConfirmableRealBanksCommand.CREATE)))) {
			return;
		}

		Bank bank = manager.createBank(name);
		player.sendMessage(RealBanksCommands.getLang(manager,
				LanguageSettings.SentenceType.BANK, "Bank.Create.Done",
				"<bank> Bank has been created.", new String[] { name }));
		if (manager.getProperties().isLog()) {
			org.rominos2.RealBanks.RealBanks
					.getInstance()
					.getLogger()
					.info(player.getName() + " created the " + name
							+ " Bank in " + player.getWorld().getName() + ".");
		}
		RealBanksEvent event = new RealBanksBankCreationEvent(bank, player);
		org.rominos2.RealBanks.RealBanks.getInstance().getServer()
				.getPluginManager().callEvent(event);
	}

	public static void delete(Player player, String name, boolean confirmed) {
		WorldManager manager = org.rominos2.RealBanks.RealBanks.getInstance()
				.getManager(player.getWorld());
		Bank bank = manager.getBank(name);
		if (bank == null) {
			player.sendMessage(RealBanksCommands.getLang(manager,
					LanguageSettings.SentenceType.BANK, "Bank.NotFound",
					"<bank> Bank does not exists.", new String[] { name }));
			return;
		}

		if (bank.isFrontageBank()) {
			player.sendMessage(RealBanksCommands.getLang(manager,
					LanguageSettings.SentenceType.BANK, "Bank.Delete.Link",
					"<bank> Bank is a link. It can't be deleted.",
					new String[] { bank.getName() }));
			return;
		}

		if (!(confirmed)) {
			player.sendMessage(ChatColor.DARK_RED
					+ org.rominos2.RealBanks.RealBanks
							.getInstance()
							.getLang(
									LanguageSettings.SentenceType.NORMAL,
									"Bank.Delete.Confirm",
									"WARNING ! Deleting this bank will probably suppress all accounts and all the player's contents",
									null));
			player.sendMessage(RealBanksCommands.getLang(manager,
					LanguageSettings.SentenceType.NORMAL, "Confirm.Ask",
					"Please Confirm with '/realbanks confirm", null));
			CommandConfirmation.waitForConfirmation(player,
					CommandConfirmation.ConfirmableRealBanksCommand.DELETE,
					bank.getName(), null);
			return;
		}

		manager.deleteBank(bank);
		player.sendMessage(RealBanksCommands.getLang(manager,
				LanguageSettings.SentenceType.BANK, "Bank.Delete.Done",
				"<bank> has been deleted.", new String[] { bank.getName() }));
		if (manager.getProperties().isLog()) {
			org.rominos2.RealBanks.RealBanks
					.getInstance()
					.getLogger()
					.info(player.getName() + " deleted the " + name
							+ " Bank in " + player.getWorld().getName() + ".");
		}
		RealBanksEvent event = new RealBanksBankDeletionEvent(bank, player);
		org.rominos2.RealBanks.RealBanks.getInstance().getServer()
				.getPluginManager().callEvent(event);
	}
}