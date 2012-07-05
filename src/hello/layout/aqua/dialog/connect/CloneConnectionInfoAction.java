package hello.layout.aqua.dialog.connect;

import hello.layout.aqua.ImageFactory;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

public class CloneConnectionInfoAction extends Action {

	private ConnectionDialog dialog;

	public CloneConnectionInfoAction(ConnectionDialog dialog) {
		//FIXME use inner class
		this.dialog = dialog;
		setText("Clone");
		setImageDescriptor(ImageDescriptor.createFromImage(ImageFactory
				.loadImage(ImageFactory.NEW_CONNECTION)));
	}

	public void run() {
		dialog.cloneConnectionInfo();
	}
}
