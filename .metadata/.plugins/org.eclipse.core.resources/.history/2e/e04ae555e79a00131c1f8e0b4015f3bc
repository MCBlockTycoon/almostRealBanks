/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.rominos2.RealBanks.api;

import java.io.File;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.rominos2.RealBanks.api.Banks.WorldManager;
import org.rominos2.RealBanks.api.Settings.LanguageSettings.SentenceType;

public abstract interface RealBanks extends Plugin {
	public abstract File getPluginFolder();

	public abstract WorldManager[] getManagers();

	public abstract WorldManager getManager(World paramWorld);

	public abstract boolean askPermissions(Player paramPlayer,
			String paramString, boolean paramBoolean);

	public abstract boolean askPermissions(Player paramPlayer,
			String paramString1, String paramString2, boolean paramBoolean);

	public abstract boolean isUsingEconomy();

	public abstract Economy getEconomy();

	public abstract String getLang(
			LanguageSettings.SentenceType paramSentenceType,
			String paramString1, String paramString2,
			String[] paramArrayOfString);
}