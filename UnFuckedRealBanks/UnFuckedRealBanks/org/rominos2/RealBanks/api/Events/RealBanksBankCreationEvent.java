/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.rominos2.RealBanks.api.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.rominos2.RealBanks.api.Banks.Bank;

public class RealBanksBankCreationEvent extends RealBanksPlayerEvent {
	private static HandlerList handlers = new HandlerList();

	public RealBanksBankCreationEvent(Bank bank, Player player) {
		super(bank, player);
	}

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}