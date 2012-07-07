package hello.layout.aqua.action;

import hello.layout.aqua.CyperDataStudio;
import hello.layout.aqua.ImageFactory;
import hello.layout.aqua.sqlwindow.Constants;
import hello.layout.aqua.sqlwindow.SQLResultModel;
import hello.layout.aqua.sqlwindow.SQLWindow;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Point;

public class ExecuteSQLAction extends Action {
	private CyperDataStudio studio;

	public ExecuteSQLAction(CyperDataStudio studio) {
		this.studio = studio;
		setImageDescriptor(ImageDescriptor.createFromImage(ImageFactory
				.loadImage(ImageFactory.EXECUTE_SQL)));
		setText("Execute");
		setToolTipText("Excute(F8)");
		setAccelerator(SWT.F8);
	}

	@Override
	public void run() {
		SQLWindow sw = CyperDataStudio.getStudio().getSqlWindow();
		CTabItem[] items = sw.getItems();
		// show result window if it's hidden.
		if (items != null && items.length > 0) {
			for (int i = 0; i < items.length; i++) {
				SashForm right = (SashForm) items[i].getControl();
				if (right.getWeights()[1] == 0) {
					right.setWeights(SQLWindow.DEFAULT_WEIGHTS);
				}
			}
			sw.setMaximized(false);
		}

		int folderIndex = sw.getSelectionIndex();
		SourceViewer sourceViewer = sw.textViewerList.get(folderIndex);
		StyledText text = sourceViewer.getTextWidget();
		// show result
		CTabItem item = sw.getSelection();
		if (item != null) {
			SQLResultModel model = (SQLResultModel) sw.tableList.get(folderIndex).getModel();
			// 尝试使用用户选中的文本
			String selectionText = text.getSelectionText().trim();
			// 如果没有选中任何文本，则执行光标所在的SQL
			if (selectionText.length() == 0) {
				selectionText = text.getText();
				// 如果有多个SQL,则只执行光标所在行的SQL
				// note that select.y is the length of the selection
				int lineCount = text.getLineCount();
				Point select = text.getSelectionRange();
				//lineNumber是从0开始的.
				int currentLineNumber = text.getLineAtOffset(select.x);
				
				String currentLineText = text.getLine(currentLineNumber).trim();
				// 又分两种情况，光标所在的行有分号
				if (currentLineText.endsWith(";")) {
					// 向前找SQL的开头
					int startLineOffset = getStartLineOffset(text,currentLineNumber);
					//先后找SQL的结尾（就是当前行行尾)
					int lineEndOffset = getLineEndOffset(text, currentLineNumber);
					
					selectionText = text.getText(startLineOffset,lineEndOffset);
					
				} else if (currentLineText.length() == 0) {
					// 光标所在的行是空白行,什么也不做.
					selectionText = "";
					
				} else {
					// 光标所在的木有分号
					// 向前找SQL的开头
					int startLineOffset = getStartLineOffset(text,currentLineNumber);
					// 向后找 SQL的结尾
					int endLineOffset = getEndLineOffset(text,currentLineNumber);
					
					selectionText = text.getText(startLineOffset,endLineOffset);
				}
			}

			// 对SQL的后处理
			selectionText = selectionText.trim();
			if (selectionText.endsWith(";")) {
				selectionText = selectionText.substring(0,selectionText.length() - 1);
			}
			if (selectionText.length() > 0) {
				model.executeSQL(selectionText);
			}
		}

	}

	private int getLineEndOffset(StyledText text, int currentLineNumber) {
		return text.getOffsetAtLine(currentLineNumber) + text.getLine(currentLineNumber).length()-1;
	}

	/**
	 * 返回结束行下一行的开头位置.
	 * 
	 * @param text
	 * @param currentLineNumber
	 * @return
	 */
	private int getEndLineOffset(StyledText text, int currentLineNumber) {
		int lineCount = text.getLineCount();
		//当前已经是最后一行，则将nextLineOffset指向行尾
		if (currentLineNumber == lineCount-1) {
			return getLineEndOffset(text, currentLineNumber);
		}
		
		//否则找寻下一行
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
	public int getStartLineOffset(StyledText text, int currentLineNumber) {
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
