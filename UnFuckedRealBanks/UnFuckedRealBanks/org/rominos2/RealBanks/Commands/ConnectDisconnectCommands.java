/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.rominos2.RealBanks.Commands;

import java.util.logging.Logger;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.rominos2.RealBanks.api.Banks.Bank;
import org.rominos2.RealBanks.api.Banks.WorldManager;
import org.rominos2.RealBanks.api.Events.RealBanksChestConnectionEvent;
import org.rominos2.RealBanks.api.Events.RealBanksChestDisconnectionEvent;
import org.rominos2.RealBanks.api.Events.RealBanksEvent;
import org.rominos2.RealBanks.api.Settings.LanguageSettings.SentenceType;
import org.rominos2.RealBanks.api.Settings.WorldSettings;
import org.rominos2.RealBanks.api.Settings.*;
import org.rominos2.RealBanks.*;


public class ConnectDisconnectCommands {
	public static void connect(Player player, String bankName, boolean confirmed) {
		WorldManager manager = org.rominos2.RealBanks.RealBanks.getInstance()
				.getManager(player.getWorld());
		Block block = player.getTargetBlock(null, 10);
		if ((block == null) || (block.getTypeId() != 54)
				|| (!(block.getState() instanceof Chest))) {
			player.sendMessage(RealBanksCommands.getLang(manager,
					LanguageSettings.SentenceType.NORMAL, "Chest.NoChest",
					"You must target a chest to use this command.", null));
			return;
		}

		Chest chest = (Chest) block.getState();
		Bank bank = manager.getBank(chest);
		if (bank != null) {
			player.sendMessage(RealBanksCommands.getLang(manager,
					LanguageSettings.SentenceType.BANK,
					"Chest.Connect.Already",
					"This Chest is already connected to the <bank> Bank.",
					new String[] { bank.getName() }));
			return;
		}

		bank = manager.getBank(bankName);
		if (bank == null) {
			player.sendMessage(RealBanksCommands.getLang(manager,
					LanguageSettings.SentenceType.BANK, "Bank.NotFound",
					"<bank> Bank does not exists", new String[] { bankName }));
			return;
		}

		if ((org.rominos2.RealBanks.RealBanks.getInstance().isUsingEconomy())
				&& (manager.getProperties().getConnectCost() > 0.0D)
				&& (!(org.rominos2.RealBanks.RealBanks.getInstance()
						.askPermissions(player, "realbanks.eco.connect", false)))
				&& (!(CommandConfirmation
						.checkConfirmedEco(
								manager,
								player,
								null,
								bank.getName(),
								manager.getProperties().getConnectCost(),
								confirmed,
								CommandConfirmation.ConfirmableRealBanksCommand.CONNECT)))) {
			return;
		}

		bank.addChest(chest);
		player.sendMessage(RealBanksCommands.getLang(manager,
				LanguageSettings.SentenceType.BANK, "Chest.Connect.Done",
				"This chest is now connected to the <bank> Bank.",
				new String[] { bank.getName() }));
		if (manager.getProperties().isLog()) {
			org.rominos2.RealBanks.RealBanks
					.getInstance()
					.getLogger()
					.info(player.getName() + " connected a Chest in "
							+ player.getWorld().getName() + " at "
							+ block.getX() + "," + block.getY() + ","
							+ block.getZ() + " to the " + bankName + " Bank.");
		}
		RealBanksEvent event = new RealBanksChestConnectionEvent(bank, player,
				chest);
		org.rominos2.RealBanks.RealBanks.getInstance().getServer()
				.getPluginManager().callEvent(event);
	}

	public static void disconnect(Player player) {
		WorldManager manager = org.rominos2.RealBanks.RealBanks.getInstance()
				.getManager(player.getWorld());
		Block block = player.getTargetBlock(null, 10);
		if ((block == null) || (block.getTypeId() != 54)
				|| (!(block.getState() instanceof Chest))) {
			player.sendMessage(RealBanksCommands.getLang(manager,
					LanguageSettings.SentenceType.NORMAL, "Chest.NoChest",
					"You must target a chest to use this command.", null));
			return;
		}

		Chest chest = (Chest) block.getState();
		Bank bank = manager.getBank(chest);
		if (bank == null) {
			player.sendMessage(RealBanksCommands.getLang(manager,
					LanguageSettings.SentenceType.NORMAL,
					"Chest.Disconnect.NotConnected",
					"This Chest is not connected to a Bank.", null));
			return;
		}

		if (!(org.rominos2.RealBanks.RealBanks.getInstance().askPermissions(
				player, "realbanks.connect", bank.getName(), true))) {
			return;
		}

		if (bank.getTransaction(chest) != null) {
			player.sendMessage(RealBanksCommands
					.getLang(
							manager,
							LanguageSettings.SentenceType.NORMAL,
							"Chest.Transaction",
							"This chest is already in a Transaction with another Player. Please wait...",
							null));
			return;
		}

		bank.removeChest(chest);
		player.sendMessage(RealBanksCommands.getLang(manager,
				LanguageSettings.SentenceType.BANK, "Chest.Disconnect.Done",
				"This chest is now disconnected from the <bank> Bank.",
				new String[] { bank.getName() }));
		if (manager.getProperties().isLog()) {
			org.rominos2.RealBanks.RealBanks
					.getInstance()
					.getLogger()
					.info(player.getName() + " disconnected a Chest in "
							+ player.getWorld().getName() + " at "
							+ block.getX() + "," + block.getY() + ","
							+ block.getZ() + " from the " + bank.getName()
							+ " Bank.");
		}
		RealBanksEvent event = new RealBanksChestDisconnectionEvent(bank,
				player, chest);
		org.rominos2.RealBanks.RealBanks.getInstance().getServer()
				.getPluginManager().callEvent(event);
	}

	public static boolean isManagerForDisconnection(Player player) {
		WorldManager manager = org.rominos2.RealBanks.RealBanks.getInstance()
				.getManager(player.getWorld());
		Block block = player.getTargetBlock(null, 10);
		if ((block == null) || (block.getTypeId() != 54)
				|| (!(block.getState() instanceof Chest))) {
			return false;
		}
		Bank bank = manager.getBank((Chest) block.getState());
		if (bank == null) {
			return false;
		}
		return org.rominos2.RealBanks.RealBanks.getInstance().askPermissions(
				player, "realbanks.bank." + bank.getName(), true);
	}
}