package hello.layout.aqua.action;

import hello.layout.aqua.CyperDataStudio;
import hello.layout.aqua.ImageFactory;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;

public class TabSaveAction extends Action{
	private CyperDataStudio studio;
	public TabSaveAction(CyperDataStudio studio) {
		this.studio = studio;
		setText("Save Script");
		setAccelerator(SWT.CTRL | 'S');
		setImageDescriptor(ImageDescriptor.createFromImage(ImageFactory
				.loadImage(ImageFactory.SAVE)));
	}

	@Override
	public void run() {
		studio.getSqlWindow().getDocument().save();
	}
	
}
