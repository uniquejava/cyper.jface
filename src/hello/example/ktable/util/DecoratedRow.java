package hello.example.ktable.util;

import java.util.LinkedHashMap;

public class DecoratedRow extends LinkedHashMap<String, Object> {
	public final static String KEY_INDICATOR = "KEY_INDICATOR";
	public final static String KEY_ROW_NUMBER = "KEY_ROW_NUMBER";

	public DecoratedRow(LinkedHashMap<String, Object> row, int rowNumber,
			boolean setRowDicator, int blankRowCount) {
		//FIXME need to remove  the if
		if (row instanceof BlankRow) {
//			this.put(KEY_INDICATOR, "");
//			this.put(KEY_ROW_NUMBER, "");
//			this.putAll(row);
			throw new IllegalStateException("blank row cannot be here");
		} else {
			this.put(KEY_INDICATOR, setRowDicator ? ">" : "");
			this.put(KEY_ROW_NUMBER, String.valueOf(rowNumber-blankRowCount));
			this.putAll(row);
		}

	}
}
