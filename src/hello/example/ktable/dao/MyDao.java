package hello.example.ktable.dao;

import hello.example.ktable.util.DataRow;
import hello.example.ktable.util.HeaderRow;
import hello.example.ktable.util.Row;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MyDao {
	public List<Row> querySql(String sql) {

		List<Row> result = new ArrayList<Row>();
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
			rs = stmt.executeQuery(sql);

			ResultSetMetaData meta = rs.getMetaData();
			int count = meta.getColumnCount();
			String[] tableHeaders = new String[count];

			Row headerRow = new HeaderRow();
			for (int i = 0; i < count; i++) {
				tableHeaders[i] = meta.getColumnName(i + 1);
				// the first row is column name info
				headerRow.put(tableHeaders[i], tableHeaders[i]);
			}
			result.add(headerRow);

			int rowNum = 0;
			while (rs.next()) {
				rowNum++;
				Row row = new DataRow();
				row.put(Row.KEY_INDICATOR, /*rowNum == 1 ? ">" : */"");
				row.put(Row.KEY_ROW_NUMBER, String.valueOf(rowNum));

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
		List<Row> list = dao.querySql("select * from EMPLOYEE");
		for (Row row : list) {
//			for (Iterator it = row.keySet().iterator(); it.hasNext();) {
//				String key = (String) it.next();
//				System.out.println(key + ":" + row.get(key));
//			}
			System.out.println(row);
		}
	}
}
