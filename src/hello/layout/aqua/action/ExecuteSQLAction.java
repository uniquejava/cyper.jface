package hello.layout.aqua.action;

import hello.example.ktable.dao.MyDao;
import hello.layout.aqua.CyperDataStudio;
import hello.layout.aqua.ImageFactory;
import hello.layout.aqua.dialog.OopsDialog;
import hello.layout.aqua.sqlwindow.Constants;
import hello.layout.aqua.sqlwindow.SQLResultModel;
import hello.layout.aqua.sqlwindow.SQLWindow;
import hello.layout.aqua.util.SqlUtil;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyledText;
import org.kitten.core.util.ErrorUtil;

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
		if (tv != null) {
			StyledText text = tv.getTextWidget();
			// show result
			CTabItem item = sw.getSelection();
			if (item != null) {
				SQLResultModel model = (SQLResultModel) sw.getTable()
						.getModel();
				// 尝试使用用户选中的文本
				String selectionText = text.getSelectionText().trim();

				// 如果没有选中任何文本，则执行光标所在的SQL
				if (selectionText.length() == 0) {
					selectionText = SqlUtil.getWholeSqlBlock(text);
				}

				// 对SQL的后处理
				selectionText = SqlUtil.removeCommentsFromSql(selectionText);
				selectionText = selectionText.trim();
				if (selectionText.endsWith(";")) {
					selectionText = selectionText.substring(0,
							selectionText.length() - 1);
				}

				if (selectionText.length() > 0) {
					if (selectionText.toLowerCase().startsWith("delete")
							|| selectionText.toLowerCase().startsWith("update")
							|| selectionText.toLowerCase().startsWith("insert")) {
						handleDDL(selectionText);
					} else {
						handleSelect(text, model, selectionText);

					}
				}
			}
		}

	}

	private void handleSelect(StyledText text, SQLResultModel model,
			String selectionText) {
		try {
			model.executeSQL(selectionText);
		} catch (Exception e) {
			String msg = ErrorUtil.getError(e);
			MessageDialog.openError(studio.getShell(), "Oops", msg);
		}
	}

	private void handleDDL(String selectionText) {
		// 执行ddl时要提示是否真的update,delete防止误操作！！！！！
		boolean go = MessageDialog.openConfirm(studio.getShell(),
				Constants.PRODUCT_NAME, "are you sure?");
		if (!go) {
			return;
		}
		try {
			String[] sqls = selectionText.split(";");
			int rowsAffected = 0;
			if (sqls.length > 1) {
				rowsAffected = new MyDao().executeBatch(selectionText
						.split(";"));
			} else {
				rowsAffected = new MyDao().executeUpdate(sqls[0]);
			}
			MessageDialog.openInformation(studio.getShell(), "Congrats",
					rowsAffected + " row(s) affected.");
		} catch (Exception e) {
			// String msg = ErrorUtil.getError(e);
			OopsDialog od = new OopsDialog(studio.getShell(), e);
			od.open();
		}
	}

}
