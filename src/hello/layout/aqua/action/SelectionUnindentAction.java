package hello.layout.aqua.action;

import hello.layout.aqua.CyperDataStudio;
import hello.layout.aqua.ImageFactory;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;

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
//		studio.getSqlWindow().getUndoManager().undo();
	}
}
