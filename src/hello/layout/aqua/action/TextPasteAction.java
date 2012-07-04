package hello.layout.aqua.action;

import hello.layout.aqua.CyperDataStudio;
import hello.layout.aqua.ImageFactory;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;

public class TextPasteAction extends Action {
	private CyperDataStudio studio;

	public TextPasteAction(CyperDataStudio studio) {
		this.studio = studio;
		setText("Paste");
		setToolTipText("Paste(Ctrl+V)");
		setImageDescriptor(ImageDescriptor.createFromImage(ImageFactory
				.loadImage(ImageFactory.PASTE)));
		setAccelerator(SWT.CTRL | 'V');
	}

	@Override
	public void run() {
		studio.getSqlWindow().getSourceViewer().getTextWidget().paste();
	}
}
