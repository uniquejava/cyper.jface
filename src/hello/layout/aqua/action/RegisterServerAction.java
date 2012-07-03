package hello.layout.aqua.action;

import static hello.layout.aqua.ImageFactory.REGISTER_SERVER;
import hello.layout.aqua.CyperDataStudio;
import hello.layout.aqua.ImageFactory;
import hello.layout.aqua.dialog.RegisterServerDialog;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;

public class RegisterServerAction extends Action {
	private CyperDataStudio studio;

	public RegisterServerAction(CyperDataStudio studio) {
		this.studio = studio;
		setImageDescriptor(ImageDescriptor.createFromImage(ImageFactory
				.loadImage(Display.getDefault(), REGISTER_SERVER)));
		setToolTipText("Register Server(Insert)");
	}

	@Override
	public void run() {
		RegisterServerDialog dialog = new RegisterServerDialog(
				studio.getShell());
		int ret = dialog.open();
		if (ret == SWT.OK) {

		}

	}

}
