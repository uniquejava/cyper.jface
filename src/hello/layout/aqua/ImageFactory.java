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
	public final static String LOGON = "logon_1616.gif";
	public final static String LOGON_BIG = "logon_big_1616.gif";
	public final static String FILTER = "filter.gif";
	public final static String SQL_EDITOR = "sql_editor.gif";
	public final static String EXECUTE_SQL = "execute_1616.gif";
	public final static String COMMIT = "commit_1616.gif";
	public final static String ROLLBACK = "rollback_1616.gif";
	public final static String OPEN = "open_1616.gif";
	public final static String SAVE = "save_1616.gif";
	public final static String REDO = "redo_1616.gif";
	public final static String UNDO = "undo_1616.gif";
	public final static String CUT = "cut_1616.gif";
	public final static String COPY = "copy_1616.gif";
	public final static String PASTE = "paste_1616.gif";
	public final static String FIND_REPLACE = "find_replace_1616.gif";
	public final static String BEAUTIFIER = "beautifier_1616.gif";
	public final static String INDENT = "indent_1616.gif";
	public final static String UNINDENT = "unindent_1616.gif";
	public final static String COMMENT = "comment_1616.gif";
	public final static String UNCOMMENT = "uncomment_1616.gif";
	
	
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
