/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.rominos2.RealBanks.Settings.IO;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

public class SQLDriver implements Driver {
	private Driver driver;

	public SQLDriver(Driver driver) throws SQLException {
		this.driver = driver;
	}

	public Connection connect(String url, Properties info) throws SQLException {
		return this.driver.connect(url, info);
	}

	public boolean acceptsURL(String url) throws SQLException {
		return this.driver.acceptsURL(url);
	}

	public DriverPropertyInfo[] getPropertyInfo(String url, Properties info)
			throws SQLException {
		return this.driver.getPropertyInfo(url, info);
	}

	public int getMajorVersion() {
		return this.driver.getMajorVersion();
	}

	public int getMinorVersion() {
		return this.driver.getMinorVersion();
	}

	public boolean jdbcCompliant() {
		return this.driver.jdbcCompliant();
	}

	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return this.driver.getParentLogger();
	}
}