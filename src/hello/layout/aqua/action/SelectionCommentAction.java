package hello.layout.aqua.action;

import hello.layout.aqua.CyperDataStudio;
import hello.layout.aqua.ImageFactory;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Point;

public class SelectionCommentAction extends Action {
	private CyperDataStudio studio;

	public SelectionCommentAction(CyperDataStudio studio) {
		this.studio = studio;
		setText("Comment");
		setToolTipText("Selection Comment");
		setImageDescriptor(ImageDescriptor.createFromImage(ImageFactory
				.loadImage(ImageFactory.COMMENT)));
	}

	@Override
	public void run() {
		StyledText text = studio.getSqlWindow().getSourceViewer()
				.getTextWidget();
		String selectionText = text.getSelectionText();
		Point range = text.getSelectionRange();
		if (selectionText.trim().length() > 0) {
			if (!selectionText.startsWith("/*")&& !selectionText.endsWith("*/")) {
				text.replaceTextRange(range.x, range.y, "/*" + selectionText+ "*/");
			}
		}
	}
}
