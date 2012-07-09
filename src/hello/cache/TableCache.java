package hello.cache;

import hello.filter.FmsRule;
import hello.layout.aqua.CyperDataStudio;
import hello.layout.aqua.dialog.LogonDialog;
import hello.layout.aqua.util.DbUtil;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.kitten.core.C;
import org.kitten.core.util.StringUtil;
import org.springframework.util.Assert;

/**
 * cacheDir/connectonName/tableType/tableList.xml
 * 
 * @author cyper.yin
 * 
 */
public class TableCache {
	public final static String CACHE_DIR = C.UD + "/cache_dir";

	private final static Map<String, Map<String, Map<String, Table>>> cache = Collections
			.synchronizedMap(new LinkedHashMap<String, Map<String, Map<String, Table>>>());

	private static Map<String, String[]> assistant_keywords = new LinkedHashMap<String, String[]>();

	private static TableCache instance;

	public static TableCache getInstance() {
		if (instance == null) {
			instance = new TableCache();
		}
		return instance;
	}

	public TableCache() {
		load();
	}

	/**
	 * 按cache_dir的层次结构初始化cache.
	 * 
	 */
	public void load() {
		// FIXME 只加载当前connection所需的缓存?
		File cacheDir = new File(CACHE_DIR);
		if (!cacheDir.exists()) {
			cacheDir.mkdirs();
		}
		File[] connectionNamesFile = cacheDir.listFiles();
		for (int i = 0; i < connectionNamesFile.length; i++) {
			File connectionNameFile = connectionNamesFile[i];
			String connectionName = connectionNameFile.getName();
			// level one
			cache.put(connectionName,
					new LinkedHashMap<String, Map<String, Table>>());

			File[] tableTypeFiles = connectionNameFile.listFiles();
			for (int j = 0; j < tableTypeFiles.length; j++) {
				File tableTypeFile = tableTypeFiles[j];
				String tableType = tableTypeFile.getName();
				// level two
				cache.get(connectionName).put(tableType,
						new LinkedHashMap<String, Table>());

				File[] xmlFiles = new File(CACHE_DIR + "/" + connectionName
						+ "/" + tableType).listFiles();
				for (int k = 0; k < xmlFiles.length; k++) {
					File xmlFile = xmlFiles[k];
					System.out.println(xmlFile.getName());
					try {
						List<Table> tableList = new TableBuilder(
								xmlFile.getAbsolutePath()).loadConfig();
						if (tableList.size() > 1) {
							System.err.println("impossilbe now!!!!!!!!!!!!!");
						}

						if (tableList.size() > 0) {
							Table t = tableList.get(0);
							// level three
							cache.get(connectionName).get(tableType)
									.put(t.getName(), t);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}
		}
	}

	public void persist() {
		int itemCount = getItemsCount();
		System.out.println("item count in memory:" + itemCount);

		for (String cName : cache.keySet()) {
			Map<String, Map<String, Table>> cNameMap = cache.get(cName);
			for (String type : cNameMap.keySet()) {
				Map<String, Table> typeMap = cNameMap.get(type);
				for (String tableName : typeMap.keySet()) {
					Table t = typeMap.get(tableName);
					String path = CACHE_DIR + "/" + cName + "/" + type + "/"
							+ tableName + ".xml";
					try {
						new TableBuilder(path).saveTables(Arrays.asList(t));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}

	}

	public int getItemsCount() {
		int sum = 0;
		for (String cName : cache.keySet()) {
			Map<String, Map<String, Table>> cNameMap = cache.get(cName);
			for (String type : cNameMap.keySet()) {
				sum += cNameMap.get(type).keySet().size();
			}
		}
		return sum;
	}
	
	public Map<String, Map<String, Table>> get(String connectionName){
		return cache.get(connectionName);
	}
	
	public Table getTable(String tableName) {
		String cName = LogonDialog.currentConnectionName;
		Assert.notNull(cName);

		Map<String, Map<String, Table>> cNameMap = cache.get(cName);
		for (String type : cNameMap.keySet()) {
			Map<String, Table> map = cNameMap.get(type);
			Table t = map.get(tableName);
			if (t != null) {
				return t;
			}
		}
		return null;
	}

	public void put(Table t) {
		String type = StringUtil.nvl(t.getType(), "EMPTY_TYPE");
		getClassifiedCache(type).put(t.getName().toUpperCase(), t);
	}

	public Set<String> getTableTypes() {
		String cName = LogonDialog.currentConnectionName;
		Assert.notNull(cName);
		
		Map<String, Map<String, Table>> cNameMap = cache.get(cName);
		return cNameMap.keySet();
	}
	
	public Set<String> getTableNamesOfType(String type) {
		String cName = LogonDialog.currentConnectionName;
		Assert.notNull(cName);
		Map<String, Map<String, Table>> cNameMap = cache.get(cName);
		return cNameMap.get(type).keySet();
	}
	
	public Set<String> getAllTableNames() {
		Set<String> keySets = new HashSet<String>();
		String cName = LogonDialog.currentConnectionName;
		Assert.notNull(cName);

		Map<String, Map<String, Table>> cNameMap = cache.get(cName);
		for (String type : cNameMap.keySet()) {
			keySets.addAll(cNameMap.get(type).keySet());
		}

		return keySets;
	}

	public Map<String, Table> getClassifiedCache(String type) {
		Assert.notNull(type);
		type = type.toUpperCase();

		String cName = LogonDialog.currentConnectionName;
		Assert.notNull(cName);

		Map<String, Map<String, Table>> cNameMap = cache.get(cName);
		if (cNameMap == null) {
			cNameMap = new LinkedHashMap<String, Map<String, Table>>();
			cache.put(cName, cNameMap);
		}

		Map<String, Table> classifiedCache = cNameMap.get(type);
		if (classifiedCache == null) {
			classifiedCache = new LinkedHashMap<String, Table>();
			cNameMap.put(type, classifiedCache);
		}

		return cache.get(cName).get(type);
	}

	public String[] getContentAssistantKeywords() {
		String cName = LogonDialog.currentConnectionName;
		Assert.notNull(cName);

		if (assistant_keywords.get(cName) == null) {
			List<String> nameList = new ArrayList<String>();
			nameList.add("select * from ");
			//FIXME bad smell
			
			String schema = CyperDataStudio.getStudio().getTableFilter().getRule().getSchemaPattern();
			if (StringUtil.isNotBlank(schema)) {
				nameList.add(schema);
			}
			nameList.add("CURRENT TIMESTAMP");
			
			nameList.addAll(getAllTableNames());

			String[] keywords = new String[nameList.size()];
			nameList.toArray(keywords);
			assistant_keywords.put(cName, keywords);
		}

		return assistant_keywords.get(cName);
	}

	public Table getTableColumnInfo(String tableName) {
		// 尝试从Cache中取字段信息.
		Table tableWithFields = getTable(tableName);
		if (tableWithFields == null) {
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

				// System.out.println("TableCache.tables="
				// + TableCache.tables.size());
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
