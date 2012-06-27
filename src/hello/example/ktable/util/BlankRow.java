package hello.example.ktable.util;


/**
 * blank row是可以有值的，但它是不带行号的.
 * 
 * @author cyper.yin
 * 
 */
public class BlankRow extends Row {
	public BlankRow(String[] tableHeader) {
		for (int i = 0; i < tableHeader.length; i++) {
			this.put(tableHeader[i], "");
		}
	}
}
