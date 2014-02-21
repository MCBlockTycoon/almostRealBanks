/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.rominos2.RealBanks.Banks.Transactions;

import org.bukkit.Material;
import org.rominos2.RealBanks.api.Banks.Transactions.TransactionLogEntry.LogEntryType;

public class TransactionLogEntry implements
		org.rominos2.RealBanks.api.Banks.Transactions.TransactionLogEntry {
	private TransactionLogEntry.LogEntryType type;
	private Material material;
	private int amount;

	public TransactionLogEntry(Material mat, int amount) {
		this.material = mat;
		if (amount > 0)
			this.type = TransactionLogEntry.LogEntryType.ADD;
		else
			this.type = TransactionLogEntry.LogEntryType.REMOVE;
		this.amount = Math.abs(amount);
	}

	public TransactionLogEntry.LogEntryType getType() {
		return this.type;
	}

	public Material getMaterial() {
		return this.material;
	}

	public int getAmount() {
		return this.amount;
	}
}