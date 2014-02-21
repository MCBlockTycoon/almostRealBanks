/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.rominos2.RealBanks.Banks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.rominos2.RealBanks.api.Banks.Bank;
import org.rominos2.RealBanks.api.Banks.Transactions.Transaction;
import org.rominos2.RealBanks.api.Events.RealBanksEvent;
import org.rominos2.RealBanks.api.Events.RealBanksTransactionCloseEvent;
import org.rominos2.RealBanks.api.Events.RealBanksTransactionOpenEvent;

public class BankAccount implements
		org.rominos2.RealBanks.api.Banks.BankAccount {
	private Bank bank;
	private OfflinePlayer player;
	private ItemStack[] content;

	public BankAccount(Bank bank, OfflinePlayer player, ItemStack[] content) {
		this.bank = bank;
		this.player = player;
		this.content = content;
	}

	public OfflinePlayer getPlayer() {
		return this.player;
	}

	public Bank getBank() {
		return this.bank;
	}

	static final void fillInventory(Transaction transac) {
		transac.getInventory().setContents(
				(ItemStack[]) ((BankAccount) transac.getAccount()).content
						.clone());
		RealBanksEvent event = new RealBanksTransactionOpenEvent(
				transac.getBank(), transac);
		org.rominos2.RealBanks.RealBanks.getInstance().getServer()
				.getPluginManager().callEvent(event);
	}

	static final void emptyInventory(Transaction transac) {
		((BankAccount) transac.getAccount()).content = ((ItemStack[]) transac
				.getInventory().getContents().clone());
		transac.getInventory().clear();
		RealBanksEvent event = new RealBanksTransactionCloseEvent(
				transac.getBank(), transac);
		org.rominos2.RealBanks.RealBanks.getInstance().getServer()
				.getPluginManager().callEvent(event);
	}

	public ItemStack[] getContentClone() {
		ItemStack[] clones = new ItemStack[this.content.length];
		for (int i = 0; i < clones.length; ++i) {
			if (this.content[i] != null) {
				clones[i] = new ItemStack(this.content[i]);
			}
		}
		return clones;
	}

	public List<Map<String, Object>> getStringContent() {
		ArrayList list = new ArrayList();
		for (int i = 0; i < this.content.length; ++i) {
			if (this.content[i] != null) {
				list.add(this.content[i].serialize());
			} else {
				list.add(new HashMap());
			}
		}
		return list;
	}

	public boolean addItem(ItemStack item, Player adder, String reason) {
		int place = 0;
		for (int i = 0; (i < this.content.length) && (place < item.getAmount()); ++i) {
			if (this.content[i] == null) {
				place += item.getMaxStackSize();
			} else if ((this.content[i].getTypeId() == item.getTypeId())
					&& (this.content[i].getDurability() == item.getDurability())) {
				place += item.getMaxStackSize() - this.content[i].getAmount();
			}
		}
		if (place < item.getAmount()) {
			return false;
		}

		Transaction transac = this.bank.openVirtualTransaction(adder, this,
				reason);

		int numberToSend = 0;
		int stillToSend = item.getAmount();
		for (int i = 0; (i < this.content.length) && (stillToSend > 0); ++i) {
			if (this.content[i] == null) {
				numberToSend = Math.min(stillToSend, item.getMaxStackSize());
				this.content[i] = new ItemStack(item.getTypeId(), numberToSend,
						item.getDurability());
				stillToSend -= numberToSend;
			} else if ((this.content[i].getTypeId() == item.getTypeId())
					&& (this.content[i].getDurability() == item.getDurability())) {
				numberToSend = Math.min(stillToSend, item.getMaxStackSize()
						- this.content[i].getAmount());
				this.content[i].setAmount(this.content[i].getAmount()
						+ numberToSend);
				stillToSend -= numberToSend;
			}
		}

		this.bank.closeTransaction(transac);
		return true;
	}

	public boolean removeItem(ItemStack item, Player getter, String reason) {
		int number = 0;
		for (int i = 0; (i < this.content.length)
				&& (number < item.getAmount()); ++i) {
			if ((this.content[i] != null)
					&& (this.content[i].getTypeId() == item.getTypeId())
					&& (this.content[i].getDurability() == item.getDurability())) {
				number += this.content[i].getAmount();
			}
		}
		if (number < item.getAmount()) {
			return false;
		}

		Transaction transac = this.bank.openVirtualTransaction(getter, this,
				reason);

		int numberToRemove = 0;
		int stillToGet = item.getAmount();
		for (int i = 0; (i < this.content.length) && (stillToGet > 0); ++i) {
			if ((this.content[i] != null)
					&& (this.content[i].getTypeId() == item.getTypeId())
					&& (this.content[i].getDurability() == item.getDurability())) {
				numberToRemove = Math.min(stillToGet,
						this.content[i].getAmount());
				if (numberToRemove == stillToGet) {
					this.content[i] = null;
				} else {
					this.content[i].setAmount(this.content[i].getAmount()
							- numberToRemove);
				}
				stillToGet -= numberToRemove;
			}
		}

		this.bank.closeTransaction(transac);
		return true;
	}
}