package hello.layout.aqua.action;

import hello.layout.aqua.CyperDataStudio;
import hello.layout.aqua.sqlwindow.SQLWindow;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;

public class TabCloseAction extends Action {
	public TabCloseAction() {
		setText("Close Tab");
		setAccelerator(SWT.CTRL | 'W');
	}

	@Override
	public void run() {
		SQLWindow window = CyperDataStudio.getStudio().getSqlWindow();
		if (window.getSelection() != null) {
			window.getSelection().dispose();
		}
	}
}
