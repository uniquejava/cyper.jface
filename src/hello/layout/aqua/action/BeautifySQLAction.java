package hello.layout.aqua.action;

import hello.layout.aqua.CyperDataStudio;
import hello.layout.aqua.ImageFactory;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

public class BeautifySQLAction extends Action {
	private CyperDataStudio studio;

	public BeautifySQLAction(CyperDataStudio studio) {
		this.studio = studio;
		setText("PL/SQL Beautifier");
		setToolTipText("PL/SQL Beautifier");
		setImageDescriptor(ImageDescriptor.createFromImage(ImageFactory
				.loadImage(ImageFactory.BEAUTIFIER)));
	}

	@Override
	public void run() {
		// studio.getSqlWindow().getUndoManager().redo();
	}
}
