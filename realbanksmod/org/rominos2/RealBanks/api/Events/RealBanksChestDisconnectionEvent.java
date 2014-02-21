/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.rominos2.RealBanks.api.Events;

import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.rominos2.RealBanks.api.Banks.Bank;

public class RealBanksChestDisconnectionEvent extends RealBanksChestPlayerEvent {
	private static HandlerList handlers = new HandlerList();

	public RealBanksChestDisconnectionEvent(Bank bank, Player player,
			Chest chest) {
		super(bank, player, chest);
	}

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}