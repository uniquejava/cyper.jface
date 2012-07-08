package hello.cache;

import hello.layout.aqua.CyperDataStudio;
import hello.layout.aqua.util.DbUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TableCache {
	public final static Map<String, Table> tables = Collections
			.synchronizedMap(new LinkedHashMap<String, Table>());
	
	private static String[] assistant_keywords = null;
	
	public static String[] getContentAssistantKeywords() {
		if (assistant_keywords==null) {
			List<String> nameList = new ArrayList<String>();
			nameList.add("select * from ");
			nameList.addAll(tables.keySet());

			assistant_keywords = new String[nameList.size()];
			nameList.toArray(assistant_keywords);
		}
		return assistant_keywords;
	}

	public static Table getTableColumnInfo(String tableName) {
		// 尝试从Cache中取字段信息.
		Table tableWithFields = tables.get(tableName);
		if (tableWithFields==null) {
			return null;
		}
		
		if (tableWithFields.getFields().size() > 0) {
			return tableWithFields;
		}

		// 如果尚未初始化字段信息.

		Connection conn = null;
		ResultSet rs = null;
		Statement stmt = null;
		String schemaPattern = CyperDataStudio.getStudio().getTableFilter()
				.getRule().getSchemaPattern();
		try {

			conn = DbUtil.getConnection();
			if (conn != null) {
				// 对于字段信息的补充.
				// 补充cache中table的数据字典信息.
				// 当用户的权限不足时，以下会报异常，还是得从JDBC入手
				/*
				 * String sql =
				 * "select T.tabschema,T.tabname,T.type,T.colcount," +
				 * "C.name as COLUMN_NAME,C.coltype,C.length,C.scale,C.colno,C.nulls,C.default "
				 * + "from  syscat.tables as T " +
				 * "inner join sysibm.syscolumns as C " +
				 * "on T.TABNAME = C.TBNAME"; stmt = conn.createStatement(); rs
				 * = stmt.executeQuery(sql); while (rs.next()) { String tabname
				 * = rs.getString("TABNAME"); if
				 * (TableCache.tables.containsKey(tabname)) { Table t =
				 * TableCache.tables.get(tabname);
				 * t.setTabschema(rs.getString("TABSCHEMA"));
				 * t.setColcount(rs.getString("COLCOUNT"));
				 * 
				 * Set<Field> fs = t.getFields(); Field f = new Field();
				 * f.setName(rs.getString("COLUMN_NAME"));
				 * f.setTbname(rs.getString("TABNAME"));
				 * f.setColtype(rs.getString("COLTYPE"));
				 * f.setLength(rs.getString("LENGTH"));
				 * f.setScale(rs.getString("SCALE"));
				 * f.setColno(rs.getString("COLNO"));
				 * f.setNulls(rs.getString("NULLS"));
				 * f.setDefaults(rs.getString("DEFAULT")); fs.add(f); } }
				 */

				System.out.println("TableCache.tables="
						+ TableCache.tables.size());
				rs = conn.getMetaData().getColumns(null, schemaPattern,
						tableName, null);
				Set<Field> fs = tableWithFields.getFields();

				int columnCount = 0;
				while (rs.next()) {
					columnCount++;
					Field f = new Field();
					f.setName(rs.getString("COLUMN_NAME"));
					f.setTbname(tableName);
					f.setColtype(rs.getString("TYPE_NAME"));
					f.setLength(rs.getString("COLUMN_SIZE"));
					f.setScale(rs.getString("DECIMAL_DIGITS"));
					f.setColno(rs.getString("ORDINAL_POSITION"));
					f.setNulls(rs.getString("IS_NULLABLE"));
					f.setDefaults(rs.getString("COLUMN_DEF"));
					fs.add(f);
				}
				System.out.println("columnCount=" + columnCount);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbUtil.close(rs, stmt, conn);
		}
		return tableWithFields;
	}
}
