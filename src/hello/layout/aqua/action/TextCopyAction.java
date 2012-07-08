package hello.layout.aqua.action;

import hello.layout.aqua.CyperDataStudio;
import hello.layout.aqua.ImageFactory;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.swt.SWT;

public class TextCopyAction extends Action {
	private CyperDataStudio studio;

	public TextCopyAction(CyperDataStudio studio) {
		this.studio = studio;
		setText("Copy");
		setToolTipText("Copy(Ctrl+C)");
		setImageDescriptor(ImageDescriptor.createFromImage(ImageFactory
				.loadImage(ImageFactory.COPY)));
		setAccelerator(SWT.CTRL | 'C');
	}

	@Override
	public void run() {
		TextViewer tv = studio.getSqlWindow().getSourceViewer();
		if (tv != null) {
			tv.getTextWidget().copy();
		}
	}
}
