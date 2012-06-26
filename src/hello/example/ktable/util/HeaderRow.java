package hello.example.ktable.util;

import java.util.LinkedHashMap;

public class HeaderRow extends LinkedHashMap<String, Object> {
	private static final long serialVersionUID = 1L;

	public HeaderRow() {
		this.put(DecoratedRow.KEY_INDICATOR, "");
		this.put(DecoratedRow.KEY_ROW_NUMBER, "");
	}
}
