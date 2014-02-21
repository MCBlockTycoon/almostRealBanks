/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.rominos2.RealBanks.api.Events;

import org.bukkit.event.HandlerList;
import org.rominos2.RealBanks.api.Banks.Bank;
import org.rominos2.RealBanks.api.Banks.Transactions.Transaction;

public class RealBanksTransactionCloseEvent extends RealBanksEvent {
	private static HandlerList handlers = new HandlerList();
	private Transaction transaction;

	public RealBanksTransactionCloseEvent(Bank bank, Transaction transaction) {
		super(bank);
		this.transaction = transaction;
	}

	public Transaction getTransaction() {
		return this.transaction;
	}

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}