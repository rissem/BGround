package com.jukejuice;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

public abstract class DbTask<T> {
	Connection conn;
	String sqliteFilePath;
	private static final Logger log = Logger.getLogger(DbTask.class);
	private boolean closeConnection = true;
	
	private Connection getConnection() {
		try {
			Class.forName("org.sqlite.JDBC");
			Connection conn = DriverManager.getConnection("jdbc:sqlite:prod.db");
			return conn;
		} catch (ClassNotFoundException e) {
			log.error("", e);
		} catch (SQLException e) {
			log.error("", e);
		}
		return null;
	}
	
	public abstract T exec () throws SQLException;
	
	public T run(Connection conn)
	{
		this.conn = conn;
		T result = null;
		try {
			result = exec();
		}
		catch (SQLException e) {
			try {
				if (closeConnection) {
					log.error("", e);
					conn.close();
				}
				return result;
			} catch (SQLException e1) {
				log.error("", e1);
				return result;
			}
		}
		try {
			if (closeConnection)
				conn.close();
			return result;
		} catch (SQLException e) {
			log.error("", e);
			return result;
		}
	}
	
	public T run()
	{
		conn = getConnection();
		return run(conn);
	}
}
