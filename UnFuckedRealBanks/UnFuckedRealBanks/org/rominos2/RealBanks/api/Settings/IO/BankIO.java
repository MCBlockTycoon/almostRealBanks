/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.rominos2.RealBanks.api.Settings.IO;

import java.util.List;
import java.util.Set;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.rominos2.RealBanks.api.Banks.Bank;
import org.rominos2.RealBanks.api.Banks.BankAccount;
import org.rominos2.RealBanks.api.Banks.Transactions.Transaction;

public abstract interface BankIO {
	public abstract Set<Chest> loadChests();

	public abstract List<BankAccount> loadAccounts(Bank paramBank);

	public abstract void save(Set<Chest> paramSet, List<BankAccount> paramList);

	public abstract void deleteBankSave();

	public abstract void saveFrontage(World paramWorld, Set<Chest> paramSet);

	public abstract Set<Bank> loadFrontageBanks();

	public abstract void log(Transaction paramTransaction);

	public abstract void close();
}