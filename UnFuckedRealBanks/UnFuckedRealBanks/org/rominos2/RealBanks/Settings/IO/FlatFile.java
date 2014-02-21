/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.rominos2.RealBanks.Settings.IO;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.rominos2.RealBanks.Banks.FrontageBank;
import org.rominos2.RealBanks.api.Banks.Transactions.Transaction;
import org.rominos2.RealBanks.api.Banks.Transactions.TransactionLogEntry;
import org.rominos2.RealBanks.api.Banks.Transactions.TransactionLogEntry.LogEntryType;
import org.rominos2.RealBanks.api.Banks.WorldManager;
import org.rominos2.RealBanks.api.Settings.WorldSettings;

public class FlatFile extends BankIO {
	private File save;

	public FlatFile(World world, String bankName) {
		super(world, bankName);
		this.save = new File(org.rominos2.RealBanks.RealBanks.getInstance()
				.getPluginFolder(), getWorld().getName() + File.separatorChar
				+ "Saves" + File.separatorChar + getBankName() + ".save");
	}

	public Set<Chest> loadChests() {
		Set chests = new HashSet();
		if (!(this.save.exists())) {
			return chests;
		}

		YamlConfiguration config = YamlConfiguration
				.loadConfiguration(this.save);

		ArrayList objList = (ArrayList) config.getList("Chests",
				new ArrayList());

		for (Iterator localIterator = objList.iterator(); localIterator
				.hasNext();) {
			Object obj = localIterator.next();
			if (obj instanceof String) {
				String[] coords = ((String) obj).split("\\,");
				Chest chest = getChestFromLocation(Integer.valueOf(coords[0])
						.intValue(), Integer.valueOf(coords[1]).intValue(),
						Integer.valueOf(coords[2]).intValue());
				if (chest != null) {
					chests.add(chest);
				} else if (org.rominos2.RealBanks.RealBanks.getInstance()
						.getManager(getWorld()).getProperties().isLog()) {
					org.rominos2.RealBanks.RealBanks
							.getInstance()
							.getLogger()
							.info("[WARNING] Error on loading chest in "
									+ getWorld().getName() + " at "
									+ Integer.valueOf(coords[0]) + ","
									+ Integer.valueOf(coords[1]) + ","
									+ Integer.valueOf(coords[2]));
				}
			}
		}
		return chests;
	}

	public List<org.rominos2.RealBanks.api.Banks.BankAccount> loadAccounts(
			org.rominos2.RealBanks.api.Banks.Bank instance) {
		List list = loadAccountsNewWay(instance);
		if (list == null) {
			list = loadAccountsOldWay(instance);
		}
		return list;
	}

	private List<org.rominos2.RealBanks.api.Banks.BankAccount> loadAccountsNewWay(
			org.rominos2.RealBanks.api.Banks.Bank instance) {
		ArrayList accounts = new ArrayList();
		if (!(this.save.exists())) {
			return accounts;
		}

		YamlConfiguration config = YamlConfiguration
				.loadConfiguration(this.save);
		ConfigurationSection section = config
				.getConfigurationSection("Accounts");
		if (section == null) {
			return accounts;
		}

		Set set = section.getKeys(false);
		if (set == null) {
			return accounts;
		}

		Iterator it = set.iterator();
		while (it.hasNext()) {
			String playerName = (String) it.next();
			OfflinePlayer player = org.rominos2.RealBanks.RealBanks
					.getInstance().getServer().getOfflinePlayer(playerName);
			if (player == null) {
				continue;
			}

			List itemList = config.getMapList("Accounts." + playerName);
			if ((itemList == null) || (itemList.isEmpty())) {
				return null;
			}

			ItemStack[] content = new ItemStack[27];
			for (int i = 0; (i < itemList.size()) && (i < 27); ++i) {
				if ((itemList.get(i) == null)
						|| (((Map) itemList.get(i)).isEmpty())) {
					content[i] = null;
				} else {
					content[i] = ItemStack.deserialize((Map) itemList.get(i));
				}
			}
			accounts.add(new org.rominos2.RealBanks.Banks.BankAccount(instance,
					player, content));
		}
		return accounts;
	}

	private List<org.rominos2.RealBanks.api.Banks.BankAccount> loadAccountsOldWay(
			org.rominos2.RealBanks.api.Banks.Bank instance) {
		ArrayList accounts = new ArrayList();
		if (!(this.save.exists())) {
			return accounts;
		}

		YamlConfiguration config = YamlConfiguration
				.loadConfiguration(this.save);
		ConfigurationSection section = config
				.getConfigurationSection("Accounts");
		if (section == null) {
			return accounts;
		}

		Set set = section.getKeys(false);
		if (set == null) {
			return accounts;
		}

		Iterator it = set.iterator();
		while (it.hasNext()) {
			String playerName = (String) it.next();
			OfflinePlayer player = org.rominos2.RealBanks.RealBanks
					.getInstance().getServer().getOfflinePlayer(playerName);
			if (player == null) {
				continue;
			}
			Object[] objTab = ((ArrayList) config.getList("Accounts."
					+ playerName, new ArrayList())).toArray();

			ItemStack[] content = new ItemStack[27];
			for (int i = 0; (i < objTab.length) && (i < 27); ++i) {
				if (objTab[i] instanceof String) {
					String line = (String) objTab[i];
					ItemStack stack;
//ccc					ItemStack stack;
					if (line.equalsIgnoreCase("empty")) {
						stack = null;
					} else {
						stack = new ItemStack(Material.valueOf(line
								.split("\\:")[0]), Integer.valueOf(
								line.split("\\:")[2]).intValue());
						stack.setDurability(Short.valueOf(line.split("\\:")[1])
								.shortValue());
					}
					content[i] = stack;
				} else {
					content[i] = null;
				}
			}
			accounts.add(new org.rominos2.RealBanks.Banks.BankAccount(instance,
					player, content));
		}
		return accounts;
	}

	public void save(Set<Chest> chests,
			List<org.rominos2.RealBanks.api.Banks.BankAccount> accounts) {
		if (this.save.exists())
			this.save.delete();
		try {
			this.save.getParentFile().mkdirs();
			this.save.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

		YamlConfiguration save = YamlConfiguration.loadConfiguration(this.save);
		saveChests(save, chests);
		saveConfigInFile(save, this.save);
		saveAccounts(save, accounts);
	}

	private void saveChests(YamlConfiguration save, Set<Chest> chests) {
		String[] list = new String[chests.size()];

		Iterator it = chests.iterator();
		int i = 0;
		while (it.hasNext()) {
			Block chest = ((Chest) it.next()).getBlock();
			list[i] = chest.getX() + "," + chest.getY() + "," + chest.getZ();
			++i;
		}
		save.set("Chests", list);
	}

	private void saveAccounts(YamlConfiguration save,
			List<org.rominos2.RealBanks.api.Banks.BankAccount> accounts) {
		for (org.rominos2.RealBanks.api.Banks.BankAccount account : accounts) {
			save.set("Accounts." + account.getPlayer().getName(),
					((org.rominos2.RealBanks.Banks.BankAccount) account)
							.getStringContent());
			saveConfigInFile(save, this.save);
		}
	}

	private void saveConfigInFile(YamlConfiguration config, File file) {
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void deleteBankSave() {
		this.save.delete();
	}

	public void close() {
	}

	public void saveFrontage(World world, Set<Chest> chests) {
		File file = new File(org.rominos2.RealBanks.RealBanks.getInstance()
				.getPluginFolder(), world.getName() + File.separatorChar
				+ world.getName() + ".frontages");
		file.getParentFile().mkdirs();

		YamlConfiguration save = YamlConfiguration.loadConfiguration(file);
		save.set(getBankName() + ".World", getWorld().getName());
		List list = new ArrayList();
		for (Chest chest : chests) {
			list.add(chest.getX() + "," + chest.getY() + "," + chest.getZ());
		}
		save.set(getBankName() + ".Chests", list);
		try {
			save.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Set<org.rominos2.RealBanks.api.Banks.Bank> loadFrontageBanks() {
		Set banks = new LinkedHashSet();

		YamlConfiguration config = YamlConfiguration
				.loadConfiguration(new File(org.rominos2.RealBanks.RealBanks
						.getInstance().getPluginFolder(), getWorld().getName()
						+ ".frontages"));

		for (String name : config.getKeys(true)) {
			String w = config.getString(name + ".World");
			if (w == null) {
				continue;
			}
			World world = org.rominos2.RealBanks.RealBanks.getInstance()
					.getServer().getWorld(w);
			if (world == null) {
				org.rominos2.RealBanks.RealBanks
						.getInstance()
						.getLogger()
						.info("Can't load Frontage Bank : Can't find World : "
								+ w);
			} else {
				org.rominos2.RealBanks.api.Banks.Bank bank = org.rominos2.RealBanks.RealBanks
						.getInstance().getManager(world).getBank(name);
				if (bank == null) {
					org.rominos2.RealBanks.RealBanks
							.getInstance()
							.getLogger()
							.info("Can't load Frontage Bank : Can't find Bank : "
									+ name + " in " + world.getName());
				} else {
					banks.add(new FrontageBank(
							(org.rominos2.RealBanks.Banks.Bank) bank,
							getWorld(), loadFrontageChests(config, name)));
				}
			}
		}
		return banks;
	}

	private Set<Chest> loadFrontageChests(YamlConfiguration config, String name) {
		Set chests = new LinkedHashSet();

		ArrayList objList = (ArrayList) config.getList(name + ".Chests",
				new ArrayList());

		for (Iterator localIterator = objList.iterator(); localIterator
				.hasNext();) {
			Object obj = localIterator.next();
			if (obj instanceof String) {
				String[] coords = ((String) obj).split("\\,");
				Block block = getWorld().getBlockAt(
						Integer.valueOf(coords[0]).intValue(),
						Integer.valueOf(coords[1]).intValue(),
						Integer.valueOf(coords[2]).intValue());
				if ((block != null) && (block.getState() instanceof Chest)) {
					chests.add((Chest) block.getState());
				} else if (org.rominos2.RealBanks.RealBanks.getInstance()
						.getManager(getWorld()).getProperties().isLog()) {
					org.rominos2.RealBanks.RealBanks
							.getInstance()
							.getLogger()
							.info("Error on loading chest in "
									+ getWorld().getName() + " at "
									+ Integer.valueOf(coords[0]) + ","
									+ Integer.valueOf(coords[1]) + ","
									+ Integer.valueOf(coords[2]));
				}
			}
		}
		return chests;
	}

	public void log(Transaction transaction) {
		if (transaction.getLogEntries().length == 0) {
			return;
		}
		List lines = new ArrayList();
		lines.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				.format(new Date())
				+ " - "
				+ transaction.getViewer().getName()
				+ " - "
				+ transaction.getAccount().getPlayer().getName()
				+ "'s account - " + transaction.getLogInformations());
		lines.addAll(getModificationsLines(transaction.getLogEntries()));
		lines.add("------------------------------------------");

		File file = new File(org.rominos2.RealBanks.RealBanks.getInstance()
				.getPluginFolder(), getWorld().getName() + File.separatorChar
				+ "Logs" + File.separatorChar + getBankName() + ".log");
		file.getParentFile().mkdirs();
		try {
			if (!(file.exists())) {
				file.createNewFile();
			}

			FileWriter writer = new FileWriter(file, true);
			for (String toWrite : lines) {
				writer.write(toWrite + "\n");
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private List<String> getModificationsLines(TransactionLogEntry[] entries) {
		List adds = new ArrayList();
		List removes = new ArrayList();
		for (TransactionLogEntry entry : entries) {
			if (entry.getType() == TransactionLogEntry.LogEntryType.ADD) {
				adds.add("ADD    " + entry.getAmount() + " "
						+ entry.getMaterial().name());
			}
			if (entry.getType() == TransactionLogEntry.LogEntryType.REMOVE) {
				removes.add("REMOVE " + entry.getAmount() + " "
						+ entry.getMaterial().name());
			}
		}
		List output = new ArrayList();
		output.addAll(adds);
		output.addAll(removes);
		return output;
	}

	public boolean checkState() {
		return true;
	}
}