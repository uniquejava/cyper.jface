package hello.layout.aqua.serverView.node;

import hello.cache.Field;
import hello.cache.Table;
import hello.cache.TableCache;
import hello.filter.TableFilter;
import hello.layout.aqua.CyperDataStudio;
import hello.layout.aqua.ImageFactory;
import hello.layout.aqua.util.DbUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.kitten.core.util.NameUtil;

public class TableFolderNode extends AbstractFolderNode {

	private Node parent;
	private String name;
	private boolean init = false;
	private List<Node> children = new ArrayList<Node>();

	public TableFolderNode(Node parent, String name, TableFilter tableFilter) {
		this.parent = parent;
		this.name = name;
		this.tableFilter = tableFilter;
	}

	@Override
	public String getName() {
		return NameUtil.upper(name.toLowerCase());
	}

	@Override
	public List<Node> getChildren() {
		if (!init) {
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

					ResultSetMetaData meta = rs.getMetaData();
					int count = meta.getColumnCount();
					String[] tableHeaders = new String[count];
					for (int i = 0; i < count; i++) {
						tableHeaders[i] = meta.getColumnName(i + 1);
						// // the first row is column name info
					}

					String[] designatedNames = null;
					if (tableFilter != null) {
						designatedNames = tableFilter.getRule()
								.getDesignatedNames();
					}

					String type = null;
					if ("TABLE".equalsIgnoreCase(this.name)) {
						type = "T";
					} else if ("VIEW".equalsIgnoreCase(this.name)) {
						type = "V";
					} else if ("ALIAS".equalsIgnoreCase(this.name)) {
						type = "A";
					} else {
						System.err.println("unimplemented cache type ="
								+ this.name);
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
						t.setType(type);
						TableCache.tables.put(t.getName(), t);
					}
					rs.close();
					rs = null;
				}

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				DbUtil.close(rs, stmt, conn);
			}

			init = true;
		}
		return children;
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
