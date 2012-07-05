package hello.layout.aqua.sqlwindow.editor;

import static hello.layout.aqua.sqlwindow.editor.EditorConstants.COLOR_COMMENT;
import static hello.layout.aqua.sqlwindow.editor.EditorConstants.COLOR_KEYWORD;
import static hello.layout.aqua.sqlwindow.editor.EditorConstants.COLOR_STRING;

import java.util.List;
import java.util.Map;

import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.swt.graphics.Color;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

public class ResourceManager {
	private ResourceManager() {
	}

	private static ColorRegistry colorRegistry;
	private static Map<String, List<String>> tips;

	public static ColorRegistry getColorRegistry() {
		if (colorRegistry == null) {
			colorRegistry = new ColorRegistry();
			// java
			// colorRegistry.put(COLOR_KEYWORD,
			// StringConverter.asRGB("127,0,85"));
			// colorRegistry
			// .put(COLOR_COMMENT, StringConverter.asRGB("63,127,95"));
			// colorRegistry.put(COLOR_STRING,
			// StringConverter.asRGB("0,0,255"));
			// PL/SQL Developer
			colorRegistry.put(COLOR_KEYWORD, StringConverter.asRGB("0,128,128"));
			colorRegistry.put(COLOR_COMMENT, StringConverter.asRGB("255,0,0"));
			colorRegistry.put(COLOR_STRING, StringConverter.asRGB("0,0,255"));
		}
		return colorRegistry;
	}

	public static Color getColor(String key) {
		return getColorRegistry().get(key);
	}

	public static Map<String, List<String>> getTips() {
		if (tips == null) {
			BeanFactory bf = new XmlBeanFactory(new ClassPathResource(
					"applicationContext.xml"));
			tips = (Map<String, List<String>>) bf.getBean("tips");
		}
		return tips;
	}
}
