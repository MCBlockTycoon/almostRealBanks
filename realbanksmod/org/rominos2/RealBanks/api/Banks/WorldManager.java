/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.rominos2.RealBanks.api.Banks;

import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.rominos2.RealBanks.api.Settings.WorldSettings;

public abstract interface WorldManager {
	public abstract World getWorld();

	public abstract WorldSettings getProperties();

	public abstract Bank[] getBanks();

	public abstract Bank[] getBanks(OfflinePlayer paramOfflinePlayer);

	public abstract Bank getBank(Chest paramChest);

	public abstract Bank getBank(String paramString);

	public abstract Bank createBank(String paramString);

	public abstract Bank createFrontageBank(Bank paramBank);

	public abstract void deleteBank(Bank paramBank);

	public abstract void save();
}