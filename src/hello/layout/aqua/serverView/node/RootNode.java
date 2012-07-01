package hello.layout.aqua.serverView.node;

import hello.layout.aqua.ImageFactory;
import hello.layout.aqua.util.DbUtil;
import hello.layout.aqua.util.Node;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class RootNode implements Node {

	private boolean init = false;
	private List<Node> children;

	@Override
	public String getName() {
		return "Database Servers";
	}

	@Override
	public boolean hasChildren() {
		return getChildren().size() > 0;
	}

	@Override
	public List<Node> getChildren() {
		if (!init) {
			Connection conn = null;
			ResultSet rs = null;
			Statement stmt = null;
			try {
				conn = DbUtil.getConnection();
				stmt = conn.createStatement();
				int rows = stmt.executeUpdate("set current schema DBEFMSVR");
				System.out.println("rows=" + rows);

				rs = conn.getMetaData().getTables(null, null, null,
						new String[] { "TABLE" });
				ResultSetMetaData meta = rs.getMetaData();
				int count = meta.getColumnCount();
				String[] tableHeaders = new String[count];

				for (int i = 0; i < count; i++) {
					tableHeaders[i] = meta.getColumnName(i + 1);
					// // the first row is column name info
				}

				children = new ArrayList<Node>();

				while (rs.next()) {
					String tableName = rs.getString("TABLE_NAME");
					System.out.println(tableName);
					children.add(new TableNode(this, rs.getString("TABLE_NAME")));
				}
				// children.add(new TableNode(this, "test"));
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
				ImageFactory.DATABASE_SERVER);
	}

}
