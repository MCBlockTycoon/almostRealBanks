/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.rominos2.RealBanks.api.Settings;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.rominos2.RealBanks.api.Banks.WorldManager;

public abstract interface WorldSettings {
	public abstract World getWorld();

	public abstract WorldManager getWorldManager();

	public abstract boolean isActive();

	public abstract boolean isLog();

	public abstract boolean isKeepSave();

	public abstract int getLinesPerPage();

	public abstract ChatColor getColor();

	public abstract int getScheduledSaveTime();

	public abstract double getCreateCost();

	public abstract double getConnectCost();
}