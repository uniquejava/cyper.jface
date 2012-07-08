package hello.layout.aqua.action;

import hello.layout.aqua.CyperDataStudio;
import hello.layout.aqua.ImageFactory;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.text.IUndoManager;
import org.eclipse.swt.SWT;

public class TextUndoAction extends Action {
	private CyperDataStudio studio;

	public TextUndoAction(CyperDataStudio studio) {
		this.studio = studio;
		setText("Undo");
		setToolTipText("Undo(Ctrl+Z)");
		setImageDescriptor(ImageDescriptor.createFromImage(ImageFactory
				.loadImage(ImageFactory.UNDO)));
		setAccelerator(SWT.CTRL | 'Z');
	}

	@Override
	public void run() {
		IUndoManager mgr = studio.getSqlWindow().getUndoManager();
		if (mgr!=null) {
			mgr.undo();
		}
	}
}
