/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.rominos2.RealBanks.Banks;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.rominos2.RealBanks.api.Settings.WorldSettings;

public class WorldManager implements
		org.rominos2.RealBanks.api.Banks.WorldManager {
	private Set<org.rominos2.RealBanks.api.Banks.Bank> banks;
	private WorldSettings properties;
	private TimeManager time;

	public WorldManager(WorldSettings settings) {
		this.properties = settings;
		this.banks = new HashSet();
		this.time = new TimeManager(this);
	}

	public void init() {
		if (this.properties.isActive()) {
			loadBanks();
			if (this.properties.isLog())
				org.rominos2.RealBanks.RealBanks
						.getInstance()
						.getLogger()
						.info("Loaded " + this.banks.size() + " banks for "
								+ getWorld().getName());
		}
	}

	public World getWorld() {
		return this.properties.getWorld();
	}

	public WorldSettings getProperties() {
		return this.properties;
	}

	public TimeManager getTimeManager() {
		return this.time;
	}

	public org.rominos2.RealBanks.api.Banks.Bank[] getBanks() {
		return ((org.rominos2.RealBanks.api.Banks.Bank[]) this.banks
				.toArray(new org.rominos2.RealBanks.api.Banks.Bank[0]));
	}

	public org.rominos2.RealBanks.api.Banks.Bank[] getBanks(OfflinePlayer player) {
		List banks = new ArrayList();
		for (org.rominos2.RealBanks.api.Banks.Bank bank : this.banks) {
			if (bank.getAccount(player) != null) {
				banks.add(bank);
			}
		}
		return ((org.rominos2.RealBanks.api.Banks.Bank[]) banks
				.toArray(new org.rominos2.RealBanks.api.Banks.Bank[0]));
	}

	public org.rominos2.RealBanks.api.Banks.Bank getBank(Chest chest) {
		for (org.rominos2.RealBanks.api.Banks.Bank bank : this.banks) {
			if (bank.isConnected(chest)) {
				return bank;
			}
		}
		return null;
	}

	public org.rominos2.RealBanks.api.Banks.Bank getBank(String name) {
		for (org.rominos2.RealBanks.api.Banks.Bank bank : this.banks) {
			if (bank.getName().equalsIgnoreCase(name)) {
				return bank;
			}
		}
		return null;
	}

	public org.rominos2.RealBanks.api.Banks.Bank createBank(String name) {
		org.rominos2.RealBanks.api.Settings.BankSettings properties = new org.rominos2.RealBanks.Settings.BankSettings(
				getWorld(), name);
		((org.rominos2.RealBanks.Settings.BankSettings) properties).load();
		org.rominos2.RealBanks.api.Banks.Bank bank = new Bank(getWorld(),
				properties);
		this.banks.add(bank);
		return bank;
	}

	public org.rominos2.RealBanks.api.Banks.Bank createFrontageBank(
			org.rominos2.RealBanks.api.Banks.Bank from) {
		FrontageBank bank = new FrontageBank((Bank) from, getWorld(),
				new LinkedHashSet());
		this.banks.add(bank);
		return bank;
	}

	public void deleteBank(org.rominos2.RealBanks.api.Banks.Bank bank) {
		if (this.banks.contains(bank)) {
			bank.delete(this.properties.isKeepSave());
			this.banks.remove(bank);
		}
	}

	public void loadBanks() {
		File folder = new File(org.rominos2.RealBanks.RealBanks.getInstance()
				.getPluginFolder(), getWorld().getName() + File.separatorChar);
		if (!(folder.exists())) {
			return;
		}
		File[] list = folder.listFiles();

		for (File file : list) {
			if (file.getName().endsWith(".yml")) {
				org.rominos2.RealBanks.api.Settings.BankSettings properties = new org.rominos2.RealBanks.Settings.BankSettings(
						getWorld(), file.getName().split("\\.")[0]);
				((org.rominos2.RealBanks.Settings.BankSettings) properties)
						.load();
				this.banks.add(new Bank(getWorld(), properties));
			}
		}

		org.rominos2.RealBanks.api.Settings.IO.BankIO frontageLoad = new org.rominos2.RealBanks.Settings.BankSettings(
				getWorld(), "frontage_load").getSave();
		for (org.rominos2.RealBanks.api.Banks.Bank bank : ((org.rominos2.RealBanks.Settings.IO.BankIO) frontageLoad)
				.loadFrontageBanks())
			this.banks.add(bank);
	}

	public void save() {
		new File(org.rominos2.RealBanks.RealBanks.getInstance()
				.getPluginFolder(), getWorld().getName() + File.separatorChar
				+ getWorld().getName() + ".frontages").delete();
		for (org.rominos2.RealBanks.api.Banks.Bank bank : this.banks) {
			bank.closeAllTranactions();
			bank.save();
		}
	}

	public void stop() {
		for (org.rominos2.RealBanks.api.Banks.Bank bank : this.banks)
			if (bank instanceof Bank)
				((Bank) bank).close();
	}
}