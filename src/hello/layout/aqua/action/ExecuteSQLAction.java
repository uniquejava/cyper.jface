package hello.layout.aqua.action;

import hello.layout.aqua.CyperDataStudio;
import hello.layout.aqua.ImageFactory;
import hello.layout.aqua.sqlwindow.SQLResultModel;
import hello.layout.aqua.sqlwindow.SQLWindow;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;

public class ExecuteSQLAction extends Action {
	private CyperDataStudio studio;

	public ExecuteSQLAction(CyperDataStudio studio) {
		this.studio = studio;
		setImageDescriptor(ImageDescriptor.createFromImage(ImageFactory
				.loadImage(ImageFactory.EXECUTE_SQL)));
		setToolTipText("Excute(F8)");
		setAccelerator(SWT.F8);
	}

	@Override
	public void run() {
		SQLWindow sw = CyperDataStudio.getStudio().getSqlWindow();
		CTabItem[] items = sw.getItems();
		//show result window if it's hidden.
		if (items != null && items.length>0) {
			for (int i = 0; i < items.length; i++) {
				SashForm right = (SashForm) items[i].getControl();
				if (right.getWeights()[1] == 0) {
					right.setWeights(SQLWindow.DEFAULT_WEIGHTS);
				} 
			}
			sw.setMaximized(false);
		}
		
		int folderIndex = sw.getSelectionIndex();
		SourceViewer text = sw.textViewerList.get(folderIndex);
		//show result
		CTabItem item = sw.getSelection();
		if (item!=null) {
			SQLResultModel model = (SQLResultModel) sw.tableList.get(folderIndex).getModel();
			String selectionText = text.getTextWidget().getSelectionText().trim();
			if (selectionText.endsWith(";")) {
				selectionText = selectionText.substring(0,selectionText.length()-1);
			}
			if (selectionText.length()==0) {
				selectionText = text.getTextWidget().getText();
			}
			
			if (selectionText.length()>0) {
				model.executeSQL(selectionText);
			}
		}
		
	}
}
