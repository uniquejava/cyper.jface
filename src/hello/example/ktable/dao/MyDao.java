package hello.example.ktable.dao;

import hello.example.ktable.util.DataRow;
import hello.example.ktable.util.HeaderRow;
import hello.example.ktable.util.QueryResult;
import hello.example.ktable.util.Row;
import hello.layout.aqua.util.DbUtil;
import hello.layout.aqua.util.SqlUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MyDao {

	public int executeUpdate(String sql) throws Exception {
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = DbUtil.getConnection();
			stmt = conn.createStatement();
			return stmt.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			DbUtil.close(null, stmt, conn);
		}
	}
	public int executeBatch(String[] sqls) throws Exception {
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = DbUtil.getConnection();
			conn.setAutoCommit(false);
			stmt = conn.createStatement();
			for (int i = 0; i < sqls.length; i++) {
				stmt.addBatch(sqls[i]);
			}
			
			int rows[] = stmt.executeBatch();
			conn.commit();
			
			int sum = 0;
			for (int i = 0; i < rows.length; i++) {
				sum += rows[i];
			}
			return sum;
		} catch (Exception e) {
			if (conn!=null) {
				conn.rollback();
			}
			throw e;
		} finally {
			DbUtil.close(null, stmt, conn);
		}
	}

	public QueryResult query(String sql) throws Exception {
		QueryResult result = new QueryResult();
		result.setTableName(SqlUtil.getTableName(sql));
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = DbUtil.getConnection();
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
				// the 2nd row is types
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
				row.put(Row.KEY_INDICATOR, /* rowNum == 1 ? ">" : */"");
				row.put(Row.KEY_ROW_NUMBER, String.valueOf(rowNum));

				for (int i = 0; i < tableHeaders.length; i++) {
					row.put(tableHeaders[i], rs.getString(tableHeaders[i]));
				}
				dataRow.add(row);

				if (rowNum == 1000) {
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

}
