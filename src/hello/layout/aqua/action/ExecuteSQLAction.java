package hello.layout.aqua.action;

import hello.layout.aqua.CyperDataStudio;
import hello.layout.aqua.ImageFactory;
import hello.layout.aqua.sqlwindow.Constants;
import hello.layout.aqua.sqlwindow.SQLResultModel;
import hello.layout.aqua.sqlwindow.SQLWindow;
import hello.layout.aqua.util.SqlUtil;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Point;

public class ExecuteSQLAction extends Action {
	private CyperDataStudio studio;

	public ExecuteSQLAction(CyperDataStudio studio) {
		this.studio = studio;
		setImageDescriptor(ImageDescriptor.createFromImage(ImageFactory
				.loadImage(ImageFactory.EXECUTE_SQL)));
		setText("Execute");
		setToolTipText("Excute(F8)");
		setAccelerator(SWT.F8);
	}

	@Override
	public void run() {
		SQLWindow sw = CyperDataStudio.getStudio().getSqlWindow();
		CTabItem[] items = sw.getItems();
		// show result window if it's hidden.
		if (items != null && items.length > 0) {
			for (int i = 0; i < items.length; i++) {
				SashForm right = (SashForm) items[i].getControl();
				if (right.getWeights()[1] == 0) {
					right.setWeights(SQLWindow.DEFAULT_WEIGHTS);
				}
			}
			sw.setMaximized(false);
		}

		TextViewer tv = sw.getSourceViewer();
		if (tv!=null) {
			StyledText text = tv.getTextWidget();
			// show result
			CTabItem item = sw.getSelection();
			if (item != null) {
				SQLResultModel model = (SQLResultModel) sw.getTable().getModel();
				// 尝试使用用户选中的文本
				String selectionText = text.getSelectionText().trim();
				// 如果没有选中任何文本，则执行光标所在的SQL
				if (selectionText.length() == 0) {
//					selectionText = text.getText();
					selectionText = SqlUtil.getWholeSqlBlock(text);
				}

				// 对SQL的后处理
				selectionText = selectionText.trim();
				if (selectionText.endsWith(";")) {
					selectionText = selectionText.substring(0,selectionText.length() - 1);
				}
				if (selectionText.length() > 0) {
					model.executeSQL(selectionText);
				}
			}
		}

	}

}
