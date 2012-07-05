package hello.layout.aqua.dialog.connect;

import org.kitten.core.util.StringUtil;

public class ConnectionInfo {
	private String name;
	private String host;
	private String port;
	private String database;
	private String schema;
	private String username;
	private String password;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String toDB2String() {
		StringBuffer sb = new StringBuffer();
		sb.append("jdbc:db2://");
		sb.append(host);
		sb.append(":");
		sb.append(port);
		sb.append("/");
		sb.append(database);
		sb.append(":retrieveMessagesFromServerOnGetMessage=true;");
		if (StringUtil.isNotBlank(schema)) {
			sb.append("currentSchema=");
			sb.append(schema);
			sb.append(";");
		}
		return sb.toString();
	}

}
