package hello.layout.aqua.sqlwindow.editor.actions;

import hello.layout.aqua.sqlwindow.SQLWindow;

import org.eclipse.jface.action.Action;

public class RedoAction extends Action {
	private SQLWindow sqlWindow;

	public RedoAction(SQLWindow sqlWindow) {
		super("Redo@Ctrl+Y");
		this.sqlWindow = sqlWindow;
	}

	@Override
	public void run() {
		sqlWindow.getUndoManager().redo();
	}
}
