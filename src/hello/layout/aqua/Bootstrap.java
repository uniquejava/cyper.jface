package hello.layout.aqua;

import hello.layout.aqua.dialog.connect.ConnectionInfo;
import hello.layout.aqua.dialog.connect.ConnectionInfoBuilder;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Bootstrap {

	private static Bootstrap instance = new Bootstrap();
	private java.util.Map<String, ConnectionInfo> connectionInfoMap;
	private ConnectionInfoBuilder builder;

	private Bootstrap() {

		connectionInfoMap = new LinkedHashMap<String, ConnectionInfo>();
		builder = new ConnectionInfoBuilder();
		try {
			List<ConnectionInfo> connectionInfos = builder.loadConfig();
			for (ConnectionInfo info : connectionInfos) {
				connectionInfoMap.put(info.getName(), info);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static Bootstrap getInstance() {
		return instance;
	}

	public Map<String, ConnectionInfo> getConnectionInfoMap() {
		return connectionInfoMap;
	}

	public void setConnectionInfoMap(
			Map<String, ConnectionInfo> connectionInfoMap) {
		this.connectionInfoMap = connectionInfoMap;
	}

	public void saveConnectionInfos() {
		try {
			builder.saveConfig(connectionInfoMap.values());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
