package hello.example.ktable.dao;

import hello.example.ktable.util.DataRow;
import hello.example.ktable.util.HeaderRow;
import hello.example.ktable.util.QueryResult;
import hello.example.ktable.util.Row;
import hello.layout.aqua.util.DbUtil;
import hello.layout.aqua.util.SqlUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TestDao {
	
	public QueryResult query(String sql) throws Exception {
		QueryResult result = new QueryResult();
		result.setTableName(SqlUtil.getTableName(sql));
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
			int columnCount = meta.getColumnCount();
			String[] tableHeaders = new String[columnCount];
			String[] types = new String[columnCount];

			Row headerRow = new HeaderRow();
			Row typeRow = new HeaderRow();
			for (int i = 0; i < columnCount; i++) {
				tableHeaders[i] = meta.getColumnName(i + 1);
				// the first row is column name info
				headerRow.put(tableHeaders[i], tableHeaders[i]);
				
				types[i] = meta.getColumnTypeName(i + 1);
				//the 2nd row is types
				typeRow.put(tableHeaders[i], types);
			}
			result.setColumnCount(columnCount);
			result.setHeaderRow(headerRow);
			result.setTableHeaders(tableHeaders);
			result.setTypes(types);
			result.setTypeRow(typeRow);

			
			
			int rowNum = 0;
			List<Row> dataRow = new ArrayList<Row>();
			while (rs.next()) {
				rowNum++;
				Row row = new DataRow();
				row.put(Row.KEY_INDICATOR, /*rowNum == 1 ? ">" : */"");
				row.put(Row.KEY_ROW_NUMBER, String.valueOf(rowNum));

				for (int i = 0; i < tableHeaders.length; i++) {
					row.put(tableHeaders[i], rs.getString(tableHeaders[i]));
				}
				dataRow.add(row);
				
				if (rowNum==1000) {
					break;
				}
			}
			result.setDataRow(dataRow);
		} catch (Exception e) {
			throw e;
		} finally {
			DbUtil.close(rs, stmt, conn);
		}
		
		
		return result;
	}


	public static void main(String[] args) throws Exception {
		TestDao dao = new TestDao();
		QueryResult result = dao.query("select * from EMPLOYEE");
		for (Row row : result.getDataRow()) {
//			for (Iterator it = row.keySet().iterator(); it.hasNext();) {
//				String key = (String) it.next();
//				System.out.println(key + ":" + row.get(key));
//			}
			System.out.println(row);
		}
	}
}
