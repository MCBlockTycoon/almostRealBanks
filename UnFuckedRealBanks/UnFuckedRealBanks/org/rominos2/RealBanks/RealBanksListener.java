/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.rominos2.RealBanks;

import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.rominos2.RealBanks.Settings.LanguageSettings;
import org.rominos2.RealBanks.api.Banks.Bank;
import org.rominos2.RealBanks.api.Banks.BankAccount;
import org.rominos2.RealBanks.api.Banks.Transactions.Transaction;
import org.rominos2.RealBanks.api.Banks.WorldManager;
import org.rominos2.RealBanks.api.Settings.LanguageSettings.SentenceType;
import org.rominos2.RealBanks.api.Settings.WorldSettings;

public class RealBanksListener implements Listener {
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onChestBreak(BlockBreakEvent event) {
		WorldManager manager = RealBanks.getInstance().getManager(
				event.getBlock().getWorld());
		if (event.getBlock().getState() instanceof Chest) {
			Bank bank = manager.getBank((Chest) event.getBlock().getState());
			if (bank != null) {
				event.setCancelled(true);
				event.getPlayer()
						.sendMessage(
								getLang(manager,
										LanguageSettings.SentenceType.BANK,
										"Chest.Break",
										"Please disconnect this Chest from <bank> Bank first",
										new String[] { bank.getName() }));
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onChestOpen(PlayerInteractEvent event) {
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
			return;
		}

		Block target = event.getClickedBlock();
		if ((target == null) || (target.getTypeId() != 54)) {
			return;
		}

		WorldManager manager = RealBanks.getInstance().getManager(
				target.getWorld());
		if (!(manager.getProperties().isActive())) {
			return;
		}

		Chest chest = (Chest) target.getState();
		Bank bank = manager.getBank(chest);
		if (bank == null) {
			return;
		}

		OfflinePlayer client = bank.getAlias(event.getPlayer());
		if (client == null) {
			client = event.getPlayer();
		} else {
			event.getPlayer()
					.sendMessage(
							getLang(manager,
									LanguageSettings.SentenceType.CLIENT,
									"Bank.As.Open",
									"You're opening this chest as if you were <client>.",
									new String[] { client.getName() }));
		}

		BankAccount account = bank.getAccount(client);
		if (account == null) {
			event.setCancelled(true);
			event.getPlayer()
					.sendMessage(
							getLang(manager,
									LanguageSettings.SentenceType.BANK,
									"Chest.Interact",
									"You must have an account in the <bank> Bank to interact with this chest.",
									new String[] { bank.getName() }));
			return;
		}

		Transaction transac = bank.getTransaction(chest);
		if (transac != null) {
			if (transac.getViewer().getName()
					.equalsIgnoreCase(event.getPlayer().getName())) {
				bank.closeTransaction(transac);
			} else {
				event.setCancelled(true);
				event.getPlayer()
						.sendMessage(
								getLang(manager,
										LanguageSettings.SentenceType.NORMAL,
										"Chest.Transaction",
										"This chest is already in a Transaction with another Player. Please wait...",
										null));
				return;
			}
		}

		bank.openChestTransaction(chest, event.getPlayer(), account);
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onInventoryClose(InventoryCloseEvent event) {
		WorldManager manager = RealBanks.getInstance().getManager(
				event.getPlayer().getWorld());
		if (!(manager.getProperties().isActive())) {
			return;
		}

		if (!(event.getPlayer() instanceof Player)) {
			return;
		}

		Block block = event.getPlayer().getTargetBlock(null, 10);
		if ((block != null) && (block.getTypeId() == 54)) {
			Chest chest = (Chest) block.getState();
			Bank bank = manager.getBank(chest);
			if (bank == null) {
				return;
			}

			Transaction transac = bank.getTransaction(chest);
			if (transac != null) {
				bank.closeTransaction(transac);
			}
			return;
		}

		Player player = (Player) event.getPlayer();

		for (Bank bank : manager.getBanks(player)) {
			Transaction transac = bank.getTransaction(player);
			if (transac != null) {
				bank.closeTransaction(transac);
				return;
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerQuit(PlayerQuitEvent event) {
		WorldManager manager = RealBanks.getInstance().getManager(
				event.getPlayer().getWorld());
		if (!(manager.getProperties().isActive())) {
			return;
		}

		for (Bank bank : manager.getBanks()) {
			Transaction transac = bank.getTransaction(event.getPlayer());
			if (transac != null) {
				bank.closeTransaction(transac);
				return;
			}
		}
	}

	private String getLang(WorldManager manager,
			LanguageSettings.SentenceType type, String node, String def,
			String[] args) {
		return manager.getProperties().getColor()
				+ RealBanks.getInstance().getLang(type, node, def, args);
	}
}