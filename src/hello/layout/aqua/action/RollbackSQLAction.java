package hello.layout.aqua.action;

import hello.layout.aqua.CyperDataStudio;
import hello.layout.aqua.ImageFactory;
import hello.model.PersonFactory;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;

public class RollbackSQLAction extends Action {
	private CyperDataStudio studio;

	public RollbackSQLAction(CyperDataStudio studio) {
		this.studio = studio;
		setImageDescriptor(ImageDescriptor.createFromImage(ImageFactory
				.loadImage(ImageFactory.ROLLBACK)));
		setText("Rollback");
		setToolTipText("Rollback(Shift+F10)");
		setAccelerator(SWT.SHIFT + SWT.F10);
	}

	@Override
	public void run() {
		System.out.println("rollback");
	}
}
