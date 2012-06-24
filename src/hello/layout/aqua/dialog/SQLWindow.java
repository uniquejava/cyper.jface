package hello.layout.aqua.dialog;

import static hello.layout.aqua.ImageFactory.ADD;
import static hello.layout.aqua.ImageFactory.COLUMN_MODE;
import static hello.layout.aqua.ImageFactory.DOWN1;
import static hello.layout.aqua.ImageFactory.DOWN2;
import static hello.layout.aqua.ImageFactory.LOCK;
import static hello.layout.aqua.ImageFactory.MYTICK;
import static hello.layout.aqua.ImageFactory.NEXT;
import static hello.layout.aqua.ImageFactory.PREV;
import static hello.layout.aqua.ImageFactory.SERVER;
import static hello.layout.aqua.ImageFactory.SQL_EDITOR;
import static hello.layout.aqua.ImageFactory.SUBTRACT;
import static hello.layout.aqua.ImageFactory.WYJ;
import static hello.layout.aqua.ImageFactory.loadImage;
import static hello.layout.aqua.util.GridDataFactory.gd4text;
import hello.layout.aqua.ImageFactory;
import hello.layout.aqua.util.GridDataFactory;
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
import org.eclipse.swt.custom.ViewForm;
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
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

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
		
		/*
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
		*/
		ViewForm viewForm = new ViewForm(right, SWT.NONE);
		viewForm.setTopCenterSeparate(true);

		// tool bar
		ToolBar toolbar = new ToolBar(viewForm, SWT.FLAT);
		final ToolItem lock = new ToolItem(toolbar, SWT.PUSH);
		lock.setImage(loadImage(LOCK));
		
		
		final ToolItem add = new ToolItem(toolbar, SWT.PUSH);
		add.setImage(loadImage(ADD));

		ToolItem sub = new ToolItem(toolbar, SWT.PUSH);
		sub.setImage(loadImage(SUBTRACT));

		
		ToolItem save = new ToolItem(toolbar, SWT.PUSH);
		save.setImage(loadImage(MYTICK));
		
		ToolItem down1 = new ToolItem(toolbar, SWT.PUSH);
		down1.setImage(loadImage(DOWN1));
		
		ToolItem down2 = new ToolItem(toolbar, SWT.PUSH);
		down2.setImage(loadImage(DOWN2));
		
		ToolItem wyj = new ToolItem(toolbar, SWT.PUSH);
		wyj.setImage(loadImage(WYJ));
		ToolItem cm = new ToolItem(toolbar, SWT.PUSH);
		cm.setImage(loadImage(COLUMN_MODE));
		ToolItem prev = new ToolItem(toolbar, SWT.PUSH);
		prev.setImage(loadImage(PREV));
		ToolItem next = new ToolItem(toolbar, SWT.PUSH);
		next.setImage(loadImage(NEXT));
		
		
		viewForm.setTopLeft(toolbar);
		
		
		// table
		Composite contentPanel = new Composite(viewForm, SWT.NONE);
		contentPanel.setLayout(new GridLayout(1, true));
		
		Table table = new Table(contentPanel, SWT.FULL_SELECTION);
		table.setHeaderVisible(true);
		table.setLayoutData(GridDataFactory.fill_both());
		table.setLinesVisible(true);
		String[] tableHeader = {"COLUMN_1","COLUMN_2","COLUMN_3","COLUMN_4","COLUMN_5"};
		for (int i = 0; i < tableHeader.length; i++) {
			TableColumn th = new TableColumn(table,SWT.NONE);
			th.setText(tableHeader[i]);
		}
		
		//row1
		{
			TableItem row = new TableItem(table, SWT.NONE);
			row.setText(new String[]{"1","cyper test for fg","GBS","2012-06-12 23:23:14","5"});
		}
		{
			TableItem row = new TableItem(table, SWT.NONE);
			row.setText(new String[]{"1","cyper test for fg","GBS","2012-06-12 23:23:14","5"});
		}
		{
			TableItem row = new TableItem(table, SWT.NONE);
			row.setText(new String[]{"1","cyper test for fg","GBS","2012-06-12 23:23:14","5"});
		}
		
		for (int i = 0; i < tableHeader.length; i++) {
			table.getColumn(i).pack();
		}
		viewForm.setContent(contentPanel);
		
		//----important------------
		right.setWeights(new int[] { 50, 50 });
		tabItem.setControl(right);
		
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
