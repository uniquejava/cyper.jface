package hello.example.ktable.util;

import java.util.LinkedHashMap;

public class BlankRow extends LinkedHashMap<String, Object> {
	public BlankRow(String[] tableHeader) {
		for (int i = 0; i < tableHeader.length; i++) {
			this.put(tableHeader[i], "");
		}
	}
}
