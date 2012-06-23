package hello;

import java.util.List;

import hello.model.Person;
import hello.model.PersonFactory;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class TableViewerExample extends ApplicationWindow {
	public final static String[] th = { "ID", "name", "sex", "favorite color" };

	public TableViewerExample() {
		super(null);
	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setSize(300, 200);
		shell.setText("TableViwer demo");

	}

	@Override
	protected Control createContents(Composite parent) {
		TableViewer tv = new TableViewer(parent, SWT.FULL_SELECTION);
		Table t = tv.getTable();

		// 有了tableColumn已经可以显示表头的
		// 那么LabelProvider到底有什么用？
		// 其实LabelProvider是用来决定如何显示tbody中每个td中的数据的
		for (int i = 0; i < th.length; i++) {
			new TableColumn(t, SWT.LEFT).setText(th[i]);
			t.getColumn(i).pack();// pack means setVisible(true)
		}

		t.setHeaderVisible(true);
		t.setLinesVisible(true);

		tv.setContentProvider(new MyContentProvider());
		tv.setLabelProvider(new MyLabelProvider());

		tv.setInput(PersonFactory.createPersons(10));
		return parent;
	}

	public static void main(String[] args) {
		TableViewerExample test = new TableViewerExample();

		test.setBlockOnOpen(true);

		test.open();
	}

	class MyLabelProvider implements ITableLabelProvider {
		public void removeListener(ILabelProviderListener listener) {
		}

		public boolean isLabelProperty(Object element, String property) {
			return false;
		}

		public void dispose() {
		}

		public void addListener(ILabelProviderListener listener) {
		}

		// 其实LabelProvider是用来决定如何显示tbody中每个td中的数据的
		public String getColumnText(Object element, int columnIndex) {
			// List<Person> list = (List<Person>) element;
			// element是集合中的一项目元素（实际类型由ContentProvider决定
			Person p = (Person) element;
			switch (columnIndex) {
			case 0:
				return String.valueOf(p.getId());
			case 1:
				return p.getName();
			case 2:
				return p.getGender();
			case 3:
				return p.getColor();
			default:
				return null;
			}
		}

		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}
	}

	class MyContentProvider implements IStructuredContentProvider {
		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

		// 就是把Input传成一个数组对象，便于LabelProvider使用
		public Object[] getElements(Object inputElement) {
			List<Person> list = (List<Person>) inputElement;
			// Object[] result = new Object[list.size()];
			// list.toArray(result);
			// return result;
			return list.toArray();
		}
	}
}
