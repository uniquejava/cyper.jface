package hello.cache;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.jdom.Document;
import org.jdom.Element;
import org.kitten.core.C;
import org.kitten.core.util.JDomUtil;
import org.kitten.core.util.StringUtil;

/**
 * 
 * @author cyper.yin
 * 
 */
public class TableBuilder {
	private String cacheFile;

	public TableBuilder(String configFile) {
		this.cacheFile = configFile;
	}

	public List<Table> loadConfig() throws Exception {
		List<Table> result = new ArrayList<Table>();
		File config = new File(cacheFile);
		if (!config.exists()) {
			// create an empty config file
			config.getParentFile().mkdirs();
			config.createNewFile();
			saveTables(null);
			return result;
		}

		Document doc = JDomUtil.buildDocumentFromFile(cacheFile);
		Element root = doc.getRootElement();
		List<Element> tables = root.getChildren();
		for (int i = 0; i < tables.size(); i++) {
			Element t = tables.get(i);
			Table info = new Table();
			info.setName(t.getAttributeValue("name"));
			info.setTabschema(t.getAttributeValue("tabschema"));
			info.setType(t.getAttributeValue("type"));
			info.setColcount(t.getAttributeValue("colcount"));
			info.setComment(t.getAttributeValue("comment"));

			List<Element> fields = t.getChildren();
			Set<Field> fieldList = new LinkedHashSet<Field>();
			for (Element f : fields) {
				Field field = new Field();
				field.setName(f.getAttributeValue("name"));
				field.setTbname(f.getAttributeValue("tbname"));
				field.setColtype(f.getAttributeValue("coltype"));
				field.setLength(f.getAttributeValue("length"));
				field.setScale(f.getAttributeValue("scale"));
				field.setColno(f.getAttributeValue("colno"));
				field.setNulls(f.getAttributeValue("nulls"));
				field.setDefaults(f.getAttributeValue("defaults"));
				fieldList.add(field);
			}
			info.setFields(fieldList);
			result.add(info);
		}
		return result;
	}

	public void saveTables(Collection<Table> tables) throws Exception {
		Element rootElement = new Element("tables");
		Document doc = new Document(rootElement);
		if (tables != null) {
			for (Table info : tables) {
				Element table = new Element("table");
				table.setAttribute("name", info.getName());
				table.setAttribute("tabschema", StringUtil.nvl(info.getTabschema()));
				table.setAttribute("type", StringUtil.nvl(info.getType()));
				table.setAttribute("colcount",StringUtil.nvl(info.getColcount()));
				table.setAttribute("comment", StringUtil.nvl(info.getComment()));

				for (Field f : info.getFields()) {
					Element field = new Element("field");
					field.setAttribute("name", f.getName());
					field.setAttribute("tbname", f.getTbname());
					field.setAttribute("coltype", StringUtil.nvl(f.getColtype()));
					field.setAttribute("length", StringUtil.nvl(f.getLength()));
					field.setAttribute("scale", StringUtil.nvl(f.getScale()));
					field.setAttribute("colno", StringUtil.nvl(f.getColno()));
					field.setAttribute("nulls", StringUtil.nvl(f.getNulls()));
					field.setAttribute("defaults",
							StringUtil.nvl(f.getDefaults()));
					table.addContent(field);
				}
				rootElement.addContent(table);
			}
		}

		File config = new File(cacheFile);
		if (!config.exists()) {
			config.getParentFile().mkdirs();
			config.createNewFile();
		}
		JDomUtil.save(doc, cacheFile, "UTF-8", JDomUtil.FORMAT_PRETTY);
	}

	public static void main(String[] args) throws Exception {
		TableBuilder builder = new TableBuilder(C.UD
				+ "/cache_dir/localhost_sample.xml");
		List<Table> list = builder.loadConfig();
		builder.saveTables(list);
	}
}
