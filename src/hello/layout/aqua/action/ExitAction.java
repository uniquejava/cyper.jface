package hello.layout.aqua.action;

import hello.layout.aqua.CyperDataStudio;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;

public class ExitAction extends Action {
	public ExitAction() {
		setText("Exit");
		setAccelerator(SWT.CTRL | 'Q');
	}

	@Override
	public void run() {
		CyperDataStudio.getStudio().getShell().dispose();
	}
}
