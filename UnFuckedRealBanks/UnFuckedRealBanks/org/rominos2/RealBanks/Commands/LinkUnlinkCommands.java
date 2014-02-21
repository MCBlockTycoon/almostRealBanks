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
import org.rominos2.RealBanks.api.Settings.LanguageSettings;

public class LinkUnlinkCommands {
	public static void link(Player player, String worldName, String bankName,
			boolean confirmed) {
		WorldManager manager = org.rominos2.RealBanks.RealBanks.getInstance()
				.getManager(player.getWorld());

		World world = org.rominos2.RealBanks.RealBanks.getInstance()
				.getServer().getWorld(worldName);
		if (world == null) {
			player.sendMessage(RealBanksCommands.getLang(manager,
					LanguageSettings.SentenceType.WORLD, "World.NotFound",
					"World <world> does not exists.",
					new String[] { worldName }));
			return;
		}

		Bank bank = manager.getBank(bankName);
		if (bank != null) {
			player.sendMessage(RealBanksCommands.getLang(manager,
					LanguageSettings.SentenceType.BANK, "Bank.Link.NotLink",
					"<bank> Bank already exists and is not a linked Bank.",
					new String[] { bank.getName() }));
			return;
		}

		WorldManager from = org.rominos2.RealBanks.RealBanks.getInstance()
				.getManager(world);
		bank = from.getBank(bankName);
		if (bank == null) {
			player.sendMessage(RealBanksCommands.getLang(manager,
					LanguageSettings.SentenceType.BANK_WORLD,
					"Bank.Link.NotFound",
					"<bank> Bank does not exists in <world>.", new String[] {
							bankName, world.getName() }));
			return;
		}

		if (bank.isFrontageBank()) {
			player.sendMessage(RealBanksCommands.getLang(manager,
					LanguageSettings.SentenceType.BANK_WORLD,
					"Bank.Link.Already",
					"<bank> Bank is already a Linked Bank in <world>.",
					new String[] { bankName, world.getName() }));
			return;
		}

		if ((org.rominos2.RealBanks.RealBanks.getInstance().isUsingEconomy())
				&& (((org.rominos2.RealBanks.Settings.WorldSettings) manager
						.getProperties()).getLinkCost() > 0.0D)
				&& (!(org.rominos2.RealBanks.RealBanks.getInstance()
						.askPermissions(player, "realbanks.eco.link", false)))
				&& (!(CommandConfirmation
						.checkConfirmedEco(
								manager,
								player,
								world.getName(),
								bank.getName(),
								((org.rominos2.RealBanks.Settings.WorldSettings) manager
										.getProperties()).getLinkCost(),
								confirmed,
								CommandConfirmation.ConfirmableRealBanksCommand.LINK)))) {
			return;
		}

		manager.createFrontageBank(bank);
		player.sendMessage(RealBanksCommands.getLang(manager,
				LanguageSettings.SentenceType.BANK_WORLD, "Bank.Link.Done",
				"<bank> Bank is now linked from <world>.",
				new String[] { bank.getName(), bank.getWorld().getName() }));

		if (manager.getProperties().isLog()) {
			org.rominos2.RealBanks.RealBanks
					.getInstance()
					.getLogger()
					.info(player.getName() + " linked the " + bank.getName()
							+ " Bank from "
							+ from.getBank(bank.getName()).getWorld().getName()
							+ " in " + player.getWorld().getName() + ".");
		}
		RealBanksEvent event = new RealBanksBankCreationEvent(bank, player);
		org.rominos2.RealBanks.RealBanks.getInstance().getServer()
				.getPluginManager().callEvent(event);
	}

	public static void unlink(Player player, String bankName, boolean confirmed) {
		WorldManager manager = org.rominos2.RealBanks.RealBanks.getInstance()
				.getManager(player.getWorld());
		Bank bank = manager.getBank(bankName);
		if (bank == null) {
			player.sendMessage(RealBanksCommands.getLang(manager,
					LanguageSettings.SentenceType.BANK, "Bank.NotFound",
					"<bank> Bank does not exists.", new String[] { bankName }));
			return;
		}

		if (!(bank.isFrontageBank())) {
			player.sendMessage(RealBanksCommands.getLang(manager,
					LanguageSettings.SentenceType.BANK, "Bank.Unlink.NotLink",
					"<bank> Bank is not a linked Bank.",
					new String[] { bank.getName() }));
			return;
		}

		if (!(confirmed)) {
			player.sendMessage(ChatColor.DARK_RED
					+ org.rominos2.RealBanks.RealBanks
							.getInstance()
							.getLang(
									LanguageSettings.SentenceType.NORMAL,
									"Bank.Unlink.Confirm",
									"WARNING ! Unlinking this bank will disconnect all its chests.",
									null));
			player.sendMessage(RealBanksCommands.getLang(manager,
					LanguageSettings.SentenceType.NORMAL, "Confirm.Ask",
					"Please Confirm with '/realbanks confirm", null));
			CommandConfirmation.waitForConfirmation(player,
					CommandConfirmation.ConfirmableRealBanksCommand.UNLINK,
					bank.getName(), null);
			return;
		}

		manager.deleteBank(bank);
		player.sendMessage(RealBanksCommands.getLang(manager,
				LanguageSettings.SentenceType.BANK_WORLD, "Bank.Unlink.Done",
				"<bank> Bank has been unlinked from <world>.", new String[] {
						bank.getName(), bank.getWorld().getName() }));
		if (manager.getProperties().isLog()) {
			org.rominos2.RealBanks.RealBanks
					.getInstance()
					.getLogger()
					.info(player.getName() + " unlinked the " + bank.getName()
							+ " Bank in " + player.getWorld().getName() + ".");
		}
		RealBanksEvent event = new RealBanksBankDeletionEvent(bank, player);
		org.rominos2.RealBanks.RealBanks.getInstance().getServer()
				.getPluginManager().callEvent(event);
	}
}