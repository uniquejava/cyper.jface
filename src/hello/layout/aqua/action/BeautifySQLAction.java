package hello.layout.aqua.action;

import hello.layout.aqua.CyperDataStudio;
import hello.layout.aqua.ImageFactory;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.hibernate.engine.jdbc.internal.BasicFormatterImpl;

public class BeautifySQLAction extends Action {
	private CyperDataStudio studio;

	public BeautifySQLAction(CyperDataStudio studio) {
		this.studio = studio;
		setText("PL/SQL Beautifier");
		setToolTipText("PL/SQL Beautifier(Ctrl+Shift+F)");
		setAccelerator(SWT.CTRL | SWT.SHIFT | 'F');
		setImageDescriptor(ImageDescriptor.createFromImage(ImageFactory
				.loadImage(ImageFactory.BEAUTIFIER)));
	}

	@Override
	public void run() {
		TextViewer tv = studio.getSqlWindow().getSourceViewer();
		if (tv != null) {
			StyledText text = studio.getSqlWindow().getSourceViewer()
					.getTextWidget();
			String selectionText = text.getSelectionText();
			if (selectionText.trim().length() > 0) {
				text.insert(new BasicFormatterImpl().format(selectionText));
			}
		}
	}
}
