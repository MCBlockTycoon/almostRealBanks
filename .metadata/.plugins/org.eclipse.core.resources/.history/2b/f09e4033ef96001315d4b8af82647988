/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.rominos2.RealBanks.api.Events;

import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.rominos2.RealBanks.api.Banks.Bank;

public abstract class RealBanksChestPlayerEvent extends RealBanksPlayerEvent {
	private Chest chest;

	public RealBanksChestPlayerEvent(Bank bank, Player player, Chest chest) {
		super(bank, player);
		this.chest = chest;
	}

	public Chest getChest() {
		return this.chest;
	}
}