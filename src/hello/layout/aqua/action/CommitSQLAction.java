package hello.layout.aqua.action;

import hello.layout.aqua.AquaDataStudio;
import hello.layout.aqua.ImageFactory;
import hello.model.PersonFactory;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;

public class CommitSQLAction extends Action {
	private AquaDataStudio studio;

	public CommitSQLAction(AquaDataStudio studio) {
		this.studio = studio;
		setImageDescriptor(ImageDescriptor.createFromImage(ImageFactory
				.loadImage(ImageFactory.COMMIT)));
		setToolTipText("Commit(F10)");
		setAccelerator(SWT.F10);
	}

	@Override
	public void run() {
		System.out.println("commit");
	}
}
