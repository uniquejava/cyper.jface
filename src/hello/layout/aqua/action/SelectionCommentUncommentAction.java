package hello.layout.aqua.action;

import javax.sound.sampled.Line;

import hello.layout.aqua.CyperDataStudio;
import hello.layout.aqua.ImageFactory;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Point;

public class SelectionCommentUncommentAction extends Action {
	private CyperDataStudio studio;

	public SelectionCommentUncommentAction(CyperDataStudio studio) {
		this.studio = studio;
		setText("Comment/Uncomment");
		setToolTipText("Comment single line(Ctrl+/)");
		setAccelerator(SWT.CTRL | '/');
	}

	@Override
	public void run() {
		StyledText text = studio.getSqlWindow().getSourceViewer().getTextWidget();
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
