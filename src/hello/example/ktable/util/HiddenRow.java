package hello.example.ktable.util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 在delete一行后，该行依然保留在list中，但沦为HiddenRow
 * @author cyper.yin
 *
 */
public class HiddenRow extends LinkedHashMap<String, Object> {
	/**
	 * 给入参换个马甲
	 * 
	 * @param map
	 */
	public HiddenRow(Map map) {
		this.putAll(map);
	}
}
