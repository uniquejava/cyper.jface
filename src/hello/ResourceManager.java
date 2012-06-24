package hello;


import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.swt.graphics.Color;

public class ResourceManager {
	public static final String COLOR_WHITE = "WHITE";
	public static final String COLOR_KEYWORD = "KEYWORD";
	public static final String COLOR_COMMENT = "COMMENT";
	public static final String COLOR_STRING = "STRING";
	public static final String COLOR_SQL_HIGHLIGHT = "SQL_HILIGHT";
	public static final String COLOR_ROW_SELECTED = "ROW_SELECTED";

	private ResourceManager() {
	}
	private static ColorRegistry colorRegistry;

	public static ColorRegistry getColorRegistry() {
		if (colorRegistry == null) {
			colorRegistry = new ColorRegistry();
			colorRegistry.put(COLOR_WHITE, StringConverter.asRGB("255,255,255"));
			colorRegistry.put(COLOR_KEYWORD, StringConverter.asRGB("127,0,85"));
			colorRegistry
					.put(COLOR_COMMENT, StringConverter.asRGB("63,127,95"));
			colorRegistry.put(COLOR_STRING, StringConverter.asRGB("0,0,255"));
			colorRegistry.put(COLOR_SQL_HIGHLIGHT, StringConverter.asRGB("229,255,229"));
			colorRegistry.put(COLOR_ROW_SELECTED, StringConverter.asRGB("232,242,254"));
		}
		return colorRegistry;
	}

	public static Color getColor(String key) {
		return getColorRegistry().get(key);
	}

}
