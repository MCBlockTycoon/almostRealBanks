/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.rominos2.RealBanks.Settings.IO;

import java.sql.Connection;
import org.bukkit.World;

public class MySQL extends BasicSQL {
	private static Connection staticConn = null;

	public MySQL(World world, String bankName, String ip, int port, String db,
			String user, String password) {
		super(world, bankName, ip, port, db, user, password);
	}

	protected Connection getStaticConnection() {
		return staticConn;
	}

	protected void setStaticConnection(Connection con) {
		staticConn = con;
	}

	protected String getLibDriverClassPath() {
		return "com.mysql.jdbc.Driver";
	}

	protected String getDriverManagerConnectionURL() {
		return "jdbc:mysql://" + this.dbIP + ":" + this.dbPort + "/"
				+ this.dbName;
	}
}