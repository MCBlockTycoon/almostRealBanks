/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.rominos2.RealBanks.Settings;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;

public class WorldSettings implements
		org.rominos2.RealBanks.api.Settings.WorldSettings {
	private World world;
	private File file;
	private org.rominos2.RealBanks.api.Banks.WorldManager manager;
	private boolean active;
	private boolean log;
	private ChatColor color;
	private boolean keepSave;
	private int linesPerPage;
	private int scheduledSaveTime;
	private double createCost;
	private double connectCost;
	private double linkCost;

	public WorldSettings(World world) {
		this.world = world;
		this.file = new File(org.rominos2.RealBanks.RealBanks.getInstance()
				.getPluginFolder(), this.world.getName() + ".yml");
		this.manager = null;
	}

	public void load() {
		if (!(this.file.exists())) {
			try {
				this.file.createNewFile();
				org.rominos2.RealBanks.RealBanks
						.getInstance()
						.getLogger()
						.info("Properties not found for "
								+ this.world.getName() + ", created new ones");
				createSettings();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		loadSettings();
	}

	private void createSettings() {
		YamlConfiguration config = YamlConfiguration
				.loadConfiguration(this.file);

		config.set("Active", Boolean.valueOf(true));
		config.set("Log", Boolean.valueOf(true));
		config.set("KeepSavesFileOnDelete", Boolean.valueOf(false));
		config.set("LinesPerPages", Integer.valueOf(5));
		config.set("Color", Character.valueOf(ChatColor.AQUA.getChar()));
		config.set("ScheduleSaveTime", Integer.valueOf(-1));
		config.set("Eco.CreateCost", Double.valueOf(0.0D));
		config.set("Eco.ConnectCost", Double.valueOf(0.0D));
		config.set("Eco.LinkCost", Double.valueOf(0.0D));
		for (int i = 0; i < ChatColor.values().length; ++i) {
			config.set("ColorList." + ChatColor.values()[i].name(),
					Character.valueOf(ChatColor.values()[i].getChar()));
		}
		try {
			config.save(this.file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void loadSettings() {
		YamlConfiguration config = YamlConfiguration
				.loadConfiguration(this.file);

		this.active = config.getBoolean("Active", true);
		this.log = config.getBoolean("Log", true);
		this.keepSave = config.getBoolean("KeepSavesFileOnDelete", false);
		this.linesPerPage = config.getInt("LinesPerPages", 5);
		this.color = ChatColor.getByChar(config.getString("Color", "b"));
		if (this.color == null) {
			this.color = ChatColor.AQUA;
		}
		this.scheduledSaveTime = config.getInt("ScheduleSaveTime", -1);
		this.createCost = config.getDouble("Eco.CreateCost", 0.0D);
		this.connectCost = config.getDouble("Eco.ConnectCost", 0.0D);
		this.linkCost = config.getDouble("Eco.LinkCost", 0.0D);
	}

	public World getWorld() {
		return this.world;
	}

	public org.rominos2.RealBanks.api.Banks.WorldManager getWorldManager() {
		if (this.manager == null) {
			this.manager = new org.rominos2.RealBanks.Banks.WorldManager(this);
		}
		return this.manager;
	}

	public boolean isActive() {
		return this.active;
	}

	public boolean isLog() {
		return this.log;
	}

	public boolean isKeepSave() {
		return this.keepSave;
	}

	public int getLinesPerPage() {
		return this.linesPerPage;
	}

	public ChatColor getColor() {
		return this.color;
	}

	public int getScheduledSaveTime() {
		return this.scheduledSaveTime;
	}

	public double getCreateCost() {
		return this.createCost;
	}

	public double getConnectCost() {
		return this.connectCost;
	}

	public double getLinkCost() {
		return this.linkCost;
	}
}