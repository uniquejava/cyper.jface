package hello.layout.aqua.util;

import hello.layout.aqua.sqlwindow.Constants;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Point;
import org.kitten.core.util.StringUtil;

public class SqlUtil {
	public static Map<String, String> getAliasMapping(String sql) {
		System.out.println(sql);
		
		Map<String, String> aliasMapping = new HashMap<String, String>();
		
		if (StringUtil.isBlank(sql)) {
			return aliasMapping;
		}

		sql = sql.trim().toUpperCase();
		int fromIndex = sql.indexOf("FROM");

		if (fromIndex == -1) {
			return aliasMapping;
		}

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
				
				//如果表名前有schema，去掉schema
				if (table.indexOf(".")!=-1) {
					table = table.substring(table.indexOf(".")+1);
				}
				aliasMapping.put(alias, table);
			}
		}
		
		return aliasMapping;
	}

	public static void main(String[] args) {
		// 没有where的情况
		 String sql = "select p. from Person p";

		// 有where的情况
//		String sql = "select * from Zhang_s  z, LiShi    as l,Wangwu where z.";
		Map<String, String> map = SqlUtil.getAliasMapping(sql);
		System.out.println(map.keySet());
		System.out.println(map.get("L"));
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

			selectionText = text.getText(startLineOffset, lineEndOffset);

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
			
			//-1，让光标从最后一行的行首移到前一行的行尾。。
			selectionText = text.getText(startLineOffset, endLineOffset-1);
		}
		return selectionText;
	}

	private static int getLineEndOffset(StyledText text, int currentLineNumber) {
		return text.getOffsetAtLine(currentLineNumber)
				+ text.getLine(currentLineNumber).length() - 1;
	}

	/**
	 * 返回结束行下一行的开头位置.
	 * 
	 * @param text
	 * @param currentLineNumber
	 * @return
	 */
	private static int getEndLineOffset(StyledText text, int currentLineNumber) {
		int lineCount = text.getLineCount();
		System.out.println("lineCount=" + lineCount);
		System.out.println("currentLineNumber=" + currentLineNumber);
		// 当前已经是最后一行，则将nextLineOffset指向行尾
		if (currentLineNumber == lineCount - 1) {
			return getLineEndOffset(text, currentLineNumber);
		}

		// 否则找寻下一行
		int foundEndLine = currentLineNumber + 1;
		while (foundEndLine < lineCount) {
			String nextLine = text.getLine(foundEndLine).trim();
			System.out.println("nextLine=[" + nextLine + "]");
			// 已经是SQL结束行
			if (nextLine.endsWith(";")) {
				foundEndLine = foundEndLine + 1;
				break;
			} else {
				// 已经是SQL结束行的下一行
				String[] sqlStart = Constants.SQL_START;
				for (int j = 0; j < sqlStart.length; j++) {
					if (nextLine.startsWith(sqlStart[j])) {
						break;
					}
				}
				if (nextLine.startsWith("--") || nextLine.startsWith("/*")
						|| nextLine.endsWith("*/") || nextLine.endsWith(";")) {
					break;
				}
			}
			foundEndLine++;
		}
		
		System.out.println("foundEndLine=" + foundEndLine);
		// foundEndLine已经是最后一行，则将nextLineOffset指向行尾
		//foundEndLine是从0开始的，而lineCount从1开始的	
		if (foundEndLine >= lineCount ) {
			foundEndLine = lineCount-1;
		}
		System.out.println("foundEndLine=" + foundEndLine);
		int endLineOffset = text.getOffsetAtLine(foundEndLine);
		return endLineOffset;
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
			if (prevLine.startsWith("--") || prevLine.startsWith("/*")
					|| prevLine.endsWith("*/") || prevLine.endsWith(";")) {
				break;
			}

			foundStartLine--;
		}

		int startLineOffset = text.getOffsetAtLine(foundStartLine + 1);
		return startLineOffset;
	}
}
