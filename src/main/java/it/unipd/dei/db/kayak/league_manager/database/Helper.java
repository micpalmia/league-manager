package it.unipd.dei.db.kayak.league_manager.database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Helper {

//	private static int port = 5555;
//	final static String URL = "jdbc:postgresql://localhost:" + port + "/Kayak";
//	final static String USER = "Kayak";
//	final static String PASSWORD = "aijaevau";
	private static int port = 5555;
	final static String URL = "jdbc:postgresql://localhost/Kayak";
	final static String USER = "denis";
	final static String PASSWORD = "a";

	static int getPort() {
		return port;
	}

	static void setPort(int p) {
		port = p;
	}

	static String readFileAsString(String filePath) throws IOException {
		StringBuffer fileData = new StringBuffer();
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		char[] buf = new char[1024000];
		int numRead = 0;
		while ((numRead = reader.read(buf)) != -1) {
			String readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData + "\n");
		}
		reader.close();
		return fileData.toString();
	}

	public static String getVersion() {
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;

		String ret = "";

		/*
		 * se ottengo questo errore: SEVERE: FATAL: password authentication
		 * failed for user "user12"
		 * 
		 * bisogna impostare la password all'user: sudo -u postgres psql
		 * postgres=# ALTER USER user12 WITH PASSWORD '34klq*';
		 */

		try {
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			st = con.createStatement();
			rs = st.executeQuery("SELECT VERSION()");

			if (rs.next()) {
				ret = rs.getString(1);
			}

		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(DDL.class.getName());
			lgr.log(Level.SEVERE, ex.getMessage(), ex);

		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}
				if (con != null) {
					con.close();
				}

			} catch (SQLException ex) {
				Logger lgr = Logger.getLogger(DDL.class.getName());
				lgr.log(Level.WARNING, ex.getMessage(), ex);
			}
		}
		return ret;
	}

	public static byte[] getHashedString(String text) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		try {
			md.update(text.getBytes("UTF-8"));// Change this to "UTF-16" if
												// needed
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return md.digest();
	}

	public static Connection openConnection() {
		Connection con = null;

		try {
			con = DriverManager.getConnection(Helper.URL, Helper.USER,
					Helper.PASSWORD);
		} catch (SQLException e) {
			Logger lgr = Logger.getLogger(Helper.class.getName());
			lgr.log(Level.SEVERE, e.getMessage(), e);
		}

		return con;
	}

	public static void closeConnection(Connection con) {
		try {
			if (con != null) {
				con.close();
			}

		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(DML.class.getName());
			lgr.log(Level.SEVERE, ex.getMessage(), ex);
		}
	}
}