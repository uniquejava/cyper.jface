package hello.layout.aqua.sqlwindow.editor;

public class EditorConstants {
	public final static String COLOR_TEXT = "text";
	public final static String COLOR_KEYWORD = "keyword";
	public final static String COLOR_FUN = "function";
	public final static String COLOR_COMMENT = "comment";
	public final static String COLOR_STRING = "string";
	
	//WARNING: no space is allowed in each keyword!
	public final static String[] colored_keywords = {"select","delete","update","from","where","inner","join","group","order",
		"by","asc","desc","as","insert","into","set","with","like","values","exist", "and","or","is","distinct","having"};
	
	public final static String[] colored_functions = {"count","sum","current","timestamp","fetch","first","rows","only"};
}
