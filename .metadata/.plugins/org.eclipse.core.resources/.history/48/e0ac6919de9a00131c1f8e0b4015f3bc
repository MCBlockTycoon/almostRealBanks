/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.rominos2.RealBanks.api.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.rominos2.RealBanks.api.Banks.Bank;

public class RealBanksBankModificationEvent extends RealBanksPlayerEvent {
	private static HandlerList handlers = new HandlerList();
	private BankModificationType type;
	private String oldValue;
	private String newValue;

	public RealBanksBankModificationEvent(Bank bank, Player player,
			BankModificationType type, String oldValue, String newValue) {
		super(bank, player);
		this.type = type;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	public BankModificationType getType() {
		return this.type;
	}

	public String getOldValue() {
		return this.oldValue;
	}

	public String getNewValue() {
		return this.newValue;
	}

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public static enum BankModificationType {
		ACCOUNT_LIMIT, ENTER_PERMISSION, LOG_TRANSACTION, ENTER_COST;
	}
}