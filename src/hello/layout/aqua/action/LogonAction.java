package hello.layout.aqua.action;

import hello.layout.aqua.CyperDataStudio;
import hello.layout.aqua.ImageFactory;
import hello.layout.aqua.dialog.LogonDialog;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;

public class LogonAction extends Action {
	private CyperDataStudio studio;

	public LogonAction(CyperDataStudio studio) {
		this.studio = studio;
		setImageDescriptor(ImageDescriptor.createFromImage(ImageFactory
				.loadImage(Display.getDefault(), ImageFactory.LOGON)));
		setToolTipText("Log on...");
	}

	@Override
	public void run() {
		// show logon dialog
		LogonDialog logonDialog = new LogonDialog(null);
		int ret = logonDialog.open();

		// not SWT.OK!
		if(ret == Window.OK){
			studio.refreshServerTree();
		} else {
			// do nothing
		}

	}

}
