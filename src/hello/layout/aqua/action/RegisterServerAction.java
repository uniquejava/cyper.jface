package hello.layout.aqua.action;

import static hello.layout.aqua.ImageFactory.REGISTER_SERVER;
import hello.layout.aqua.ImageFactory;
import hello.layout.aqua.dialog.RegisterServerDialog;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;

public class RegisterServerAction extends Action {
	private Shell shell;

	public RegisterServerAction(final Shell shell) {
		this.shell = shell;
		setImageDescriptor(ImageDescriptor.createFromImage(ImageFactory
				.loadImage(shell.getDisplay(), REGISTER_SERVER)));
		setToolTipText("Register Server(Insert)");
	}

	@Override
	public void run() {
		RegisterServerDialog dialog = new RegisterServerDialog(shell);
		int ret = dialog.open();
		if (ret == SWT.OK) {
			
		}

	}

}
