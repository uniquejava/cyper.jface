package hello.layout.aqua.util;

import static hello.layout.aqua.dialog.LogonDialog.*;
import hello.layout.aqua.Bootstrap;
import hello.layout.aqua.dialog.connect.ConnectionInfo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DbUtil {
	public static Connection getConnection() throws Exception {
		if (currentConnectionName != null) {
			Class.forName("com.ibm.db2.jcc.DB2Driver");
			ConnectionInfo info = Bootstrap.getInstance()
					.getConnectionInfoMap().get(currentConnectionName);
			String url = info.toDB2String();
			return DriverManager.getConnection(url, currentUsername,
					currentPassword);
		}
		return null;
	}

	public static void main(String[] args) {

		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = DbUtil.getConnection();
			stmt = conn.createStatement();
			rs = stmt
					.executeQuery("select count(*) from DBEFMSVR.FMSV1_O_POOL_DEPT");
			rs.next();
			System.out.println("number=" + rs.getInt(1));

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbUtil.close(rs, stmt, conn);
		}

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
