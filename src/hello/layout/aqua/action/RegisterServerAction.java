package hello.layout.aqua.action;

import hello.layout.aqua.CyperDataStudio;
import hello.layout.aqua.ImageFactory;
import hello.layout.aqua.dialog.connect.ConnectionDialog;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;

public class RegisterServerAction extends Action {
	private CyperDataStudio studio;

	public RegisterServerAction(CyperDataStudio studio) {
		this.studio = studio;
		setImageDescriptor(ImageDescriptor.createFromImage(ImageFactory
				.loadImage(Display.getDefault(), ImageFactory.LOGON)));
		setToolTipText("Register Server(Insert)");
	}

	@Override
	public void run() {
		ConnectionDialog dialog;
		try {
			dialog = new ConnectionDialog(
					studio.getShell());
			int ret = dialog.open();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
