package hello.layout.aqua.action;

import hello.layout.aqua.CyperDataStudio;
import hello.layout.aqua.ImageFactory;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Point;

public class SelectionUncommentAction extends Action {
	private CyperDataStudio studio;

	public SelectionUncommentAction(CyperDataStudio studio) {
		this.studio = studio;
		setText("Uncomment");
		setToolTipText("Selection Uncomment");
		setImageDescriptor(ImageDescriptor.createFromImage(ImageFactory
				.loadImage(ImageFactory.UNCOMMENT)));
	}

	@Override
	public void run() {
		TextViewer tv = studio.getSqlWindow().getSourceViewer();
		if (tv!=null) {
			StyledText text = tv.getTextWidget();
			String selectionText = text.getSelectionText().trim();
			Point range = text.getSelectionRange();
			if (selectionText.startsWith("/*") && selectionText.endsWith("*/")) {
				String trimedText = selectionText.substring(2);
				trimedText = trimedText.substring(0, trimedText.length() - 2);
				text.replaceTextRange(range.x, range.y, trimedText);
			}
		}
	
	}
}
