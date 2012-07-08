package hello.layout.aqua.action;

import hello.layout.aqua.CyperDataStudio;
import hello.layout.aqua.ImageFactory;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.text.IUndoManager;
import org.eclipse.swt.SWT;

public class TextRedoAction extends Action {
	private CyperDataStudio studio;

	public TextRedoAction(CyperDataStudio studio) {
		this.studio = studio;
		setText("Redo");
		setToolTipText("Redo(Ctrl+Y)"); 
		setImageDescriptor(ImageDescriptor.createFromImage(ImageFactory
				.loadImage(ImageFactory.REDO)));
		setAccelerator(SWT.CTRL | 'Y');
	}

	@Override
	public void run() {
		IUndoManager mgr = studio.getSqlWindow().getUndoManager();
		if (mgr!=null) {
			mgr.redo();
		}
	}
}
