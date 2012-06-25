package hello.example.ktable.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

public class MyDao {
	public List<LinkedHashMap<String, Object>> query(String tableName) {

		List<LinkedHashMap<String, Object>> result = new ArrayList<LinkedHashMap<String, Object>>();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			Class.forName("com.ibm.db2.jcc.DB2Driver");
			conn = DriverManager
					.getConnection(
							"jdbc:db2://localhost:50000/SAMPLE:retrieveMessagesFromServerOnGetMessage=true;currentSchema=CYPER.YIN;",
							"db2admin", "db2admin");
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select * from " + tableName);

			ResultSetMetaData meta = rs.getMetaData();
			int count = meta.getColumnCount();
			String[] tableHeaders = new String[count];
			
			LinkedHashMap<String, Object> row = new LinkedHashMap<String, Object>();
			for (int i = 0; i < count; i++) {
				tableHeaders[i] = meta.getColumnName(i+1);
				//the first row is column name info
				row.put(String.valueOf(i), tableHeaders[i]);
			}
			result.add(row);

			while (rs.next()) {
				row = new LinkedHashMap<String, Object>();
				for (int i = 0; i < tableHeaders.length; i++) {
					row.put(tableHeaders[i], rs.getString(tableHeaders[i]));
				}
				result.add(row);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
			} catch (Exception e) {
			}
			try {
				stmt.close();
			} catch (Exception e) {
			}
			try {
				conn.close();
			} catch (Exception e) {
			}
		}
		return result;
	}

	public static void main(String[] args) {
		MyDao dao = new MyDao();
		List<LinkedHashMap<String, Object>> list = dao.query("EMPLOYEE");
		for (LinkedHashMap<String, Object> row : list) {
			for (Iterator it = row.keySet().iterator(); it.hasNext();) {
				String key = (String) it.next();
				System.out.println(key +":" + row.get(key));
			}
		}
	}
}
