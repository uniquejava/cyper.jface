package hello.layout.aqua;

import java.util.Enumeration;
import java.util.Hashtable;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class ImageFactory {
	public final static String REAL_PATH = System.getProperty("user.dir") + "/icons/";
	public final static String LOGO = "logo.gif";
	public final static String DATABASE_SERVER = "database_server.gif";
	public final static String DATABASE = "database.gif";
	public final static String SERVER = "server.gif";
	public final static String SCRIPT = "script.gif";
	public final static String REGISTER_SERVER = "wiz_new_server.gif";
	public final static String FILTER = "filter.gif";
	public final static String SQL_EDITOR = "sql_editor.gif";
	public final static String EXECUTE_SQL = "execute_1616.gif";
	public final static String COMMIT = "commit_1616.gif";
	public final static String ROLLBACK = "rollback_1616.gif";
	public final static String ADD = "add_row.gif";
	public final static String SUBTRACT = "delete_row.gif";
	public final static String LOCK = "lock_1616.gif";
	public final static String UNLOCK = "unlock_1616.gif";
	public final static String UP = "up.gif";
	public final static String DOWN = "down.gif";
	public final static String TICK = "running.gif";
	public final static String MYTICK = "running.gif";
	public final static String DOWN1 = "down1_1616.gif";
	public final static String DOWN2 = "down2_1616.gif";
	public final static String WYJ = "wyj_1616.gif";
	public final static String COLUMN_MODE = "column_mode_1616.gif";
	public final static String PREV = "up_1616.gif";
	public final static String NEXT = "down_1616.gif";
	public final static String ROW_INDICATOR = "row_indicator.gif";
	public final static String NODE_TABLE = "table_icon.gif";
	

	private ImageFactory() {
	}

	private static Hashtable<String, Image> hImage = new Hashtable<String, Image>();

	
	public static Image loadImage(String imageName) {
		Image image = (Image) hImage.get(imageName.toUpperCase());
		if (image == null) {
			image = new Image(Display.getCurrent(), REAL_PATH + imageName);
			hImage.put(imageName.toUpperCase(), image);
		}
		return image;
	}
	public static Image loadImage(Display display, String imageName) {
		Image image = (Image) hImage.get(imageName.toUpperCase());
		if (image == null) {
			image = new Image(display, REAL_PATH + imageName);
			hImage.put(imageName.toUpperCase(), image);
		}
		return image;
	}

	public static void displose() {
		Enumeration<Image> e = hImage.elements();
		while (e.hasMoreElements()) {
			Image image = (Image) e.nextElement();
			if (!image.isDisposed()) {
				image.dispose();
			}
		}
	}
}
