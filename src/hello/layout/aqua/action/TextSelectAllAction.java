package hello.layout.aqua.action;

import hello.layout.aqua.CyperDataStudio;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.swt.SWT;

public class TextSelectAllAction extends Action {
	private CyperDataStudio studio;

	public TextSelectAllAction(CyperDataStudio studio) {
		this.studio = studio;
		setText("Select All");
		setAccelerator(SWT.CTRL | 'A');
	}

	@Override
	public void run() {
		TextViewer tv = studio.getSqlWindow().getSourceViewer();
		if (tv != null) {
			tv.getTextWidget().selectAll();
		}
	}
}
