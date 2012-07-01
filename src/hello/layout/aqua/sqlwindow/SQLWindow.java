package hello.layout.aqua.sqlwindow;

import static hello.example.ktable.util.ModelUtil.calcRefRowNumber;
import static hello.example.ktable.util.ModelUtil.getRowSelection;
import static hello.layout.aqua.ImageFactory.ADD;
import static hello.layout.aqua.ImageFactory.COLUMN_MODE;
import static hello.layout.aqua.ImageFactory.DOWN1;
import static hello.layout.aqua.ImageFactory.DOWN2;
import static hello.layout.aqua.ImageFactory.LOCK;
import static hello.layout.aqua.ImageFactory.MYTICK;
import static hello.layout.aqua.ImageFactory.NEXT;
import static hello.layout.aqua.ImageFactory.PREV;
import static hello.layout.aqua.ImageFactory.SQL_EDITOR;
import static hello.layout.aqua.ImageFactory.SUBTRACT;
import static hello.layout.aqua.ImageFactory.WYJ;
import static hello.layout.aqua.ImageFactory.loadImage;
import hello.example.ktable.sort.KTableSortOnClick;
import hello.example.ktable.sort.SortComparatorExample;
import hello.example.ktable.util.ModelUtil;
import hello.example.ktable.util.RefreshType;
import hello.example.ktable.util.Row;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import de.kupzog.ktable.KTable;
import de.kupzog.ktable.KTableCellDoubleClickListener;
import de.kupzog.ktable.KTableCellSelectionListener;
import de.kupzog.ktable.KTableSortComparator;
import de.kupzog.ktable.SWTX;

public class SQLWindow {
	public static int seq = 1;
	public final static int[] DEFAULT_WEIGHTS = new int[] { 30, 70 };
	private CTabFolder tabFolder;
	public List<CTabItem> tabItemList = new ArrayList<CTabItem>();
	public List<Text> textViewerList = new ArrayList<Text>();
	public List<KTable> tableList = new ArrayList<KTable>();
	private static SQLWindow instance = null;
	
	
	private SQLWindow(CTabFolder tabFolder) {
		this.tabFolder = tabFolder;
	}
	public static SQLWindow getInstace(CTabFolder tabFolder){
		if (instance==null) {
			instance = new SQLWindow(tabFolder);
		}
		return instance;
	}
	
	
	public CTabItem createNewTabItem(String title){
		// right top
		// sql editor
		final CTabItem tabItem = new CTabItem(tabFolder, SWT.None);
		tabItem.setText(title);
		tabItem.setImage(loadImage(SQL_EDITOR));

		SashForm right = new SashForm(tabFolder, SWT.VERTICAL);
		right.setLayout(new FillLayout());

		final Text textViewer = new Text(right, SWT.MULTI | SWT.BORDER);

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

		right.setWeights(SQLWindow.DEFAULT_WEIGHTS);
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

		final ToolItem sub = new ToolItem(toolbar, SWT.PUSH);
		sub.setImage(loadImage(SUBTRACT));

		
		final ToolItem save = new ToolItem(toolbar, SWT.PUSH);
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
		
		
		//swt table
		
		Composite contentPanel = new Composite(viewForm, SWT.NONE);
//		contentPanel.setLayout(new GridLayout(1, true));
		contentPanel.setLayout(new FillLayout());
		
		final KTable table = new KTable(contentPanel, SWT.FULL_SELECTION
				| SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL
				| SWTX.FILL_WITH_LASTCOL | SWTX.EDIT_ON_KEY);
		
		final SQLResultModel model = new SQLResultModel(table);

		final KTableSortOnClick sort = new KTableSortOnClick(table,
				new SortComparatorExample(model, -1,
						KTableSortComparator.SORT_NONE));
		table.addCellSelectionListener(new KTableCellSelectionListener() {
			public void updateRowIndicator(int rowNew) {
				SQLResultModel model = (SQLResultModel) table.getModel();
				for (int i = 0; i < model.getRowCount(); i++) {
					String indicatorCell = (String) model.getContentAt(0, i);
					if (indicatorCell.equals(">")) {
						model.setContentAt(0, i, "");
						break;
					}
				}
				model.setContentAt(0, rowNew, ">");
				table.redraw();
			}

			public void cellSelected(int col, int row, int statemask) {
				int actualRow = model.mapRowIndexToModel(row);
				System.out.println("checkList=" + model.checkList);
				updateModifiedCell(model, col + "/" + actualRow);
//				shell.setText("[" + row + "," + col + "]");
				// System.out
				// .println("Cell [" + col + ";" + row + "] selected11.");
				// 点表头的时候row竟然不为0，
				// 这是因为在KTableSortOnClick中使用了m_Table.setSelection(2, i, false);
				// 更改的点cell的事件
				// FIXME 这段我都看不懂写的啥了.
				// 如果在refresh的时候已经设定了rowIndicator
				/*if (model.updateRowIndicatorInRefreshMethod && sort.called  ) {
					System.out.println("model.updateRowIndicatorInRefreshMethod");
					model.updateRowIndicatorInRefreshMethod = false;
					return;
				} else*/
				if (row != 0) {
					updateRowIndicator(row);
				}

			}

			public void fixedCellSelected(int col, int row, int statemask) {
				updateModifiedCell(model, null);
				if (row != 0) {
					updateRowIndicator(row);
				}
			}
		});
		table.addCellSelectionListener(sort);
		table.addCellDoubleClickListener(new KTableCellDoubleClickListener() {
			@Override
			public void fixedCellDoubleClicked(int coxl, int rowx, int statemask) {
				updateModifiedCell(model, null);
			}

			@Override
			public void cellDoubleClicked(int col, int row, int statemask) {
				// System.out.println("Cell [" + col + ";" + row
				// + "] double clicked.");
				int actualRow = model.mapRowIndexToModel(row);
				updateModifiedCell(model, col + "/" + actualRow);
			}
		});

		viewForm.setContent(contentPanel);

		Listener listener = new Listener() {
			@Override
			public void handleEvent(Event event) {
				SQLResultModel model = (SQLResultModel) table.getModel();
				if (event.widget == add) {
					int refRowNumber = calcRefRowNumber(table);

					// 在此处应该让编辑状态的cell先失去焦点
					table.setSelection(2, refRowNumber, false);
					updateModifiedCell(model, null);

					// 当插入空白行后，row indicator是否指向新的空白行?Yes,!PLD是这么做的
					// 新插入的空白行没有行号
					// 其它行的行号保持不变
					List newList = model.insertBlankRow();
					sort.called = false;
					model.refreshWithSort(newList, refRowNumber,
							RefreshType.ADD);
					// Rectangle r = table.getCellRect(2, refRowNumber);
					// model.getCellEditor(2, refRowNumber).open(table, 2,
					// refRowNumber, r);

				} else if (event.widget == sub) {
					int rowSelectoin = getRowSelection(table);

					// 在此处应该让编辑状态的cell先失去焦点
					table.setSelection(2, rowSelectoin, false);
					updateModifiedCell(model, null);

					// 当删除的是空白行，OK，直接删除,反正它没有行号。
					// 当删除的是普通行，此时其它行的行号保持不变，但删除这行使行号变得不连续了
					if (rowSelectoin != ModelUtil.NO_SELECTION) {
						List newList = model.deleteRow(rowSelectoin);
						sort.called = false;
						model.refreshWithSort(newList, rowSelectoin,
								RefreshType.SUBTRACT);
					}
				} else if (event.widget == save) {
					// 在此处应该让编辑状态的cell先失去焦点
					int rowSelectoin = getRowSelection(table);
					table.setSelection(2, rowSelectoin, false);
					updateModifiedCell(model, null);
					
					System.out.println("origin=" + model.origin.size());
					System.out.println("data=" + model.data.size());
					System.out.println(model.origin);
					System.out.println(model.data);
				}
					

			}
		};
		add.addListener(SWT.Selection, listener);
		sub.addListener(SWT.Selection, listener);
		save.addListener(SWT.Selection, listener);

		
		
		viewForm.setContent(contentPanel);
		
		
		
		//----important------------
		right.setWeights(DEFAULT_WEIGHTS);
		tabItem.setControl(right);
		
		tabItemList.add(tabItem);
		textViewerList.add(textViewer);
		tableList.add(table);
		
		tabItem.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				System.out.println("I am disposed.");
				tabItemList.remove(tabItem);
				textViewerList.remove(textViewer);
				tableList.remove(table);
			}
		});
		
		tabFolder.setSelection(tabItem);
		
		return tabItem;
	}
	/**
	 * 持久化先前可能修改过的单元格.同时将将要修改的单元格newKey加入观察列表.
	 * 
	 * @param model
	 * @param newKey
	 */
	private void updateModifiedCell(final SQLResultModel model, String newKey) {
		if (model.checkList.size() > 0) {
			for (Iterator it = model.checkList.iterator(); it.hasNext();) {
				try {
					String key = (String) it.next();
					int slash = key.indexOf("/");
					int col = Integer.parseInt(key.substring(0, slash));
					int row = Integer.parseInt(key.substring(slash + 1));
					String newContent = (String) model.content.get(key);
					Row rowData = (Row) (model.data.get(row));
					String oldContent = (String) rowData
							.get(model.tableHeader[col]);
					System.out.println("oldContent=" + oldContent
							+ ",newContent=" + newContent);
					if (!newContent.equals(oldContent)) {
						rowData.put(model.tableHeader[col], newContent);
						System.out.println(model.checkList);
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}
		model.checkList.clear();
		if (newKey != null) {
			model.checkList.add(newKey);
		}
	}
//
//	class MyLabelProvider implements ITableLabelProvider {
//		public void removeListener(ILabelProviderListener listener) {
//		}
//
//		public boolean isLabelProperty(Object element, String property) {
//			return false;
//		}
//
//		public void dispose() {
//		}
//
//		public void addListener(ILabelProviderListener listener) {
//		}
//
//		// 其实LabelProvider是用来决定如何显示tbody中每个td中的数据的
//		public String getColumnText(Object element, int columnIndex) {
//			// List<Person> list = (List<Person>) element;
//			// element是集合中的一项目元素（实际类型由ContentProvider决定
//			Person p = (Person) element;
//			switch (columnIndex) {
//			case 0:
//				return String.valueOf(p.getId());
//			case 1:
//				return p.getName();
//			case 2:
//				return p.getGender();
//			case 3:
//				return p.getColor();
//			default:
//				return null;
//			}
//		}
//
//		public Image getColumnImage(Object element, int columnIndex) {
//			return null;
//		}
//	}
//
//	class MyContentProvider implements IStructuredContentProvider {
//		public void dispose() {
//		}
//
//		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
//		}
//
//		// 就是把Input传成一个数组对象，便于LabelProvider使用
//		public Object[] getElements(Object inputElement) {
//			List<Person> list = (List<Person>) inputElement;
//			// Object[] result = new Object[list.size()];
//			// list.toArray(result);
//			// return result;
//			return list.toArray();
//		}
//	}
}
