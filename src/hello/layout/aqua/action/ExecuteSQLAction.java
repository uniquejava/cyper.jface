package hello.layout.aqua.action;

import hello.layout.aqua.AquaDataStudio;
import hello.layout.aqua.ImageFactory;
import hello.model.PersonFactory;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;

public class ExecuteSQLAction extends Action {
	private AquaDataStudio studio;

	public ExecuteSQLAction(AquaDataStudio studio) {
		this.studio = studio;
		setImageDescriptor(ImageDescriptor.createFromImage(ImageFactory
				.loadImage(ImageFactory.EXECUTE_SQL)));
		setToolTipText("Excute(F8)");
		setAccelerator(SWT.F8);
	}

	@Override
	public void run() {
		CTabItem[] items = studio.tabFolder.getItems();
		//show result window if it's hidden.
		if (items != null && items.length>0) {
			for (int i = 0; i < items.length; i++) {
				SashForm right = (SashForm) items[i].getControl();
				if (right.getWeights()[1] == 0) {
					right.setWeights(new int[] { 50, 50 });
				} 
			}
			studio.tabFolder.setMaximized(false);
		}
		
		//show result
		CTabItem item = studio.tabFolder.getSelection();
		if (item!=null) {
			TableViewer tv = (TableViewer) item.getData("tv");
			tv.setInput(PersonFactory.createPersons(10));
		}
		
	}
}
