package hello.layout.aqua.dialog.connect;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.kitten.core.C;
import org.kitten.core.util.DES;
import org.kitten.core.util.JDomUtil;

public class ConnectionInfoBuilder {
	private String configFile = C.UD + "/connections.xml";

	public ConnectionInfoBuilder() {
	}

	public ConnectionInfoBuilder(String configFile) {
		this.configFile = configFile;
	}

	public List<ConnectionInfo> loadConfig() throws Exception {
		List<ConnectionInfo> result = new ArrayList<ConnectionInfo>();
		File config = new File(configFile);
		if (!config.exists()) {
			// create an empty config file
			saveConfig(null);
			return result;
		}

		Document doc = JDomUtil.buildDocumentFromFile(configFile);
		Element root = doc.getRootElement();
		List<Element> connects = root.getChildren();
		for (int i = 0; i < connects.size(); i++) {
			Element connect = connects.get(i);
			Element host = connect.getChild("host");
			Element port = connect.getChild("port");
			Element database = connect.getChild("database");
			Element schema = connect.getChild("schema");
			Element username = connect.getChild("username");
			Element password = connect.getChild("password");

			ConnectionInfo info = new ConnectionInfo();
			info.setName(connect.getAttributeValue("name"));
			info.setHost(host.getText());
			info.setPort(port.getText());
			info.setDatabase(database.getText());
			info.setSchema(schema.getText());
			info.setUsername(username.getText());
			info.setPassword(DES.decrypt(password.getText()));
			result.add(info);
		}
		return result;
	}

	public void saveConfig(Collection<ConnectionInfo> connects)
			throws Exception {
		Element rootElement = new Element("connections");
		Document doc = new Document(rootElement);
		if (connects != null) {
			for (ConnectionInfo info : connects) {
				Element connection = new Element("connection");
				connection.setAttribute("name", info.getName());
				Element host = new Element("host");
				host.setText(info.getHost());
				connection.addContent(host);

				Element port = new Element("port");
				port.setText(info.getPort());
				connection.addContent(port);

				Element db = new Element("database");
				db.setText(info.getDatabase());
				connection.addContent(db);

				Element schema = new Element("schema");
				schema.setText(info.getSchema());
				connection.addContent(schema);

				Element username = new Element("username");
				username.setText(info.getUsername());
				connection.addContent(username);

				Element password = new Element("password");
				String pwd = info.getPassword();
				if (!pwd.startsWith(DES.PREFIX)) {
					pwd = DES.encrypt(pwd);
				}
				password.setText(pwd);
				connection.addContent(password);
				rootElement.addContent(connection);
			}
		}

		JDomUtil.save(doc, configFile, "UTF-8", JDomUtil.FORMAT_PRETTY);
	}

	public static void main(String[] args) throws Exception {
		ConnectionInfoBuilder builder = new ConnectionInfoBuilder();
		List<ConnectionInfo> list = builder.loadConfig();
		System.out.println(list.size());
		builder.saveConfig(list);
	}
}
