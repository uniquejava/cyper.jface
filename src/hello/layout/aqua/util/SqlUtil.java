package hello.layout.aqua.util;

import hello.layout.aqua.sqlwindow.Constants;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Point;
import org.kitten.core.util.FileUtil;
import org.kitten.core.util.StringUtil;

public class SqlUtil {
	public static String getTableName(String sql) {
		sql = sql.trim().toUpperCase();
		int fromIndex = sql.indexOf("FROM");
		if (fromIndex == -1) {
			return null;
		}

		int whereIndex = sql.indexOf("WHERE");
		if (whereIndex == -1) {
			whereIndex = sql.length();
		}

		String tablePart = sql.substring(fromIndex + 4, whereIndex).trim();
		tablePart = tablePart.replaceAll("\\s{2,}", " ");
		return tablePart.split(" ")[0];
	}

	public static Map<String, String> getAliasMapping(String sql) {
		System.out.println(sql);

		Map<String, String> aliasMapping = new HashMap<String, String>();

		if (StringUtil.isBlank(sql)) {
			return aliasMapping;
		}

		sql = sql.trim().toUpperCase();

		if (sql.startsWith("SELECT") || sql.startsWith("DELETE")) {
			aliasInSelect(sql, aliasMapping);
		} else if (sql.startsWith("UPDATE")) {
			aliasInUpdate(sql, aliasMapping);
		}
		return aliasMapping;
	}

	private static void aliasInUpdate(String sql,
			Map<String, String> aliasMapping) {
		int setIndex = sql.indexOf("SET");
		if (setIndex == -1) {
			setIndex = sql.length();
		}
		String tablePart = sql.substring("UPDATE".length(), setIndex).trim();
		// 去掉AS
		tablePart = tablePart.replaceAll(" AS ", " ");
		// 去掉多余的空格
		tablePart = tablePart.replaceAll("\\s{2,}", " ");
		// ZHANG_S Z, LISHI L,WANGWU
		// 拆分
		String[] tables = tablePart.split(",");
		for (int i = 0; i < tables.length; i++) {
			String str = tables[i].trim();
			int space = str.indexOf(" ");
			if (space != -1) {
				String alias = str.substring(space + 1);
				String table = str.substring(0, space);
				// 如果表名前有schema，去掉schema
				if (table.indexOf(".") != -1) {
					table = table.substring(table.indexOf(".") + 1);
				}
				aliasMapping.put(alias, table);
			}
		}
	}

	private static void aliasInSelect(String sql,
			Map<String, String> aliasMapping) {
		int fromIndex = sql.indexOf("FROM");
		if (fromIndex != -1) {
			int whereIndex = sql.indexOf("WHERE");
			if (whereIndex == -1) {
				whereIndex = sql.length();
			}
			String tablePart = sql.substring(fromIndex + 4, whereIndex).trim();
			// 去掉AS
			tablePart = tablePart.replaceAll(" AS ", " ");
			// 去掉多余的空格
			tablePart = tablePart.replaceAll("\\s{2,}", " ");
			System.out.println("tablePart=" + tablePart);
			// ZHANG_S Z, LISHI L,WANGWU
			// 拆分
			String[] tables = tablePart.split(",");

			for (int i = 0; i < tables.length; i++) {
				String str = tables[i].trim();
				int space = str.indexOf(" ");
				if (space != -1) {
					String alias = str.substring(space + 1);
					String table = str.substring(0, space);

					// 如果表名前有schema，去掉schema
					if (table.indexOf(".") != -1) {
						table = table.substring(table.indexOf(".") + 1);
					}
					aliasMapping.put(alias, table);
				}
			}
		}
	}

	public static void main(String[] args) throws Exception {
		/*
		// 没有where的情况
		String sql = "select p.x from Person p";

		// 有where的情况
		// String sql =
		// "select * from Zhang_s  z, LiShi    as l,Wangwu where z.";
		Map<String, String> map = SqlUtil.getAliasMapping(sql);
		System.out.println(map.keySet());
		System.out.println(map.get("L"));

		System.out.println(SqlUtil.getTableName(sql));
		**/
		//test removeCommentsFromSql
		String input = FileUtil.getFileContent(new File("c:/test.txt"), "UTF-8");
		System.out.println(SqlUtil.removeCommentsFromSql(input));
		
		
		
	}

	public static String getSqlBeforeCursor(StyledText text) {
		// lineNumber是从0开始的.
		int caretOffset = text.getCaretOffset();
		int currentLineNumber = text.getLineAtOffset(text.getCaretOffset());
		// 向前找SQL的开头
		int startLineOffset = getStartLineOffset(text, currentLineNumber);
		String selectionText = text.getText(startLineOffset, caretOffset);
		return selectionText;
	}

	/**
	 * 从SQL语句中移除以--开头的注释，以及/* *\/包裹的注释
	 * @param sql
	 * @return
	 */
	public static String removeCommentsFromSql(String sql){
		//移除单行注释 --
		Pattern single = Pattern.compile("--.*$",Pattern.MULTILINE);
		Matcher m1 = single.matcher(sql);
		sql = m1.replaceAll("");
		
		
		//移除多行注释
		Pattern multi = Pattern.compile("/\\*[^\\*/]*\\*/",Pattern.DOTALL);
		Matcher m2 = multi.matcher(sql);
		return m2.replaceAll("").trim();
	}
	
	
	public static String getWholeSqlBlock(StyledText text) {
		String selectionText;
		// 如果有多个SQL,则只执行光标所在行的SQL
		// note that select.y is the length of the selection
		Point select = text.getSelectionRange();
		// lineNumber是从0开始的.
		int currentLineNumber = text.getLineAtOffset(select.x);

		String currentLineText = text.getLine(currentLineNumber).trim();
		// 又分两种情况，光标所在的行有分号
		if (currentLineText.endsWith(";")) {
			// 向前找SQL的开头
			int startLineOffset = getStartLineOffset(text, currentLineNumber);
			// 先后找SQL的结尾（就是当前行行尾)
			int lineEndOffset = getLineEndOffset(text, currentLineNumber);

			// 注意-1的含义，深入体会HelloLineOffset.java
			selectionText = text.getText(startLineOffset, lineEndOffset - 1);

		} else if (currentLineText.length() == 0) {
			// 光标所在的行是空白行,什么也不做.
			selectionText = "";

		} else {
			// 光标所在的木有分号
			// 向前找SQL的开头
			int startLineOffset = getStartLineOffset(text, currentLineNumber);
			// 向后找 SQL的结尾
			int endLineOffset = getEndLineOffset(text, currentLineNumber);

			System.out.println("startLineOffset=" + startLineOffset);
			System.out.println("endLineOffset=" + endLineOffset);

			// 注意-3的含义，深入体会HelloLineOffset.java
			selectionText = text.getText(startLineOffset, endLineOffset - 3);
		}
		return selectionText;
	}

	private static int getLineEndOffset(StyledText text, int currentLineNumber) {
		return text.getOffsetAtLine(currentLineNumber)
				+ text.getLine(currentLineNumber).length();
	}

	/**
	 * 返回结束行下一行的开头位置.
	 * 
	 * @param text
	 * @param currentLineIndex
	 * @return
	 */
	private static int getEndLineOffset(StyledText text, int currentLineIndex) {
		// 否则找寻下一行
		int result = currentLineIndex;

		int lineCount = text.getLineCount();
		System.out.println("lineCount=" + lineCount);
		System.out.println("currentLineIndex=" + currentLineIndex);
		// 当前已经是最后一行，则将nextLineOffset指向行尾
		if (result >= lineCount - 1) {
			result = lineCount;
		} else {
			while (result < lineCount - 1) {
				result = result + 1;
				String nextLineText = text.getLine(result).trim();
				System.out.println("nextLine=[" + nextLineText + "]");

				// 已经是SQL结束行
				if (nextLineText.endsWith(";")) {
					break;
				} else {
					// 已经是SQL结束行的下一行
					String[] sqlStart = Constants.SQL_START;
					for (int j = 0; j < sqlStart.length; j++) {
						if (nextLineText.startsWith(sqlStart[j])) {
							break;
						}
					}
					
					//如果SQL中混有注释,这些if条件就是错误的
//					if (nextLineText.startsWith("--")
//							|| nextLineText.startsWith("/*")
//							|| nextLineText.endsWith("*/")
//							|| nextLineText.endsWith(";")) {
//						break;
//					}
					if (nextLineText.endsWith(";")) {
						break;
					}
				}
			}
		}

		System.out.println("foundEndLine=" + result);
		// 在下面的情况下，会报异常，因此NND要注意传入的result的范围
		// lineIndex < 0 || lineIndex > 0 && lineIndex >= content.getLineCount()

		// 请看HelloLineOffset的例子
		if (result == lineCount) {
			return text.getOffsetAtLine(result - 1)
					+ text.getLine(result - 1).length()
					+ text.getLineDelimiter().length();
		} else {
			return text.getOffsetAtLine(result);
		}
	}

	/**
	 * 返回开始行的起点位置.
	 * 
	 * @param text
	 * @param currentLineNumber
	 * @return
	 */
	public static int getStartLineOffset(StyledText text, int currentLineNumber) {
		int foundStartLine = currentLineNumber - 1;
		// 光标所在的行有分号
		while (foundStartLine >= 0) {
			String prevLine = text.getLine(foundStartLine).trim().toLowerCase();
			System.out.println("prevLine=[" + prevLine + "]");
			// 已经是SQL开始行的前一行
			String[] sqlStart = Constants.SQL_START;
			for (int j = 0; j < sqlStart.length; j++) {
				if (prevLine.startsWith(sqlStart[j])) {
					break;
				}
			}
			
			//如果SQL中混有注释,这些if条件就是错误的
//			if (prevLine.startsWith("--") || prevLine.startsWith("/*")
//					|| prevLine.endsWith("*/") || prevLine.endsWith(";")) {
				if (prevLine.endsWith(";")) {
				break;
			}

			foundStartLine--;
		}

		int startLineOffset = text.getOffsetAtLine(foundStartLine + 1);
		return startLineOffset;
	}
}
