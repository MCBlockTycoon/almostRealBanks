/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.rominos2.RealBanks.Banks;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.server.v1_5_R3.TileEntityChest;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.craftbukkit.v1_5_R3.inventory.CraftInventory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.rominos2.RealBanks.Banks.Transactions.ChestTransaction;
import org.rominos2.RealBanks.Banks.Transactions.VirtualTransaction;
import org.rominos2.RealBanks.api.Banks.Transactions.Transaction;
import org.rominos2.RealBanks.api.Settings.BankSettings;
import org.rominos2.RealBanks.api.Settings.IO.BankIO;

public class Bank implements org.rominos2.RealBanks.api.Banks.Bank {
	private BankSettings properties;
	private World world;
	private Map<Chest, ChestTransaction> chests;
	private List<VirtualTransaction> virtualTransactions;
	private List<org.rominos2.RealBanks.api.Banks.BankAccount> accounts;
	private Map<OfflinePlayer, OfflinePlayer> aliases;

	public Bank(World world, BankSettings properties) {
		this.properties = properties;
		this.world = world;

		this.chests = new HashMap();
		Set chests = this.properties.getSave().loadChests();
		Iterator it = chests.iterator();
		while (it.hasNext()) {
			this.chests.put((Chest) it.next(), null);
		}
		this.virtualTransactions = new ArrayList();

		this.accounts = this.properties.getSave().loadAccounts(this);
		this.aliases = new HashMap();
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

	public String getName() {
		return this.properties.getName();
	}

	public org.rominos2.RealBanks.api.Banks.BankAccount[] getAccounts() {
		return ((org.rominos2.RealBanks.api.Banks.BankAccount[]) this.accounts
				.toArray(new org.rominos2.RealBanks.api.Banks.BankAccount[0]));
	}

	public org.rominos2.RealBanks.api.Banks.BankAccount getAccount(
			OfflinePlayer player) {
		for (org.rominos2.RealBanks.api.Banks.BankAccount account : this.accounts) {
			if (account.getPlayer().getName()
					.equalsIgnoreCase(player.getName())) {
				return account;
			}
		}
		return null;
	}

	public void addAccount(OfflinePlayer player) {
		this.accounts.add(new BankAccount(this, player, new ItemStack[27]));
	}

	public void removeAccount(OfflinePlayer player) {
		org.rominos2.RealBanks.api.Banks.BankAccount account = getAccount(player);
		if (account == null) {
			return;
		}
		this.accounts.remove(account);
	}

	public boolean isFull() {
		return (this.accounts.size() >= this.properties.getAccountsLimit());
	}

	public BankSettings getProperties() {
		return this.properties;
	}

	public World getWorld() {
		return this.world;
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
		Inventory inventory = new CraftInventory(new TileEntityChest());
		VirtualTransaction transac = new VirtualTransaction(seer, inventory,
				account, reason);
		this.virtualTransactions.add(transac);
		BankAccount.fillInventory(transac);
		seer.openInventory(inventory);
		return transac;
	}

	public void closeTransaction(Transaction transac) {
		transac.getViewer().closeInventory();
		transac.getInventory().getViewers().remove(transac.getViewer());

		if (transac instanceof ChestTransaction) {
			closeChestTransaction((ChestTransaction) transac);
		} else {
			closeVirtualTransaction((VirtualTransaction) transac);
		}

		if (this.properties.isLogTransactions()) {
			transac.log();
			this.properties.getSave().log(transac);
		}
		BankAccount.emptyInventory(transac);
	}

	private void closeChestTransaction(ChestTransaction transac) {
		Chest chest = transac.getChest();
		if ((!(this.chests.containsKey(chest)))
				|| (this.chests.get(chest) == null))
			return;
		this.chests.put(chest, null);
	}

	final void closeVirtualTransaction(VirtualTransaction transac) {
		if (this.virtualTransactions.contains(transac))
			this.virtualTransactions.remove(transac);
	}

	public void closeAllTranactions() {
		for (ChestTransaction transac : this.chests.values()) {
			if (transac != null) {
				closeChestTransaction(transac);
			}
		}
		for (VirtualTransaction transac : this.virtualTransactions)
			closeVirtualTransaction(transac);
	}

	public ChestTransaction getTransaction(Chest chest) {
		return ((ChestTransaction) this.chests.get(chest));
	}

	public Transaction getTransaction(Player player) {
		for (ChestTransaction transac : this.chests.values()) {
			if ((transac != null)
					&& (transac.getViewer().getName().equalsIgnoreCase(player
							.getName()))) {
				return transac;
			}
		}

		for (Transaction transac : this.virtualTransactions) {
			if (transac.getViewer().getName()
					.equalsIgnoreCase(player.getName())) {
				return transac;
			}
		}
		return null;
	}

	public void save() {
		this.properties.getSave().save(this.chests.keySet(), this.accounts);
	}

	public void delete(boolean keepSave) {
		this.properties.getFile().delete();
		if (!(keepSave))
			this.properties.getSave().deleteBankSave();
	}

	public OfflinePlayer getAlias(Player player) {
		return ((OfflinePlayer) this.aliases.get(player));
	}

	public void setAlias(Player player, OfflinePlayer alias) {
		if (player.getName().equalsIgnoreCase(alias.getName())) {
			alias = null;
		}
		this.aliases.put(player, alias);
	}

	final void close() {
		closeAllTranactions();
		getProperties().getSave().close();
	}

	final List<VirtualTransaction> getVirtualTransactions() {
		return this.virtualTransactions;
	}

	public boolean isFrontageBank() {
		return false;
	}
}