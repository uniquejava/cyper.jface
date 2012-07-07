package hello.layout.aqua.action;

import hello.layout.aqua.CyperDataStudio;
import hello.layout.aqua.ImageFactory;
import hello.layout.aqua.sqlwindow.Constants;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Point;

public class SelectionIndentAction extends Action {
	private CyperDataStudio studio;

	public SelectionIndentAction(CyperDataStudio studio) {
		this.studio = studio;
		setText("Indent");
		setToolTipText("Selection Indent(Ctrl+I)");
		setImageDescriptor(ImageDescriptor.createFromImage(ImageFactory
				.loadImage(ImageFactory.INDENT)));
		setAccelerator(SWT.CTRL | 'I');
	}

	@Override
	public void run() {
		StyledText text = studio.getSqlWindow().getSourceViewer().getTextWidget();
		Point range = text.getSelectionRange();
		int startLine = text.getLineAtOffset(range.x);
		int endLine = text.getLineAtOffset(range.x + range.y);
		for (int lineNumber = startLine; lineNumber <= endLine; lineNumber++) {
			// 模拟向右缩进两个空格.
			int offset = text.getOffsetAtLine(lineNumber);
			String lineText = text.getLine(lineNumber);
			text.replaceTextRange(offset, lineText.length(),
					Constants.TAB_SPACE + lineText);
		}
		// 缩进后，先前选中的内容要继续被选中.
		int selectedLineCount = endLine - startLine + 1;
		int startOffset = range.x + Constants.TAB_SPACE.length();
		int length = range.y + (selectedLineCount - 1)* Constants.TAB_SPACE.length();
		text.setSelectionRange(startOffset, length);
	}
}
