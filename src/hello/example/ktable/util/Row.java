package hello.example.ktable.util;

import java.util.LinkedHashMap;

public abstract class Row extends LinkedHashMap<String, Object> {
	public final static String KEY_INDICATOR = "KEY_INDICATOR";
	public final static String KEY_ROW_NUMBER = "KEY_ROW_NUMBER";
	public static boolean hasIndicator(Row row){
		return ">".equals(row.get(KEY_INDICATOR));
	}
}
