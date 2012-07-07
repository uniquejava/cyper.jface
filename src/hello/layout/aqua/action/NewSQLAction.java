package hello.layout.aqua.action;

import hello.layout.aqua.CyperDataStudio;
import hello.layout.aqua.ImageFactory;
import hello.layout.aqua.sqlwindow.SQLWindow;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;

public class NewSQLAction extends Action {
	private CyperDataStudio studio;

	public NewSQLAction(CyperDataStudio studio) {
		this.studio = studio;
		setImageDescriptor(ImageDescriptor.createFromImage(ImageFactory
				.loadImage(ImageFactory.NEW_SQL)));
		setText("New SQL");
		setToolTipText("New(Ctrl+N)");
		// 这里的setAccelerator有啥用？为什么还要在shell上addKeyListener才行。
		// 最终发现action只有放到了file menu上，快捷键才会激活.
		setAccelerator(SWT.CTRL | 'N');
	}

	@Override
	public void run() {
		try {
			SQLWindow sw = CyperDataStudio.getStudio().getSqlWindow();
			sw.createNewTabItem("New SQL_" + (sw.seq++), true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
