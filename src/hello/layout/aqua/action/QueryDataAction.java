package hello.layout.aqua.action;

import hello.layout.aqua.CyperDataStudio;
import hello.layout.aqua.serverView.node.TableNode;
import hello.layout.aqua.sqlwindow.SQLResultModel;
import hello.layout.aqua.sqlwindow.SQLWindow;
import hello.layout.aqua.util.Node;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;

public class QueryDataAction extends Action {
	private CyperDataStudio studio;

	public QueryDataAction(CyperDataStudio studio) {
		this.studio = studio;
		this.setText("Query data");
//		this.setEnabled(getSelection() instanceof TableNode);
	}
	private Node getSelection(){
		StructuredSelection select =  (StructuredSelection) studio.serverTree.getSelection();
		Node node = (Node) select.getFirstElement();
		System.out.println(node);
		return node;
	}
	@Override
	public void run() {
		Node node = getSelection();
		System.out.println(node);
		if (!(node instanceof TableNode)) {
			return; 
		}
		TableNode tableNode = (TableNode)node;
		SQLWindow sw = CyperDataStudio.getStudio().getSqlWindow();
		sw.createNewTabItem("Query data of table " + tableNode.getName()+ "_" + (sw.seq++)/* + ".sql"*/,false);
		int folderIndex = sw.getSelectionIndex();
		SourceViewer text = sw.textViewerList.get(folderIndex);
		text.getTextWidget().setText("select * from " + tableNode.getName());
		
		
		
		
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
		
		//show result
		CTabItem item = sw.getSelection();
		if (item!=null) {
			SQLResultModel model = (SQLResultModel) sw.tableList.get(folderIndex).getModel();
			model.executeSQL(text.getTextWidget().getText().trim());
		}
		
	}
}
