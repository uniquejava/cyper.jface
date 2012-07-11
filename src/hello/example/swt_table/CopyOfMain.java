package hello.example.swt_table;

import static hello.ResourceManager.COLOR_SQL_HIGHLIGHT;
import static hello.ResourceManager.getColor;
import static hello.layout.aqua.ImageFactory.ADD;
import static hello.layout.aqua.ImageFactory.COLUMN_MODE;
import static hello.layout.aqua.ImageFactory.DOWN1;
import static hello.layout.aqua.ImageFactory.DOWN2;
import static hello.layout.aqua.ImageFactory.LOCK;
import static hello.layout.aqua.ImageFactory.LOGO;
import static hello.layout.aqua.ImageFactory.MYTICK;
import static hello.layout.aqua.ImageFactory.NEXT;
import static hello.layout.aqua.ImageFactory.PREV;
import static hello.layout.aqua.ImageFactory.SUBTRACT;
import static hello.layout.aqua.ImageFactory.WYJ;
import static hello.layout.aqua.ImageFactory.loadImage;
import hello.example.ktable.dao.TestDao;
import hello.example.ktable.util.HeaderRow;
import hello.example.ktable.util.Row;
import hello.layout.aqua.util.GridDataFactory;

import java.text.Collator;
import java.util.List;
import java.util.Locale;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

public class CopyOfMain {

	private static final int SORT_ICON_WIDTH = 30;
	public static final Display display = new Display();
	public final Shell shell = new Shell(display);
	private ViewForm viewForm = null;
	private List<Row> list;

	public CopyOfMain() throws Exception {
		configureShell();
		viewForm = new ViewForm(shell, SWT.NONE);
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

		final ToolItem down1 = new ToolItem(toolbar, SWT.PUSH);
		down1.setImage(loadImage(DOWN1));

		final ToolItem down2 = new ToolItem(toolbar, SWT.PUSH);
		down2.setImage(loadImage(DOWN2));

		final ToolItem wyj = new ToolItem(toolbar, SWT.PUSH);
		wyj.setImage(loadImage(WYJ));

		final ToolItem cm = new ToolItem(toolbar, SWT.CHECK);
		cm.setImage(loadImage(COLUMN_MODE));

		final ToolItem prev = new ToolItem(toolbar, SWT.PUSH);
		prev.setImage(loadImage(PREV));

		final ToolItem next = new ToolItem(toolbar, SWT.PUSH);
		next.setImage(loadImage(NEXT));

		viewForm.setTopLeft(toolbar);

		// table
		final Composite contentPanel = new Composite(viewForm, SWT.NONE);
		// contentPanel.setLayout(new GridLayout(1, true));
		final StackLayout layout = new StackLayout();
		contentPanel.setLayout(layout);

		final Table table = new Table(contentPanel, SWT.NONE);
		table.setHeaderVisible(true);
		table.setLayoutData(GridDataFactory.fill_both());
		table.setLinesVisible(true);

		final Table columnModeTable = new Table(contentPanel,
				SWT.FULL_SELECTION);
		columnModeTable.setHeaderVisible(true);
		columnModeTable.setLayoutData(GridDataFactory.fill_both());
		columnModeTable.setLinesVisible(true);

		layout.topControl = table;

		list = new TestDao().querySql("select * from ORG");
		setInput(table, list);

		// set row indicator
		table.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
//				int total = table.getItemCount();
//				for (int i = 0; i < total; i++) {
//					TableItem item = table.getItem(i);
//					if (table.isSelected(i)) {
//						item.setText(">");
//						System.out.println(item.getBackground());
//					} else {
//						item.setText("");
//					}
//				}
			}
		});

		// set row indicator
//		columnModeTable.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				int total = columnModeTable.getItemCount();
//				for (int i = 0; i < total; i++) {
//					TableItem item = columnModeTable.getItem(i);
//					if (columnModeTable.isSelected(i)) {
//						item.setText(">");
//					} else {
//						item.setText("");
//					}
//				}
//			}
//		});

		Listener listener = new Listener() {
			@Override
			public void handleEvent(Event event) {
				if (event.widget == add) {
					// deselect current row
					int selectionIndex = table.getSelectionIndex();
					TableItem selectedItem = table.getSelection()[0];
					selectedItem.setText("");

					// add new row and select it
					TableItem row = new TableItem(table, SWT.NONE,
							selectionIndex);
					table.setSelection(selectionIndex);
					row.setText(">");
				} else if (event.widget == cm) {

					int selectionIndex = table.getSelectionIndex();

					if (layout.topControl == table) {
						setInput4ColumnMode(columnModeTable, selectionIndex);
						layout.topControl = columnModeTable;
					} else {
						layout.topControl = table;
					}

					contentPanel.layout();

				}

			}
		};
		add.addListener(SWT.Selection, listener);
		cm.addListener(SWT.Selection, listener);

		viewForm.setContent(contentPanel);

	}

	private String[] getColumnText(Table table, TableItem item) {
		int count = table.getColumnCount();
		String[] text = new String[count];
		for (int i = 0; i < count; i++) {
			text[i] = item.getText(i);
		}
		return text;
	}

	private void setInput(final Table table, final List<Row> list) {
		if (list == null || list.size() == 0) {
			return;
		}
		// clear the old data.
		table.removeAll();

		final HeaderRow tableHeader = (HeaderRow) list.get(0);
		int columnCount = tableHeader.keySet().size();

		// header

		Listener sortListener = new Listener() {
			public void handleEvent(Event e) {

				TableItem[] items = table.getItems();
				Collator collator = Collator.getInstance(Locale.getDefault());
				TableColumn column = (TableColumn) e.widget;

				TableColumn prevSortColumn = table.getSortColumn();

				int sortDirection = SWT.UP;
				if (column == prevSortColumn) {
					int direction = table.getSortDirection();
					if (direction == SWT.UP) {
						sortDirection = SWT.DOWN;
					} else if (direction == SWT.DOWN) {
						sortDirection = SWT.None;
					} else {
						sortDirection = SWT.UP;
					}
				}

				if (sortDirection== SWT.None) {
					setInput(table, list, tableHeader);
				}else{
					int index = (Integer) column.getData();
					for (int i = 1; i < items.length; i++) {
						String value1 = items[i].getText(index);
						for (int j = 0; j < i; j++) {
							String value2 = items[j].getText(index);
							if ((sortDirection == SWT.UP && collator.compare(value1, value2) < 0)
									|| (sortDirection == SWT.DOWN && collator.compare(value1, value2) > 0)) {
								String[] values = getColumnText(table, items[i]);
								items[i].dispose();
								TableItem item = new TableItem(table, SWT.NONE, j);
								item.setText(values);
								if (">".equals(item.getText())){
									table.setSelection(item);
								}
								items = table.getItems();
								break;
							}
						}
					}
				}
				table.setSortColumn(column);
				table.setSortDirection(sortDirection);
			}
		};
		int w = 0;
		for (String key : tableHeader.keySet()) {
			TableColumn th = new TableColumn(table, SWT.NONE);
			th.setText((String) tableHeader.get(key));
			if (w == 0) {
				th.setResizable(false);
				th.setMoveable(false);
			} else if (w == 1) {
				th.setResizable(false);
				th.setMoveable(false);
			} else {
				th.setData(w);
				th.addListener(SWT.Selection, sortListener);
			}
			w++;
		}

		// row
		for (int k = 1; k < list.size(); k++) {
			Row row = list.get(k);
			final TableItem tableItem = new TableItem(table, SWT.NONE);
			String[] texts = new String[columnCount];
			row.values().toArray(texts);
			tableItem.setText(texts);
			if (k % 2 == 1) {
				tableItem.setBackground(getColor(COLOR_SQL_HIGHLIGHT));
			}
			
			
			//bind text editor.
			for (int m = 2; m < columnCount; m++) {
				final TableEditor editor = new TableEditor(table);
				final Text text = new Text(table,SWT.None);
				if (k % 2 == 1) {
					text.setBackground(getColor(COLOR_SQL_HIGHLIGHT));
				}
				text.setText(texts[m]);
				editor.grabHorizontal=true;
				editor.setEditor(text, tableItem, m);
				final int mm = m;
				text.addModifyListener(new ModifyListener() {
					@Override
					public void modifyText(ModifyEvent e) {
						editor.getItem().setText(mm,text.getText());
					}
				});
				
				
				final int kk = k;
				text.addFocusListener(new FocusListener() {
					
					@Override
					public void focusLost(FocusEvent e) {
						tableItem.setText("");
					}
					
					@Override
					public void focusGained(FocusEvent e) {
						tableItem.setText(">");
					}
				});
				
				if (k==1 && m==2) {
					text.setFocus();
				}
			}
			
		}

		for (int j = 0; j < columnCount; j++) {
			TableColumn c = table.getColumn(j);
			c.pack();
			//留下排序图标用的位置
			c.setWidth(c.getWidth()+SORT_ICON_WIDTH);
		}
	}
	private void setInput(final Table table, final List<Row> list, HeaderRow tableHeader) {
		// clear the old data.
		table.remove(0, table.getItemCount()-1);
		int columnCount = tableHeader.keySet().size();
		// row
		for (int k = 1; k < list.size(); k++) {
			Row row = list.get(k);
			TableItem tableItem = new TableItem(table, SWT.NONE);
			String[] text = new String[columnCount];
			row.values().toArray(text);
			tableItem.setText(text);
			if (k % 2 == 1) {
				tableItem.setBackground(getColor(COLOR_SQL_HIGHLIGHT));
			}
		}
	}
	/**
	 * 
	 * @param rowIndex
	 */
	private void setInput4ColumnMode(Table columnModeTable, int rowIndex) {
		columnModeTable.removeAll();

		// list的第一行是header，所以这里要+1
		Row row = list.get(rowIndex + 1);
		// fix header is "","Rowx","Fields"
		String[] tableHeader = new String[] { "", "Row" + (rowIndex + 1),
				"Fields" };
		int columnCount = 3;
		{
			TableColumn th = new TableColumn(columnModeTable, SWT.NONE);
			th.setText(tableHeader[0]);
			th.setResizable(false);
			th.setMoveable(false);
		}
		{
			TableColumn th = new TableColumn(columnModeTable, SWT.NONE);
			th.setText(tableHeader[1]);
		}
		{
			TableColumn th = new TableColumn(columnModeTable, SWT.NONE);
			th.setText(tableHeader[2]);
		}
		// row
		int k = 0;
		for (String key : row.keySet()) {
			// 跳过前两个key
			if (k > 1) {
				TableItem tableItem = new TableItem(columnModeTable, SWT.NONE);
				String[] text = new String[] { "", key, (String) row.get(key) };
				tableItem.setText(text);
				if (k % 2 == 1) {
					tableItem.setBackground(getColor(COLOR_SQL_HIGHLIGHT));
				}
			}
			k++;
		}
		for (int j = 0; j < columnCount; j++) {
			columnModeTable.getColumn(j).pack();
		}

	}

	public void open() {
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

	public static void main(String[] args) throws Exception {
		CopyOfMain test = new CopyOfMain();
		test.open();
	}

	protected void configureShell() {
		shell.setText("Table Example");
		shell.setImage(loadImage(LOGO));
		// shell.setSize(500,600);
		shell.setLayout(new FillLayout());
		// shell.pack();
	}
}
