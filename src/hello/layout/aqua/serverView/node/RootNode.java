package hello.layout.aqua.serverView.node;

import hello.cache.Field;
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

public class RootNode extends AbstractFolderNode {

	private String name;
	private boolean init = false;
	private List<Node> children = new ArrayList<Node>();

	public RootNode(String name, TableFilter tableFilter) {
		this.name = name;
		this.tableFilter = tableFilter;
	}

	@Override
	public String getName() {
		return this.name.toUpperCase();
	}

	@Override
	public List<Node> getChildren() {
		if (!init) {
			Connection conn = null;
			ResultSet rs = null;
			Statement stmt = null;
			try {
				conn = DbUtil.getConnection();

				String[] designatedTableTypes = null;

				if (tableFilter != null) {
					designatedTableTypes = tableFilter.getRule()
							.getDesignatedTableTypes();
				}

				if (conn != null) {
					rs = conn.getMetaData().getTableTypes();
					while (rs.next()) {
						String tableType = rs.getString("TABLE_TYPE");
						if (designatedTableTypes != null
								&& !ArrayUtils.contains(designatedTableTypes,
										tableType)) {
							continue;
						}
						children.add(new TableFolderNode(this, tableType,tableFilter));
					}

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
		return null;
	}

	@Override
	public Image getImage() {
		return ImageFactory.loadImage(Display.getCurrent(),
				ImageFactory.DATABASE);
	}

}
