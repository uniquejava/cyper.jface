package hello.layout.aqua.action;

import hello.layout.aqua.CyperDataStudio;
import hello.layout.aqua.ImageFactory;
import hello.layout.aqua.sqlwindow.Constants;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Point;

public class SelectionUnindentAction extends Action {
	private CyperDataStudio studio;

	public SelectionUnindentAction(CyperDataStudio studio) {
		this.studio = studio;
		setText("Unindent");
		setToolTipText("Selection Unindent(Ctrl+U)");
		setImageDescriptor(ImageDescriptor.createFromImage(ImageFactory
				.loadImage(ImageFactory.UNINDENT)));
		setAccelerator(SWT.CTRL | 'U');
	}

	@Override
	public void run() {
		TextViewer tv = studio.getSqlWindow().getSourceViewer();
		if (tv != null) {
			StyledText text = tv.getTextWidget();
			Point range = text.getSelectionRange();
			int startLine = text.getLineAtOffset(range.x);
			int endLine = text.getLineAtOffset(range.x + range.y);

			int firstLineSpaceCount = 0;
			int otherLineTotalSpaceCount = 0;
			for (int lineNumber = startLine; lineNumber <= endLine; lineNumber++) {
				// 模拟向左缩进两个空格.
				int offset = text.getOffsetAtLine(lineNumber);
				String lineText = text.getLine(lineNumber);

				int spaceCount = 0;
				if (lineText.startsWith(Constants.TAB_SPACE)) {
					spaceCount = Constants.TAB_SPACE.length();
					// 不足一个TAB,则缩进到顶头
				} else if (lineText.startsWith(" ")) {
					// 计算开头空格的个数
					int i = 0;
					while (lineText.charAt(i) == ' ') {
						i++;
						spaceCount++;
					}
				}
				if (spaceCount > 0) {
					text.replaceTextRange(offset, lineText.length(),
							lineText.substring(spaceCount));
				}
				// 如果是第一次循环（第一行）
				if (lineNumber == startLine) {
					firstLineSpaceCount = spaceCount;
				} else {
					otherLineTotalSpaceCount += spaceCount;
				}
			}

			// 缩进后，先前选中的内容要继续被选中.
			int startOffset = range.x - firstLineSpaceCount;
			if (startOffset < 0) {
				startOffset = 0;
			}

			int length = range.y - otherLineTotalSpaceCount;
			text.setSelectionRange(startOffset, length);
		}
	}
}
