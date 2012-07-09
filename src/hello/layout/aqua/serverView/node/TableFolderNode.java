package hello.layout.aqua.serverView.node;

import hello.cache.Table;
import hello.cache.TableCache;
import hello.filter.TableFilter;
import hello.layout.aqua.ImageFactory;
import hello.layout.aqua.util.DbUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.kitten.core.util.NameUtil;

public class TableFolderNode extends AbstractFolderNode {

	private Node parent;
	private String name;
	private boolean init = false;
	private boolean useCache = false;
	private List<Node> children = new ArrayList<Node>();

	public TableFolderNode(Node parent, String name, TableFilter tableFilter, boolean useCache) {
		this.parent = parent;
		this.name = name;
		this.tableFilter = tableFilter;
		this.useCache = useCache;
	}

	@Override
	public String getName() {
		return NameUtil.upper(name.toLowerCase());
	}

	@Override
	public List<Node> getChildren() {
		if (!init) {
			if (useCache) {
				String[] designatedNames = null;
				if (tableFilter != null) {
					designatedNames = tableFilter.getRule()
							.getDesignatedNames();
				}

				Set<String> tableNames = TableCache.getInstance().getTableNamesOfType(this.name);
				for (String tableName : tableNames) {
					if (designatedNames != null&& !ArrayUtils.contains(designatedNames,tableName)) {
						continue;
					}
					children.add(new TableNode(this, tableName));
				}
			}else{
				loadFromDb();
			}
			
			init = true;
		}
		return children;
	}

	private void loadFromDb() {
		Connection conn = null;
		ResultSet rs = null;
		Statement stmt = null;
		String schemaPattern = tableFilter.getRule().getSchemaPattern();
		String tableNamePattern = tableFilter.getRule()
				.getTableNamePattern();
		try {
			conn = DbUtil.getConnection();
			if (conn != null) {

				rs = conn.getMetaData().getTables(null, schemaPattern,
						tableNamePattern, new String[] { name });

//				ResultSetMetaData meta = rs.getMetaData();
//				int count = meta.getColumnCount();
//				String[] tableHeaders = new String[count];
//				for (int i = 0; i < count; i++) {
//					tableHeaders[i] = meta.getColumnName(i + 1);
//					// // the first row is column name info
//				}

				String[] designatedNames = null;
				if (tableFilter != null) {
					designatedNames = tableFilter.getRule()
							.getDesignatedNames();
				}

				int i = 0;
				while (rs.next()) {
					System.out.println(i++);
					String tableName = rs.getString("TABLE_NAME");
					if (designatedNames != null
							&& !ArrayUtils.contains(designatedNames,
									tableName)) {
						continue;
					}
					children.add(new TableNode(this, tableName));

					// put to cache.
					Table t = new Table();
					t.setName(tableName);
					t.setType(this.name);
					TableCache.getInstance().put(t);
				}
				rs.close();
				rs = null;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbUtil.close(rs, stmt, conn);
		}
	}

	@Override
	public Node getParent() {
		return parent;
	}

	@Override
	public Image getImage() {
		return ImageFactory.loadImage(Display.getCurrent(),
				ImageFactory.CLOSED_FOLDER);
	}

}
