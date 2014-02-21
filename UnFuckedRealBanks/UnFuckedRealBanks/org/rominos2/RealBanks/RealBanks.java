/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.rominos2.RealBanks;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.rominos2.RealBanks.Banks.TimeManager;
import org.rominos2.RealBanks.Commands.RealBanksCommands;
import org.rominos2.RealBanks.Settings.IO.IOSettings;
import org.rominos2.RealBanks.Settings.IO.IOSettings.IOType;
import org.rominos2.RealBanks.api.Banks.Bank;
import org.rominos2.RealBanks.api.Settings.LanguageSettings.SentenceType;

public class RealBanks extends JavaPlugin implements Runnable,
		org.rominos2.RealBanks.api.RealBanks {
	private static RealBanks instance;
	private static boolean firstLoad = true;
	private File mainDir;
	private List<org.rominos2.RealBanks.api.Banks.WorldManager> managers;
	private RealBanksListener listener;
	private org.rominos2.RealBanks.api.Settings.LanguageSettings lang;
	private IOSettings ioSettings;
	private Permission pVault;
	private Economy eVault;

	public void onEnable() {
		instance = this;
		this.mainDir = new File("plugins" + File.separatorChar + "RealBanks");
		this.managers = new ArrayList();
		this.listener = new RealBanksListener();
		this.pVault = null;
		this.eVault = null;

		this.mainDir.mkdirs();

		this.ioSettings = new IOSettings();
		this.ioSettings.load();
		getLogger().info(
				"Save Type Selected : " + this.ioSettings.getIOType().name());

		this.managers = new ArrayList();
		for (World world : getServer().getWorlds()) {
			loadWorld(world);
		}

		if (getServer().getPluginManager().isPluginEnabled("Vault")) {
			setupVault();
		}
		setupMetrics();

		this.lang = new org.rominos2.RealBanks.Settings.LanguageSettings();
		((org.rominos2.RealBanks.Settings.LanguageSettings) this.lang).load();

		if (firstLoad) {
			getServer().getPluginManager().registerEvents(this.listener, this);
		}
		getServer().getScheduler()
				.scheduleSyncRepeatingTask(this, this, 0L, 1L);
		getLogger().info(
				"RealBanks Plugin, by Rominos2, version "
						+ getDescription().getVersion() + ", is enabled.");
		firstLoad = false;
	}

	public void onDisable() {
		getServer().getScheduler().cancelTasks(this);

		for (org.rominos2.RealBanks.api.Banks.WorldManager manager : this.managers) {
			manager.save();
			((org.rominos2.RealBanks.Banks.WorldManager) manager).stop();
		}
		getLogger().info(
				"RealBanks Plugin, by Rominos2, version "
						+ getDescription().getVersion() + ", is disabled.");
	}

	private void setupVault() {
		RegisteredServiceProvider permissionProvider = getServer()
				.getServicesManager().getRegistration(Permission.class);
		if (permissionProvider != null) {
			this.pVault = ((Permission) permissionProvider.getProvider());
			getLogger().info(
					"Hooking into Permissions : " + this.pVault.getName());
		}
		RegisteredServiceProvider economyProvider = getServer()
				.getServicesManager().getRegistration(Economy.class);
		if (economyProvider != null) {
			this.eVault = ((Economy) economyProvider.getProvider());
			getLogger().info("Hooking into Economy : " + this.eVault.getName());
		}
	}

	private void setupMetrics() {
		try {
			Metrics metrics = new Metrics(this);
			Metrics.Graph numbers = metrics.createGraph("RealBanks Numbers");
			numbers.addPlotter(new Metrics.Plotter("Number of Banks") {
				public int getValue() {
					int number = 0;
					for (org.rominos2.RealBanks.api.Banks.WorldManager manager : RealBanks
							.getInstance().getManagers()) {
						number += manager.getBanks().length;
					}
					return number;
				}
			});
			numbers.addPlotter(new Metrics.Plotter("Number of Accounts") {
				public int getValue() {
					int number = 0;
					for (org.rominos2.RealBanks.api.Banks.WorldManager manager : RealBanks
							.getInstance().getManagers()) {
						for (Bank bank : manager.getBanks()) {
							number += bank.getAccounts().length;
						}
					}
					return number;
				}
			});
			numbers.addPlotter(new Metrics.Plotter("Number of Chests") {
				public int getValue() {
					int number = 0;
					for (org.rominos2.RealBanks.api.Banks.WorldManager manager : RealBanks
							.getInstance().getManagers()) {
						for (Bank bank : manager.getBanks()) {
							number += bank.getChests().length;
						}
					}
					return number;
				}
			});
			Metrics.Graph io = metrics.createGraph("Save Types");
			io.addPlotter(new Metrics.Plotter(this.ioSettings.getIOType()
					.name()) {
				public int getValue() {
					return 1;
				}
			});
			if (metrics.start())
				getLogger()
						.info("Starting sending anonymous data to Metrics Servers each 10 minutes. To disable it, just set opt-out to true in PluginMetrics/config.yml");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean onCommand(CommandSender sender, Command command,
			String cmdLabel, String[] args) {
		if (sender instanceof Player) {
			if ((cmdLabel.equalsIgnoreCase("realbanks"))
					|| (cmdLabel.equalsIgnoreCase("rb"))) {
				if (!(processCommand((Player) sender, args))) {
					sender.sendMessage(ChatColor.RED
							+ getInstance().getLang(
									LanguageSettings.SentenceType.NORMAL,
									"Command.Invalid",
									"Invalid Command. Try /rb for help.", null));
				}
				return true;
			}
			return false;
		}
		sender.sendMessage("You must be in-game to use the RealBanks Commands.");
		return true;
	}

	public boolean processCommand(Player player, String[] args) {
		return RealBanksCommands.onCommand(player, args);
	}

	public static org.rominos2.RealBanks.api.RealBanks getInstance() {
		return instance;
	}

	public File getPluginFolder() {
		return this.mainDir;
	}

	public org.rominos2.RealBanks.api.Banks.WorldManager[] getManagers() {
		return ((org.rominos2.RealBanks.api.Banks.WorldManager[]) this.managers
				.toArray(new org.rominos2.RealBanks.api.Banks.WorldManager[0]));
	}

	public org.rominos2.RealBanks.api.Banks.WorldManager getManager(World world) {
		for (int i = 0; i < this.managers.size(); ++i) {
			if (world
					.getName()
					.equalsIgnoreCase(
							((org.rominos2.RealBanks.api.Banks.WorldManager) this.managers
									.get(i)).getWorld().getName())) {
				return ((org.rominos2.RealBanks.api.Banks.WorldManager) this.managers
						.get(i));
			}
		}
		loadWorld(world);
		return getManager(world);
	}

	private void loadWorld(World world) {
		org.rominos2.RealBanks.api.Settings.WorldSettings properties = new org.rominos2.RealBanks.Settings.WorldSettings(
				world);
		((org.rominos2.RealBanks.Settings.WorldSettings) properties).load();
		if (properties.isLog()) {
			getLogger().info(
					"Properties Loaded for " + world.getName() + ", active : "
							+ properties.isActive());
		}
		this.managers.add(properties.getWorldManager());
		((org.rominos2.RealBanks.Banks.WorldManager) properties
				.getWorldManager()).init();
	}

	public boolean askPermissions(Player player, String right, boolean message) {
		if ((player.isOp()) || (player.hasPermission(right))) {
			return true;
		}
		if ((this.pVault != null) && (this.pVault.playerHas(player, right))) {
			return true;
		}
		if (message) {
			player.sendMessage(ChatColor.RED
					+ getLang(LanguageSettings.SentenceType.NORMAL,
							"Error.Permissions",
							"You've not the Permission to do that.", null));
		}
		return false;
	}

	public boolean askPermissions(Player player, String right, String bank,
			boolean message) {
		if ((askPermissions(player, right, false))
				|| (askPermissions(player, "realbanks.bank." + bank, false))) {
			return true;
		}
		if (message) {
			player.sendMessage(ChatColor.RED
					+ getLang(LanguageSettings.SentenceType.NORMAL,
							"Error.Permissions",
							"You've not the Permission to do that.", null));
		}
		return false;
	}

	public boolean isUsingEconomy() {
		return (this.eVault != null);
	}

	public Economy getEconomy() {
		return this.eVault;
	}

	public String getLang(LanguageSettings.SentenceType type, String node,
			String def, String[] args) {
		return this.lang.getLang(type, node, def, args);
	}

	public void run() {
		for (org.rominos2.RealBanks.api.Banks.WorldManager manager : this.managers)
			((org.rominos2.RealBanks.Banks.WorldManager) manager)
					.getTimeManager().onTick();
	}

	public IOSettings getIOSettings() {
		return this.ioSettings;
	}
}