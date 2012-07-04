package hello.layout.aqua.action;

import hello.layout.aqua.CyperDataStudio;
import hello.layout.aqua.ImageFactory;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;

public class TextCutAction extends Action {
	private CyperDataStudio studio;

	public TextCutAction(CyperDataStudio studio) {
		this.studio = studio;
		setText("Cut");
		setToolTipText("Cut(Ctrl+X)");
		setImageDescriptor(ImageDescriptor.createFromImage(ImageFactory
				.loadImage(ImageFactory.CUT)));
		setAccelerator(SWT.CTRL | 'X');
	}

	@Override
	public void run() {
		studio.getSqlWindow().getSourceViewer().getTextWidget().cut();
	}
}
