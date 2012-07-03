package hello.layout.aqua.sqlwindow.editor.actions;

import hello.layout.aqua.sqlwindow.SQLWindow;

import org.eclipse.jface.action.Action;

public class UndoAction extends Action {
	private SQLWindow sqlWindow;

	public UndoAction(SQLWindow sqlWindow) {
		super("Undo@Ctrl+Z");
		this.sqlWindow = sqlWindow;
	}

	@Override
	public void run() {
		sqlWindow.getUndoManager().undo();
	}
}
