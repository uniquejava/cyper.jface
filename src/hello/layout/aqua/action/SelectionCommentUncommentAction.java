package hello.layout.aqua.action;

import hello.layout.aqua.CyperDataStudio;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Point;

public class SelectionCommentUncommentAction extends Action {
	private CyperDataStudio studio;

	public SelectionCommentUncommentAction(CyperDataStudio studio) {
		this.studio = studio;
		setText("Auto comment");
		setToolTipText("Comment single line(Ctrl+/)");
		setAccelerator(SWT.CTRL | '/');
	}

	@Override
	public void run() {
		TextViewer tv = studio.getSqlWindow().getSourceViewer();
		if (tv!=null) {
			StyledText text = tv.getTextWidget();
			Point range = text.getSelectionRange();
			int startLine = text.getLineAtOffset(range.x);
			int endLine = text.getLineAtOffset(range.x + range.y);
			
			for (int lineNumber = startLine; lineNumber <= endLine; lineNumber++) {
				int offset = text.getOffsetAtLine(lineNumber);
				String lineText = text.getLine(lineNumber);
				if (lineText.trim().startsWith("--")) {
					//取消注释
					text.replaceTextRange(offset, lineText.length(), lineText.replace("--", ""));
				}else{
					//注释
					text.replaceTextRange(offset, lineText.length(), "--"+lineText);
				}
			}
		}
	}
}
