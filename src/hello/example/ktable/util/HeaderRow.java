package hello.example.ktable.util;

public class HeaderRow extends Row {
	private static final long serialVersionUID = 1L;

	public HeaderRow() {
		this.put(KEY_INDICATOR, "");
		this.put(KEY_ROW_NUMBER, "");
	}
}
