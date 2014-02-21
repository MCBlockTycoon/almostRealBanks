/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.rominos2.RealBanks.Banks.Transactions;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.rominos2.RealBanks.api.Banks.BankAccount;

public class VirtualTransaction extends Transaction {
	private Inventory inventory;
	private String reason;

	public VirtualTransaction(Player seer, Inventory inventory,
			BankAccount account, String reason) {
		super(seer, account);
		this.inventory = inventory;
		this.reason = reason;
	}

	public Inventory getInventory() {
		return this.inventory;
	}

	public String getReason() {
		return this.reason;
	}

	public String getLogInformations() {
		return this.reason;
	}
}