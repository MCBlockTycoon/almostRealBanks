/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.rominos2.RealBanks.Settings.IO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLSyntaxErrorException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.rominos2.RealBanks.Banks.FrontageBank;
import org.rominos2.RealBanks.api.Banks.Transactions.Transaction;
import org.rominos2.RealBanks.api.Banks.Transactions.TransactionLogEntry;
import org.rominos2.RealBanks.api.Banks.Transactions.TransactionLogEntry.LogEntryType;
import org.rominos2.RealBanks.api.Banks.WorldManager;

public abstract class SQL extends BankIO {
	protected Connection connection;
	protected Statement statement;

	protected SQL(World world, String bankName) {
		super(world, bankName);
		this.connection = null;
	}

	public Set<Chest> loadChests() {
		Set chests = new LinkedHashSet();
		try {
			checkConnection();
		} catch (Exception e1) {
			e1.printStackTrace();
			org.rominos2.RealBanks.RealBanks.getInstance().getLogger()
					.info("Can't load chests.");
			return chests;
		}

		try {
			ResultSet result = this.statement
					.executeQuery("SELECT x, y, z FROM realbanks_"
							+ getWorld().getName() + "_chests WHERE bank='"
							+ getSQLBankName() + "'");
			while (result.next()) {
				Chest chest = getChestFromLocation(result.getInt("x"),
						result.getInt("y"), result.getInt("z"));
				if (chest != null)
					chests.add(chest);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return chests;
	}

	public List<org.rominos2.RealBanks.api.Banks.BankAccount> loadAccounts(
			org.rominos2.RealBanks.api.Banks.Bank instance) {
		List accounts = new ArrayList();
		try {
			checkConnection();
		} catch (Exception e1) {
			e1.printStackTrace();
			org.rominos2.RealBanks.RealBanks.getInstance().getLogger()
					.info("Can't load accounts.");
			return accounts;
		}

		ItemStack[] content = new ItemStack[27];
		try {
			ResultSet result = this.statement
					.executeQuery("SELECT * FROM realbanks_"
							+ getWorld().getName() + "_accounts WHERE bank='"
							+ getSQLBankName() + "'");
			while (result.next()) {
				OfflinePlayer player = org.rominos2.RealBanks.RealBanks
						.getInstance().getServer()
						.getOfflinePlayer(result.getString("player"));
				if (player != null) {
					for (int i = 0; i < 27; ++i) {
						content[i] = getItemFromString(result
								.getString("c" + i));
					}
					accounts.add(new org.rominos2.RealBanks.Banks.BankAccount(
							instance, player, content));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return accounts;
	}

	public void save(Set<Chest> chests,
			List<org.rominos2.RealBanks.api.Banks.BankAccount> accounts) {
		try {
			checkConnection();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		saveChests(chests);
		saveAccounts(accounts);
	}

	private void saveChests(Set<Chest> chests) {
		for (Chest chest : chests)
			try {
				this.statement.executeUpdate("INSERT INTO realbanks_"
						+ getWorld().getName() + "_chests VALUES ('"
						+ getSQLBankName() + "', " + chest.getX() + ", "
						+ chest.getY() + ", " + chest.getZ() + ")");
			} catch (SQLIntegrityConstraintViolationException localSQLIntegrityConstraintViolationException) {
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}

	private void saveAccounts(List<org.rominos2.RealBanks.api.Banks.BankAccount> accounts)
  {
    label493: for (org.rominos2.RealBanks.api.Banks.BankAccount account : accounts)
      try {
        ItemStack[] toSave = account.getContentClone();
        String contentString = "";
        if (this.statement.executeQuery("SELECT player FROM realbanks_" + getWorld().getName() + "_accounts WHERE bank='" + getSQLBankName() + "' AND player='" + account.getPlayer().getName() + "'").next()) {
          for (int i = 0; i < toSave.length - 1; ++i) {
            contentString = contentString + "c" + i + "='" + getStringFromItem(toSave[i]) + "',";
          }
          contentString = contentString + "c26='" + getStringFromItem(toSave[(toSave.length - 1)]) + "'";
          this.statement.executeUpdate("UPDATE realbanks_" + getWorld().getName() + "_accounts SET " + contentString + " WHERE bank='" + getSQLBankName() + "' AND player='" + account.getPlayer().getName() + "'");
         break label493; //ccc had a : here instead of ; 
        }
        for (int i = 0; i < toSave.length - 1; ++i) {
          contentString = contentString + "'" + getStringFromItem(toSave[i]) + "',";
        }
        contentString = contentString + "'" + getStringFromItem(toSave[(toSave.length - 1)]) + "'";
        this.statement.executeUpdate("INSERT INTO realbanks_" + getWorld().getName() + "_accounts VALUES ('" + getSQLBankName() + "','" + account.getPlayer().getName() + "'," + contentString + ")");
      }
      catch (SQLException e) {
        e.printStackTrace();
      }
  }

	public void deleteBankSave() {
		try {
			checkConnection();
			this.statement.executeUpdate("DELETE FROM realbanks_"
					+ getWorld().getName() + "_chests WHERE bank='"
					+ getSQLBankName() + "'");
			this.statement.executeUpdate("DELETE FROM realbanks_"
					+ getWorld().getName() + "_accounts WHERE bank='"
					+ getSQLBankName() + "'");
			this.statement.executeUpdate("DELETE FROM realbanks_"
					+ getWorld().getName() + "_frontages WHERE bank='"
					+ getSQLBankName() + "'");
			this.statement.executeUpdate("DELETE FROM realbanks_"
					+ getWorld().getName() + "_logs WHERE bank='"
					+ getSQLBankName() + "'");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void saveFrontage(World world, Set<Chest> chests) {
		try {
			checkConnection();
			this.statement.executeUpdate("INSERT INTO realbanks_"
					+ world.getName() + "_frontages VALUES ('"
					+ getSQLBankName() + "','" + getWorld().getName() + "')");

			saveChests(chests);
		} catch (SQLIntegrityConstraintViolationException localSQLIntegrityConstraintViolationException) {
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Set<org.rominos2.RealBanks.api.Banks.Bank> loadFrontageBanks() {
		Set banks = new LinkedHashSet();
		try {
			checkConnection();

			ResultSet result = this.statement
					.executeQuery("SELECT * FROM realbanks_"
							+ getWorld().getName() + "_frontages");

			while (result.next()) {
				World world = org.rominos2.RealBanks.RealBanks.getInstance()
						.getServer().getWorld(result.getString("world"));
				if (world == null) {
					org.rominos2.RealBanks.RealBanks
							.getInstance()
							.getLogger()
							.info("Can't load Frontage Bank : Can't find World : "
									+ result.getString("world"));
				} else {
					org.rominos2.RealBanks.api.Banks.Bank bank = org.rominos2.RealBanks.RealBanks
							.getInstance().getManager(world)
							.getBank(result.getString("bank"));
					if (bank == null) {
						org.rominos2.RealBanks.RealBanks
								.getInstance()
								.getLogger()
								.info("Can't load Frontage Bank : Can't find Bank : "
										+ result.getString("bank")
										+ " in "
										+ world.getName());
					} else {
						banks.add(new FrontageBank(
								(org.rominos2.RealBanks.Banks.Bank) bank,
								world, loadFrontageChests(bank.getName())));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return banks;
	}

	private Set<Chest> loadFrontageChests(String bankName) {
		Set chests = new LinkedHashSet();
		try {
			ResultSet result = this.statement
					.executeQuery("SELECT x,y,z FROM realbanks_"
							+ getWorld().getName() + "_chests WHERE bank='"
							+ bankName + "'");
			while (result.next()) {
				Block block = getWorld().getBlockAt(result.getInt("x"),
						result.getInt("y"), result.getInt("z"));
				if ((block != null) && (block.getState() instanceof Chest))
					chests.add((Chest) block.getState());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return chests;
	}

	public void close() {
		try {
			if ((this.connection == null) || (this.connection.isClosed()))
				return;
			this.connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void checkConnection() throws Exception {
		boolean firstTime = this.connection == null;

		if ((this.connection == null) || (this.connection.isClosed())) {
			this.connection = connect();
			this.statement = this.connection.createStatement();
		}

		if ((!(firstTime)) || (isTablesExists()))
			return;
		org.rominos2.RealBanks.RealBanks
				.getInstance()
				.getLogger()
				.info("Can't find tables in database for "
						+ getWorld().getName() + ", creating them.");
		createTables();
	}

	private void createTables() throws SQLException {
		try {
			this.statement
					.executeUpdate("CREATE TABLE realbanks_"
							+ getWorld().getName()
							+ "_chests (bank varchar(50), x int, y int, z int, primary key(bank, x, y, z))");
		} catch (SQLSyntaxErrorException localSQLSyntaxErrorException) {
		}
		String contentCreation = "";
		for (int i = 0; i < 26; ++i) {
			contentCreation = contentCreation + "c" + i + " varchar(50),";
		}
		contentCreation = contentCreation + "c26 varchar(50)";
		try {
			this.statement.executeUpdate("CREATE TABLE realbanks_"
					+ getWorld().getName()
					+ "_accounts (bank varchar(50), player varchar(50), "
					+ contentCreation + ", primary key(bank, player))");
		} catch (SQLSyntaxErrorException localSQLSyntaxErrorException1) {
		}
		try {
			this.statement
					.executeUpdate("CREATE TABLE realbanks_"
							+ getWorld().getName()
							+ "_frontages (bank varchar(50), world varchar(50), primary key (bank, world))");
		} catch (SQLSyntaxErrorException localSQLSyntaxErrorException2) {
		}
		try {
			this.statement
					.executeUpdate("CREATE TABLE realbanks_"
							+ getWorld().getName()
							+ "_logs (jour char(10), heure char(8), bank varchar(50), viewer varchar(50), account varchar(50), operation varchar(10), amount int, material varchar(50), reason varchar(100))");
		} catch (SQLSyntaxErrorException localSQLSyntaxErrorException3) {
		}
	}

	private String getStringFromItem(ItemStack stack) {
		if (stack == null) {
			return "";
		}
		String result = stack.getAmount() + ":" + stack.getTypeId() + ":"
				+ stack.getDurability() + "|";
		for (Map.Entry entry : stack.getEnchantments().entrySet()) {
			result = result + ((Enchantment) entry.getKey()).getId() + "/"
					+ entry.getValue() + ":";
		}
		return result;
	}

	private ItemStack getItemFromString(String string) {
		if (string.isEmpty()) {
			return null;
		}

		String[] split = string.split("\\|")[0].split("\\:");
		ItemStack stack = new ItemStack(Integer.valueOf(split[1]).intValue(),
				Integer.valueOf(split[0]).intValue(), Short.valueOf(split[2])
						.shortValue());

		split = string.split("\\|");
		if (split.length >= 2) {
			split = string.split("\\|")[1].split("\\:");

			for (String ench : split) {
				String[] enchSplit = ench.split("\\/");
				Enchantment e = Enchantment.getById(Integer.valueOf(
						enchSplit[0]).intValue());
				stack.addEnchantment(e, Integer.valueOf(enchSplit[1])
						.intValue());
			}
		}

		return stack;
	}

	private boolean isTablesExists() {
		try {
			checkConnection();
			this.statement.executeQuery("SELECT * FROM realbanks_"
					+ getWorld().getName()
					+ "_chests WHERE bank='IamADummyBankTralala'");
			this.statement.executeQuery("SELECT * FROM realbanks_"
					+ getWorld().getName()
					+ "_accounts WHERE bank='IamADummyBankTralala'");
			this.statement.executeQuery("SELECT * FROM realbanks_"
					+ getWorld().getName()
					+ "_frontages WHERE bank='IamADummyBankTralala'");
			this.statement.executeQuery("SELECT * FROM realbanks_"
					+ getWorld().getName()
					+ "_logs WHERE bank='IamADummyBankTralala'");
			return true;
		} catch (Exception e) {
		}
		return false;
	}

	private String getSQLBankName() {
		return getBankName().replace("'", "''");
	}

	public void log(Transaction transaction) {
		if (transaction.getLogEntries().length == 0) {
			return;
		}

		String jour = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		String heure = new SimpleDateFormat("HH:mm:ss").format(new Date());
		try {
			checkConnection();
			for (TransactionLogEntry entry : transaction.getLogEntries())
				this.statement.executeUpdate("INSERT INTO realbanks_"
						+ getWorld().getName() + "_logs VALUES ('" + jour
						+ "','" + heure + "','" + getBankName() + "','"
						+ transaction.getViewer().getName() + "','"
						+ transaction.getAccount().getPlayer().getName()
						+ "','" + entry.getType().name() + "','"
						+ entry.getAmount() + "','"
						+ entry.getMaterial().name() + "','"
						+ transaction.getLogInformations() + "')");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected abstract Connection connect() throws Exception;

	protected static void loadDriver(String classPath)
			throws ClassNotFoundException {
		Class.forName(classPath);
	}

	public boolean checkState() {
		try {
			return (connect() != null);
		} catch (Exception e) {
		}
		return false;
	}
}