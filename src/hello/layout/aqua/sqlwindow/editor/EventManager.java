package hello.layout.aqua.sqlwindow.editor;

import hello.layout.aqua.sqlwindow.SQLWindow;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.FindReplaceDocumentAdapter;
import org.eclipse.jface.text.IRegion;
import org.eclipse.swt.graphics.Point;

public class EventManager {
	private SQLWindow sqlWindow;

	public EventManager(SQLWindow sqlWindow) {
		this.sqlWindow = sqlWindow;
	}

	public boolean howAboutFind(FindReplaceDocumentAdapter findAdapter,
			String findString, boolean forwardSearch, boolean caseSensitive,
			boolean wholeWord, boolean regExSearch) {
		boolean bFind = false;
		IRegion region = null;
		try {
			int startOffset = sqlWindow.getSourceViewer().getTextWidget()
					.getCaretOffset();
			if (!forwardSearch) {
				Point pt = sqlWindow.getSourceViewer().getSelectedRange();
				if (pt.x != pt.y) {
					startOffset = pt.x - 1;
				}
			}
			if (startOffset > findAdapter.length()) {
				startOffset = findAdapter.length() - 1;
			}
			if (startOffset < 0) {
				startOffset = 0;
			}
			region = findAdapter.find(startOffset, findString, forwardSearch,
					caseSensitive, wholeWord, regExSearch);
			if (region != null) {
				sqlWindow.getSourceViewer().setSelectedRange(
						region.getOffset(), region.getLength());
				bFind = true;
			}

		} catch (Exception e) {
		}
		return bFind;
	}

	public void doReplace(FindReplaceDocumentAdapter findAdapter,
			String replaceText, boolean regExReplace) {
		try {
			findAdapter.replace(replaceText, regExReplace);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
}
