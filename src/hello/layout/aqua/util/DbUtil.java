package hello.layout.aqua.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class DbUtil {
	public static Connection getConnection() {
		try {
			Class.forName("com.ibm.db2.jcc.DB2Driver");

			return DriverManager
					.getConnection(
							"jdbc:db2://localhost:50000/SAMPLE:retrieveMessagesFromServerOnGetMessage=true;currentSchema=CYPER.YIN;",
							"db2admin", "db2admin");


		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void close(ResultSet rs, Statement stmt, Connection conn) {
		if (rs != null) {
			try {
				rs.close();
			} catch (Exception e) {
				// TODO: handle exception
			}
			rs = null;
		}
		if (stmt != null) {
			try {
				stmt.close();
			} catch (Exception e) {
				// TODO: handle exception
			}
			stmt = null;
		}
		if (conn != null) {
			try {
				conn.close();
			} catch (Exception e) {
				// TODO: handle exception
			}
			conn = null;
		}
	}
}
