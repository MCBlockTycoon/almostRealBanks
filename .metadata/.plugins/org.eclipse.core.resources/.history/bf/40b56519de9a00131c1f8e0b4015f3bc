/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.rominos2.RealBanks.api.Banks;

import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.rominos2.RealBanks.api.Banks.Transactions.Transaction;
import org.rominos2.RealBanks.api.Settings.BankSettings;

public abstract interface Bank {
	public abstract String getName();

	public abstract Chest[] getChests();

	public abstract boolean isConnected(Chest paramChest);

	public abstract void addChest(Chest paramChest);

	public abstract void removeChest(Chest paramChest);

	public abstract BankAccount[] getAccounts();

	public abstract BankAccount getAccount(OfflinePlayer paramOfflinePlayer);

	public abstract void addAccount(OfflinePlayer paramOfflinePlayer);

	public abstract void removeAccount(OfflinePlayer paramOfflinePlayer);

	public abstract boolean isFull();

	public abstract OfflinePlayer getAlias(Player paramPlayer);

	public abstract void setAlias(Player paramPlayer,
			OfflinePlayer paramOfflinePlayer);

	public abstract Transaction openChestTransaction(Chest paramChest,
			Player paramPlayer, BankAccount paramBankAccount);

	public abstract Transaction openVirtualTransaction(Player paramPlayer,
			BankAccount paramBankAccount, String paramString);

	public abstract void closeTransaction(Transaction paramTransaction);

	public abstract void closeAllTranactions();

	public abstract Transaction getTransaction(Chest paramChest);

	public abstract Transaction getTransaction(Player paramPlayer);

	public abstract BankSettings getProperties();

	public abstract World getWorld();

	public abstract void save();

	public abstract void delete(boolean paramBoolean);

	public abstract boolean isFrontageBank();
}