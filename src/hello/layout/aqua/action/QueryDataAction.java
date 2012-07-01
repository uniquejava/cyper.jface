package hello.layout.aqua.action;

import hello.layout.aqua.AquaDataStudio;
import hello.layout.aqua.serverView.node.TableNode;
import hello.layout.aqua.sqlwindow.SQLResultModel;
import hello.layout.aqua.sqlwindow.SQLWindow;
import hello.layout.aqua.util.Node;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Text;

public class QueryDataAction extends Action {
	private AquaDataStudio studio;

	public QueryDataAction(AquaDataStudio studio) {
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
		SQLWindow sw = SQLWindow.getInstace(studio.tabFolder);
		sw.createNewTabItem(tableNode.getName()+ (sw.seq++) + ".sql");
		int folderIndex = studio.tabFolder.getSelectionIndex();
		Text text = sw.textViewerList.get(folderIndex);
		text.setText("select * from " + tableNode.getName());
		
		
		
		
		CTabItem[] items = studio.tabFolder.getItems();
		//show result window if it's hidden.
		if (items != null && items.length>0) {
			for (int i = 0; i < items.length; i++) {
				SashForm right = (SashForm) items[i].getControl();
				if (right.getWeights()[1] == 0) {
					right.setWeights(SQLWindow.DEFAULT_WEIGHTS);
				} 
			}
			studio.tabFolder.setMaximized(false);
		}
		
		//show result
		CTabItem item = studio.tabFolder.getSelection();
		if (item!=null) {
			SQLResultModel model = (SQLResultModel) sw.tableList.get(folderIndex).getModel();
			model.executeSQL(text.getText().trim());
		}
		
	}
}
