/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.rominos2.RealBanks.api.Events;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.rominos2.RealBanks.api.Banks.Bank;

public class RealBanksPlayerUnsubscribeEvent extends RealBanksPlayerEvent {
	private static HandlerList handlers = new HandlerList();
	private OfflinePlayer client;

	public RealBanksPlayerUnsubscribeEvent(Bank bank, Player player,
			OfflinePlayer client) {
		super(bank, player);
		this.client = client;
	}

	public OfflinePlayer getClient() {
		return this.client;
	}

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}