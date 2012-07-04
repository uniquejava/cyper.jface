package hello.layout.aqua.action;

import hello.layout.aqua.CyperDataStudio;
import hello.layout.aqua.ImageFactory;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

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
		// studio.getSqlWindow().getUndoManager().undo();
	}
}
