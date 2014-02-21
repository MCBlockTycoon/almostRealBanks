/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.rominos2.RealBanks.Banks.Transactions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.rominos2.RealBanks.api.Banks.Bank;
import org.rominos2.RealBanks.api.Banks.BankAccount;

public abstract class Transaction implements
		org.rominos2.RealBanks.api.Banks.Transactions.Transaction {
	private BankAccount account;
	private Player viewer;
	private List<org.rominos2.RealBanks.api.Banks.Transactions.TransactionLogEntry> entries;

	public Transaction(Player viewer, BankAccount account) {
		this.account = account;
		this.viewer = viewer;
		this.entries = new ArrayList();
	}

	public BankAccount getAccount() {
		return this.account;
	}

	public Bank getBank() {
		return this.account.getBank();
	}

	public Player getViewer() {
		return this.viewer;
	}

	public abstract Inventory getInventory();

	public void log() {
		this.entries = getModifications(this.account.getContentClone(),
				(ItemStack[]) getInventory().getContents().clone());
	}

	public org.rominos2.RealBanks.api.Banks.Transactions.TransactionLogEntry[] getLogEntries() {
		return ((org.rominos2.RealBanks.api.Banks.Transactions.TransactionLogEntry[]) this.entries
				.toArray(new TransactionLogEntry[0]));
	}

	public abstract String getLogInformations();

	public static List<org.rominos2.RealBanks.api.Banks.Transactions.TransactionLogEntry> getModifications(
			ItemStack[] before, ItemStack[] after) {
		Map modifs = new HashMap();

		for (ItemStack stack : after) {
			if (stack != null) {
				int number = stack.getAmount();
				if (modifs.containsKey(stack.getType())) {
					number += ((Integer) modifs.get(stack.getType()))
							.intValue();
				}
				modifs.put(stack.getType(), Integer.valueOf(number));
			}
		}

		for (ItemStack stack : before) {
			if (stack != null) {
				int number = -stack.getAmount();
				if (modifs.containsKey(stack.getType())) {
					number += ((Integer) modifs.get(stack.getType()))
							.intValue();
				}
				modifs.put(stack.getType(), Integer.valueOf(number));
			}
		}

		List entries = new ArrayList();
		for (Map.Entry entry : modifs.entrySet()) {
			if (((Integer) entry.getValue()).intValue() != 0) {
				entries.add(new TransactionLogEntry((Material) entry.getKey(),
						((Integer) entry.getValue()).intValue()));
			}
		}
		return entries;
	}
}