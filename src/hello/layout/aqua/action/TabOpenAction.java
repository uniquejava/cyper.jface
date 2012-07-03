package hello.layout.aqua.action;

import java.io.File;

import hello.layout.aqua.CyperDataStudio;
import hello.layout.aqua.ImageFactory;
import hello.layout.aqua.sqlwindow.SQLWindow;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;

public class TabOpenAction extends Action {
	private CyperDataStudio studio;

	public TabOpenAction(CyperDataStudio studio) {
		this.studio = studio;
		setImageDescriptor(ImageDescriptor.createFromImage(ImageFactory
				.loadImage(ImageFactory.OPEN)));
		setText("Open SQL");
		setToolTipText("Open(Ctrl+O)");
		//这里的setAccelerator有啥用？为什么还要在shell上addKeyListener才行。
		//最终发现action只有放到了file menu上，快捷键才会激活.
		setAccelerator(SWT.CTRL | 'O');
	}

	@Override
	public void run() {
		try {
				FileDialog dialog = new FileDialog(studio.getShell(), SWT.OPEN);
				dialog.setFilterExtensions(new String[] { "*.sql", "*.*" });
				String fileName = dialog.open();
				if (fileName == null || fileName.length() == 0) {
					return;
				}
				SQLWindow sw = CyperDataStudio.getStudio().getSqlWindow();
				sw.createNewTabItem(new File(fileName).getName(),true);
				
				sw.getDocument().open(fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
