package hello.layout.aqua.dialog;

import static hello.layout.aqua.ImageFactory.SERVER;
import static hello.layout.aqua.ImageFactory.SQL_EDITOR;
import static hello.layout.aqua.ImageFactory.loadImage;
import static hello.layout.aqua.util.GridDataFactory.gd4text;
import hello.layout.aqua.ImageFactory;
import hello.model.Person;
import hello.model.PersonFactory;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

public class SQLWindow {
	public final static String[] th = { "ID", "name", "sex", "favorite color" };
	private CTabFolder tabFolder;
	public List<CTabItem> tabItemList = new ArrayList<CTabItem>();
	
	public SQLWindow(CTabFolder tabFolder) {
		this.tabFolder = tabFolder;
	}
	
	public void createNewTabItem(String title){
		// right top
		// sql editor
		final CTabItem tabItem = new CTabItem(tabFolder, SWT.None);
		tabItem.setText(title);
		tabItem.setImage(loadImage(SQL_EDITOR));

		SashForm right = new SashForm(tabFolder, SWT.VERTICAL);
		right.setLayout(new FillLayout());

		Text text = new Text(right, SWT.MULTI | SWT.BORDER);

		// =====================result window
		Composite rightBottom = new Composite(right, SWT.BORDER);
		GridLayout gl = new GridLayout(1, true);
		rightBottom.setLayout(gl);
		Composite buttonPanel = new Composite(rightBottom, SWT.BORDER);
		buttonPanel.setLayout(new RowLayout());
		buttonPanel.setLayoutData(gd4text());
		{
			Button b1 = new Button(buttonPanel, SWT.FLAT);
			b1.setImage(ImageFactory.loadImage(SERVER));
		}
		{
			Button b1 = new Button(buttonPanel, SWT.FLAT);
			b1.setImage(ImageFactory.loadImage(SERVER));
		}
		{
			Button b1 = new Button(buttonPanel, SWT.FLAT);
			b1.setImage(ImageFactory.loadImage(SERVER));
		}

		// results
		TableViewer tv = new TableViewer(rightBottom, SWT.FULL_SELECTION);
		tv.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
		Table t = tv.getTable();
		for (int i = 0; i < th.length; i++) {
			new TableColumn(t, SWT.LEFT).setText(th[i]);
			t.getColumn(i).pack();// pack means setVisible(true)
		}
		t.setHeaderVisible(true);
		t.setLinesVisible(true);
		tv.setContentProvider(new MyContentProvider());
		tv.setLabelProvider(new MyLabelProvider());
		tv.setInput(PersonFactory.createPersons(10));

		right.setWeights(new int[] { 50, 50 });
		tabItem.setControl(right);
		tabItem.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				System.out.println("I am disposed.");
				tabItemList.remove(tabItem);
			}
		});
		tabItem.setData("tv",tv);
		tabItem.setData("text",text);
		
		tabItemList.add(tabItem);
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
