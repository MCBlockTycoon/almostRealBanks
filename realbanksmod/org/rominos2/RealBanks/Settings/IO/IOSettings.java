/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.rominos2.RealBanks.Settings.IO;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;

public class IOSettings {
	private File file;
	private IOType type;
	private String sqlHost;
	private int sqlPort;
	private String sqlName;
	private String sqlUser;
	private String sqlPass;

	public IOSettings() {
		this.file = new File(org.rominos2.RealBanks.RealBanks.getInstance()
				.getPluginFolder(), "BankIO.yml");
	}

	public void load() {
		if (!(this.file.exists())) {
			try {
				this.file.createNewFile();
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
		config.set("BankIO.Choice", "FlatFile");
		String[] types = new String[IOType.values().length];
		for (int i = 0; i < types.length; ++i) {
			types[i] = IOType.values()[i].name();
		}
		config.set("BankIO.ChoiceList", types);
		config.set("SQL.Server.Host", "127.0.0.1");
		config.set("SQL.Server.Port", Integer.valueOf(3306));
		config.set("SQL.DatabaseName", "myDatabase");
		config.set("SQL.Access.User", "myUser");
		config.set("SQL.Access.Password", "myPassword");
		try {
			config.save(this.file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void loadSettings() {
		YamlConfiguration config = YamlConfiguration
				.loadConfiguration(this.file);

		this.type = IOType.valueOf(config
				.getString("BankIO.Choice", "FlatFile"));

		if (this.type != IOType.FlatFile) {
			this.sqlHost = config.getString("SQL.Server.Host", "127.0.0.1");
			this.sqlPort = config.getInt("SQL.Server.Port", 3306);
			this.sqlName = config.getString("SQL.DatabaseName", "myDatabase");
			this.sqlUser = config.getString("SQL.Access.User", "myUser");
			this.sqlPass = config
					.getString("SQL.Access.Password", "myPassword");
		}
	}

	public BankIO getBankIO(World world, String bankName) {
		BankIO io = null;
		try {
			switch ($SWITCH_TABLE$org$rominos2$RealBanks$Settings$IO$IOSettings$IOType()[this.type
					.ordinal()]) {
			case 1:
				io = new FlatFile(world, bankName);
				break;
			case 2:
				io = new MySQL(world, bankName, this.sqlHost, this.sqlPort,
						this.sqlName, this.sqlUser, this.sqlPass);
				break;
			case 3:
				io = new PostgreSQL(world, bankName, this.sqlHost,
						this.sqlPort, this.sqlName, this.sqlUser, this.sqlPass);
				break;
			case 4:
				io = new SQLite(world, bankName, null, 0, this.sqlName,
						this.sqlUser, this.sqlPass);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if ((io == null) || (!(io.checkState()))) {
			org.rominos2.RealBanks.RealBanks
					.getInstance()
					.getLogger()
					.info("Can't load from that BankIO, defaulting to FlatFile.");
			this.type = IOType.FlatFile;
			io = new FlatFile(world, bankName);
		}
		return io;
	}

	public IOType getIOType() {
		return this.type;
	}

	public static enum IOType {
		FlatFile, MySQL, PostgreSQL, SQLite;
	}
}