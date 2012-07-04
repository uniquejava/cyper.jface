package hello.layout.aqua.action;

import hello.layout.aqua.CyperDataStudio;
import hello.layout.aqua.ImageFactory;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;

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
		// studio.getSqlWindow().getUndoManager().undo();
	}
}
