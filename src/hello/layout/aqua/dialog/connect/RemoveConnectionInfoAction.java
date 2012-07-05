package hello.layout.aqua.dialog.connect;

import hello.layout.aqua.ImageFactory;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

public class RemoveConnectionInfoAction extends Action {
	private ConnectionDialog dialog;

	public RemoveConnectionInfoAction(ConnectionDialog dialog) {
		//FIXME use inner class
		this.dialog = dialog;
		setText("Remove");
		setImageDescriptor(ImageDescriptor.createFromImage(ImageFactory
				.loadImage(ImageFactory.DELETE_CONNECTION)));
	}

	public void run() {
		dialog.removeConnectionInfo();
	}
}
