/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.rominos2.RealBanks.Settings;

import java.io.File;
import java.io.IOException;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.rominos2.RealBanks.Settings.IO.IOSettings;
import org.rominos2.RealBanks.api.Settings.IO.BankIO;

public class BankSettings implements
		org.rominos2.RealBanks.api.Settings.BankSettings {
	private String name;
	private File file;
	private BankIO save;
	private int acountsLimit;
	private String enterPermission;
	private boolean logTransaction;
	private double enterCost;

	public BankSettings(World world, String name) {
		this.name = name;
		this.file = new File(org.rominos2.RealBanks.RealBanks.getInstance()
				.getPluginFolder(), world.getName() + File.separatorChar + name
				+ ".yml");
		this.save = ((org.rominos2.RealBanks.RealBanks) org.rominos2.RealBanks.RealBanks
				.getInstance()).getIOSettings().getBankIO(world, name);
	}

	public String getName() {
		return this.name;
	}

	public File getFile() {
		return this.file;
	}

	public BankIO getSave() {
		return this.save;
	}

	public void load() {
		if (!(this.file.exists())) {
			try {
				this.file.getParentFile().mkdirs();
				this.file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			createSettings();
		}

		loadSettings();
	}

	private void createSettings() {
		YamlConfiguration config = YamlConfiguration
				.loadConfiguration(this.file);

		config.set("Properties.AccountsLimit", Integer.valueOf(10));
		config.set("Properties.EnterPermissionNode", "");
		config.set("Properties.LogTransactions", Boolean.valueOf(false));
		config.set("Eco.EnterCost", Double.valueOf(0.0D));
		try {
			config.save(this.file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void loadSettings() {
		YamlConfiguration config = YamlConfiguration
				.loadConfiguration(this.file);

		this.acountsLimit = config.getInt("Properties.AccountsLimit", 10);
		this.enterPermission = config.getString(
				"Properties.EnterPermissionNode", "");
		this.logTransaction = config.getBoolean("Properties.LogTransactions",
				false);
		this.enterCost = config.getDouble("Eco.EnterCost", 0.0D);
	}

	public int getAccountsLimit() {
		return this.acountsLimit;
	}

	public void setAccountLimit(int value) {
		value = Math.abs(value);
		this.acountsLimit = value;
		changeSettings("Properties.AccountsLimit",
				Integer.valueOf(this.acountsLimit));
	}

	public String getEnterPermission() {
		return this.enterPermission;
	}

	public void setEnterPermission(String value) {
		this.enterPermission = value;
		changeSettings("Properties.EnterPermissionNode", this.enterPermission);
	}

	public boolean isLogTransactions() {
		return this.logTransaction;
	}

	public void setLogTransactions(boolean value) {
		this.logTransaction = value;
		changeSettings("Properties.LogTransactions",
				Boolean.valueOf(this.logTransaction));
	}

	public double getEnterCost() {
		return this.enterCost;
	}

	public void setEnterCost(double cost) {
		cost = Math.abs(cost);
		this.enterCost = cost;
		changeSettings("Eco.EnterCost", Double.valueOf(cost));
	}

	private void changeSettings(String node, Object value) {
		YamlConfiguration config = YamlConfiguration
				.loadConfiguration(this.file);
		config.set(node, value);
		saveConfigInFile(config, this.file);
	}

	private void saveConfigInFile(YamlConfiguration config, File file) {
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}