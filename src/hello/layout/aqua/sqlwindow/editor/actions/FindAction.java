package hello.layout.aqua.sqlwindow.editor.actions;


import hello.layout.aqua.dialog.FindAndReplace;
import hello.layout.aqua.sqlwindow.SQLWindow;

import org.eclipse.jface.action.Action;

public class FindAction extends Action {
	private SQLWindow sqlWindow;

	public FindAction(SQLWindow sqlWindow) {
		super("Find@Ctrl+F");
		this.sqlWindow = sqlWindow;
	}
	
	@Override
	public void run() {
		new FindAndReplace(sqlWindow, sqlWindow.getShell()).open();
	}
}
