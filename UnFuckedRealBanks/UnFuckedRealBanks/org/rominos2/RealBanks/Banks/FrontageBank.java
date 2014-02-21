/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.rominos2.RealBanks.Banks;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.rominos2.RealBanks.Banks.Transactions.ChestTransaction;
import org.rominos2.RealBanks.Banks.Transactions.VirtualTransaction;
import org.rominos2.RealBanks.api.Banks.Transactions.Transaction;
import org.rominos2.RealBanks.api.Settings.BankSettings;
import org.rominos2.RealBanks.api.Settings.IO.BankIO;

public class FrontageBank implements org.rominos2.RealBanks.api.Banks.Bank {
	private Bank bank;
	private World world;
	private Map<Chest, ChestTransaction> chests;

	public FrontageBank(Bank bank, World world, Set<Chest> chests) {
		this.bank = bank;
		this.world = world;
		this.chests = new HashMap();
		Iterator it = chests.iterator();
		while (it.hasNext())
			this.chests.put((Chest) it.next(), null);
	}

	public String getName() {
		return this.bank.getName();
	}

	public Chest[] getChests() {
		return ((Chest[]) this.chests.keySet().toArray(new Chest[0]));
	}

	public void addChest(Chest chest) {
		if (!(this.chests.containsKey(chest)))
			this.chests.put(chest, null);
	}

	public void removeChest(Chest chest) {
		if (this.chests.containsKey(chest)) {
			if (this.chests.get(chest) != null) {
				closeChestTransaction((ChestTransaction) this.chests.get(chest));
			}
			this.chests.remove(chest);
		}
	}

	public boolean isConnected(Chest chest) {
		return this.chests.containsKey(chest);
	}

	public org.rominos2.RealBanks.api.Banks.BankAccount[] getAccounts() {
		return this.bank.getAccounts();
	}

	public org.rominos2.RealBanks.api.Banks.BankAccount getAccount(
			OfflinePlayer player) {
		return this.bank.getAccount(player);
	}

	public void addAccount(OfflinePlayer player) {
		this.bank.addAccount(player);
	}

	public void removeAccount(OfflinePlayer player) {
		this.bank.removeAccount(player);
	}

	public boolean isFull() {
		return this.bank.isFull();
	}

	public OfflinePlayer getAlias(Player player) {
		return this.bank.getAlias(player);
	}

	public void setAlias(Player player, OfflinePlayer alias) {
		this.bank.setAlias(player, alias);
	}

	public Transaction openChestTransaction(Chest chest, Player player,
			org.rominos2.RealBanks.api.Banks.BankAccount account) {
		Transaction tr = getTransaction(chest);
		if (tr != null) {
			closeChestTransaction((ChestTransaction) tr);
		}
		tr = getTransaction(player);
		if (tr != null) {
			closeTransaction(tr);
		}

		ChestTransaction transac = new ChestTransaction(player, chest, account);
		this.chests.put(chest, transac);
		BankAccount.fillInventory(transac);
		return transac;
	}

	public Transaction openVirtualTransaction(Player seer,
			org.rominos2.RealBanks.api.Banks.BankAccount account, String reason) {
		return this.bank.openVirtualTransaction(seer, account, reason);
	}

	public void closeTransaction(Transaction transac) {
		transac.getViewer().closeInventory();
		transac.getInventory().getViewers().remove(transac.getViewer());
		if (transac instanceof ChestTransaction) {
			closeChestTransaction((ChestTransaction) transac);
		} else
			this.bank.closeVirtualTransaction((VirtualTransaction) transac);
	}

	private void closeChestTransaction(ChestTransaction transac) {
		Chest chest = transac.getChest();
		if ((this.chests.containsKey(chest))
				&& (this.chests.get(chest) != null)) {
			this.chests.put(chest, null);
		}

		if (getProperties().isLogTransactions()) {
			transac.log();
		}
		BankAccount.emptyInventory(transac);
	}

	public void closeAllTranactions() {
		for (ChestTransaction transac : this.chests.values()) {
			if (transac != null) {
				closeChestTransaction(transac);
			}
		}
		for (VirtualTransaction transac : this.bank.getVirtualTransactions())
			this.bank.closeVirtualTransaction(transac);
	}

	public Transaction getTransaction(Chest chest) {
		return ((Transaction) this.chests.get(chest));
	}

	public Transaction getTransaction(Player player) {
		for (ChestTransaction transac : this.chests.values()) {
			if ((transac != null)
					&& (transac.getViewer().getName().equalsIgnoreCase(player
							.getName()))) {
				return transac;
			}
		}

		for (Transaction transac : this.bank.getVirtualTransactions()) {
			if (transac.getViewer().getName()
					.equalsIgnoreCase(player.getName())) {
				return transac;
			}
		}
		return null;
	}

	public BankSettings getProperties() {
		return this.bank.getProperties();
	}

	public World getWorld() {
		return this.world;
	}

	public void save() {
		getProperties().getSave()
				.saveFrontage(this.world, this.chests.keySet());
	}

	public void delete(boolean keepSave) {
	}

	public boolean isFrontageBank() {
		return true;
	}
}