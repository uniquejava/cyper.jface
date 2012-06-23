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
	public final static String EXECUTE_SQL = "execute_sql.gif";

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
