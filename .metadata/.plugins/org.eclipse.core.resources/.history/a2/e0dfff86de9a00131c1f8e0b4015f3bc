/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.rominos2.RealBanks.api.Banks.Transactions;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.rominos2.RealBanks.api.Banks.Bank;
import org.rominos2.RealBanks.api.Banks.BankAccount;

public abstract interface Transaction {
	public abstract BankAccount getAccount();

	public abstract Bank getBank();

	public abstract Player getViewer();

	public abstract Inventory getInventory();

	public abstract void log();

	public abstract TransactionLogEntry[] getLogEntries();

	public abstract String getLogInformations();
}