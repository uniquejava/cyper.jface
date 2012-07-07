package hello.layout.aqua.action;

import hello.layout.aqua.CyperDataStudio;
import hello.layout.aqua.ImageFactory;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;

public class CommitSQLAction extends Action {
	private CyperDataStudio studio;

	public CommitSQLAction(CyperDataStudio studio) {
		this.studio = studio;
		setImageDescriptor(ImageDescriptor.createFromImage(ImageFactory
				.loadImage(ImageFactory.COMMIT)));
		setText("Commit");
		setToolTipText("Commit(F10)");
		setAccelerator(SWT.F10);
	}

	@Override
	public void run() {
		System.out.println("commit");
	}
}
