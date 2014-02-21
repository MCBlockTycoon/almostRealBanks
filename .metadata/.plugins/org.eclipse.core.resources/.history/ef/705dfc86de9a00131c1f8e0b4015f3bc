/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.rominos2.RealBanks.Settings.IO;

import java.sql.Connection;
import org.bukkit.World;

public class PostgreSQL extends BasicSQL {
	private static Connection staticConn = null;

	public PostgreSQL(World world, String bankName, String ip, int port,
			String db, String user, String password) {
		super(world, bankName, ip, port, db, user, password);
	}

	protected Connection getStaticConnection() {
		return staticConn;
	}

	protected void setStaticConnection(Connection con) {
		staticConn = con;
	}

	protected String getLibDriverClassPath() {
		return "org.postgresql.Driver";
	}

	protected String getDriverManagerConnectionURL() {
		return "jdbc:postgresql://" + this.dbIP + ":" + this.dbPort + "/"
				+ this.dbName;
	}
}