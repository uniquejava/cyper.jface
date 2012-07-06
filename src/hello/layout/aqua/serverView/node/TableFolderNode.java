package hello.layout.aqua.serverView.node;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.kitten.core.util.NameUtil;

import hello.layout.aqua.ImageFactory;
import hello.layout.aqua.util.DbUtil;

public class TableFolderNode  extends AbstractNode {
	
	private Node parent;
	private String name;
	private boolean init = false;
	private List<Node> children = new ArrayList<Node>();
	
	public TableFolderNode(Node parent,String name) {
		this.parent = parent;
		this.name = name;
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
			try {
				conn = DbUtil.getConnection();
				if (conn != null) {
					rs = conn.getMetaData().getTables(null, null, null,
							new String[] { name });
					ResultSetMetaData meta = rs.getMetaData();
					int count = meta.getColumnCount();
					String[] tableHeaders = new String[count];

					for (int i = 0; i < count; i++) {
						tableHeaders[i] = meta.getColumnName(i + 1);
						// // the first row is column name info
					}

					while (rs.next()) {
						String tableName = rs.getString("TABLE_NAME");
						// System.out.println(tableName);
						children.add(new TableNode(this,tableName));
					}

				}
				// children.add(new TableNode(this, "ORG"));
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
