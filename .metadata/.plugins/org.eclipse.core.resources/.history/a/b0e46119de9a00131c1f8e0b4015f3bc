/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.rominos2.RealBanks.Settings.IO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.logging.Logger;
import org.bukkit.World;

public abstract class BasicSQL extends SQL {
	protected String dbIP;
	protected int dbPort;
	protected String dbName;
	private String user;
	private String password;

	public BasicSQL(World world, String bankName, String ip, int port,
			String db, String user, String password) {
		super(world, bankName);
		this.dbIP = ip;
		this.dbPort = port;
		this.dbName = db;
		this.user = user;
		this.password = password;
	}

	protected abstract Connection getStaticConnection();

	protected abstract void setStaticConnection(Connection paramConnection);

	protected abstract String getLibDriverClassPath();

	protected abstract String getDriverManagerConnectionURL();

	protected Connection connect() throws Exception {
		if ((getStaticConnection() != null)
				&& (!(getStaticConnection().isClosed()))) {
			return getStaticConnection();
		}
		try {
			SQL.loadDriver(getLibDriverClassPath());

			Connection con = DriverManager.getConnection(
					getDriverManagerConnectionURL(), this.user, this.password);
			setStaticConnection(con);
			return con;
		} catch (Exception e) {
			org.rominos2.RealBanks.RealBanks.getInstance().getLogger()
					.severe("ERROR during BankIO loading : " + e.getMessage());
		}
		return null;
	}
}