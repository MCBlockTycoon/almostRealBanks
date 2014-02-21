/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.rominos2.RealBanks.Banks.Transactions;

import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.rominos2.RealBanks.api.Banks.BankAccount;

public class ChestTransaction extends Transaction {
	private Chest chest;

	public ChestTransaction(Player seer, Chest chest, BankAccount account) {
		super(seer, account);
		this.chest = chest;
	}

	public Chest getChest() {
		return this.chest;
	}

	public Inventory getInventory() {
		return this.chest.getInventory();
	}

	public String getLogInformations() {
		return "Chest : " + this.chest.getX() + "," + this.chest.getY() + ","
				+ this.chest.getZ();
	}
}