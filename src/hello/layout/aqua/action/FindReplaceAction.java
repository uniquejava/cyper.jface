package hello.layout.aqua.action;


import hello.layout.aqua.CyperDataStudio;
import hello.layout.aqua.ImageFactory;
import hello.layout.aqua.dialog.FindAndReplace;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;

public class FindReplaceAction extends Action {
	private CyperDataStudio studio;

	public FindReplaceAction(CyperDataStudio studio) {
		this.studio = studio;
		setText("Find & Replace...");
		setToolTipText("Find & Replace(Ctrl+F)");
		setImageDescriptor(ImageDescriptor.createFromImage(ImageFactory
				.loadImage(ImageFactory.FIND_REPLACE)));
		setAccelerator(SWT.CTRL | 'F');
	}
	
	@Override
	public void run() {
		if (studio.getSqlWindow().getSelectionIndex()!=-1) {
			new FindAndReplace(studio, studio.getShell()).open();
		}
	}
}
