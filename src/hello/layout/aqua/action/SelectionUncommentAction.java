package hello.layout.aqua.action;

import hello.layout.aqua.CyperDataStudio;
import hello.layout.aqua.ImageFactory;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

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
//		studio.getSqlWindow().getUndoManager().undo();
	}
}
