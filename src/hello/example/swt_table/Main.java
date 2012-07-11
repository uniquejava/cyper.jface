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

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
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

/**
 * (1)杜绝table.setSelection()这样的代码， selection太丑了，我们不需要selection，只需要row indicator.<br>
 * (2)杜绝table.getSelectionIndex(),table不再有任何selection.<br>
 * (3)维护一个全局的rowIndicator，在text focus时给它设定为当前的行号.
 * 
 * @author cyper.yin
 * 
 */
public class Main {

	private static final String COL_NUMBER = "colNumber";
	private static final String ROW_NUMBER = "rowNumber";
	private static final int SORT_ICON_WIDTH = 30;
	public static final Display display = new Display();
	public final Shell shell = new Shell(display);
	private ViewForm viewForm = null;
	private List<Row> list;
	private Text focusedText;
	private Text[][] textArray = null;

	// canCallFocusGained is for bug 001
	private boolean canCallFocusGained = true;

	public Main() throws Exception {
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

		final ToolItem viewSingleRow = new ToolItem(toolbar, SWT.CHECK);
		viewSingleRow.setImage(loadImage(COLUMN_MODE));

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

		// fix bug 002
		fixTableLinesClickBug(table);

		// 对toolbar的点击事件 集中进行处理
		Listener listener = new Listener() {

			@Override
			public void handleEvent(Event event) {
				if (event.widget == add) {
					// TableItem row = new TableItem(table, SWT.NONE,
					// selectionIndex);
				} else if (event.widget == viewSingleRow) {
					if (layout.topControl == table) {
						setInput4ColumnMode(columnModeTable, getFocus().y);
						layout.topControl = columnModeTable;
					} else {
						layout.topControl = table;
					}

					// fix bug 001
					canCallFocusGained = false;
					contentPanel.layout();

					// 点一下toolbar，text的焦点就丢失了，所以
					focusedText.setFocus();
					canCallFocusGained = true;

				} else if (event.widget == prev) {
					// 查看上一行数据
					// 先将焦点上移一格
					Point focus = getFocus();
					// 如果不是第一行才可以上移
					if (focus.y >= 2) {
						focusedText = textArray[focus.y - 1][focus.x];
						focusedText.setFocus();
						setInput4ColumnMode(columnModeTable, getFocus().y);
					}
				} else if (event.widget == next) {
					// 查看下一行数据
					// 先将焦点下移一格
					Point focus = getFocus();
					// 如果不是最后一行才可以下移，注意最后一行的y坐标是textArray.length-1
					if (focus.y < textArray.length - 1) {
						focusedText = textArray[focus.y + 1][focus.x];
						focusedText.setFocus();
						setInput4ColumnMode(columnModeTable, getFocus().y);
					}
				}

			}
		};
		add.addListener(SWT.Selection, listener);
		viewSingleRow.addListener(SWT.Selection, listener);
		prev.addListener(SWT.Selection, listener);
		next.addListener(SWT.Selection, listener);

		viewForm.setContent(contentPanel);

	}

	private void fixTableLinesClickBug(final Table table) {
		final int headerHeight = table.getHeaderHeight();
		final int itemHeight = table.getItemHeight();
		table.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e) {
				// 当光标点在表格的线上时，文本框会失去焦点..no!不可以
				// 33,47,61,75, 为了解决这个bug,首先观察每条线的y坐标，然后将 这个坐标偏上的单元格选中
				// headerHeight=20, itemHeight=14
				// 得出应选中单元格y坐标的通项式为(e.y-33)/14+1,注意必须为14.0而不是14
				// 得出更一般的(e.y-headerHeight-itemHeight-1)/itemHeight+1

				int n = (int) Math.ceil((e.y - headerHeight - itemHeight)
						/ (itemHeight * 1.0)) + 1;

				if (n >= 1 && n < textArray.length) {
					textArray[n][getFocus().x].setFocus();
					focusedText = textArray[n][getFocus().x];
				} else if (n < 1) {
					// PLSQL Developer不管鼠标点哪儿都至少有一行选中，所以这里我们也来实现.
					textArray[1][getFocus().x].setFocus();
					focusedText = textArray[1][getFocus().x];
				} else if (n >= textArray.length) {
					textArray[textArray.length - 1][getFocus().x].setFocus();
					focusedText = textArray[textArray.length - 1][getFocus().x];
				}
			}
		});
	}

	/**
	 * 取得当前获得焦点的text的Point(其实是在数组中的位置)
	 * 
	 * @return
	 */
	private Point getFocus() {
		Integer x = (Integer) focusedText.getData(COL_NUMBER);
		Integer y = (Integer) focusedText.getData(ROW_NUMBER);
		return new Point(x, y);
	}

	private void setInput(final Table table, final List<Row> list) {
		if (list == null || list.size() == 0) {
			return;
		}
		// clear the old data.
		table.removeAll();

		final HeaderRow tableHeader = (HeaderRow) list.get(0);
		int rowCount = list.size();
		int columnCount = tableHeader.keySet().size();

		// 第一行留白算了。。以便text和整个表格完全保持一致.
		textArray = new Text[rowCount][columnCount];

		// header
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
			}
			w++;
		}

		// row
		for (int rowNumber = 1; rowNumber < list.size(); rowNumber++) {
			Row row = list.get(rowNumber);
			final TableItem tableItem = new TableItem(table, SWT.NONE);
			String[] texts = new String[columnCount];
			row.values().toArray(texts);
			tableItem.setText(texts);
			if (rowNumber % 2 == 1) {
				tableItem.setBackground(getColor(COLOR_SQL_HIGHLIGHT));
			}

			final int rrowNumber = rowNumber;

			// bind text editor.
			for (int colNumber = 0; colNumber < columnCount; colNumber++) {
				final TableEditor editor = new TableEditor(table);
				final Text text = new Text(table, SWT.None);
				if (rowNumber % 2 == 1) {
					text.setBackground(getColor(COLOR_SQL_HIGHLIGHT));
				}
				text.setText(texts[colNumber]);
				text.setData(ROW_NUMBER, rowNumber);
				text.setData(COL_NUMBER, colNumber);
				textArray[rowNumber][colNumber] = text;

				editor.grabHorizontal = true;
				editor.setEditor(text, tableItem, colNumber);
				final int mm = colNumber;
				text.addModifyListener(new ModifyListener() {
					@Override
					public void modifyText(ModifyEvent e) {
						editor.getItem().setText(mm, text.getText());
					}
				});

				text.addFocusListener(new FocusListener() {
					@Override
					public void focusLost(FocusEvent e) {
						if (canCallFocusGained) {
							tableItem.setText("");
							textArray[rrowNumber][0].setText("");
						}
					}

					@Override
					public void focusGained(FocusEvent e) {
						if (canCallFocusGained) {
							tableItem.setText(">");
							textArray[rrowNumber][0].setText(">");
							focusedText = text;
						}
					}
				});

				if (rowNumber == 1 && colNumber == 2) {
					text.setFocus();
				}
			}

		}

		for (int j = 0; j < columnCount; j++) {
			TableColumn c = table.getColumn(j);
			c.pack();
			// 留下排序图标用的位置
			c.setWidth(c.getWidth() + SORT_ICON_WIDTH);
		}
	}

	/**
	 * 
	 * @param rowNumber
	 */
	private void setInput4ColumnMode(Table columnModeTable, int rowNumber) {
		columnModeTable.removeAll();

		Row row = list.get(rowNumber);
		// fix header is "","Rowx","Fields"
		String[] tableHeader = new String[] { "", "Row" + (rowNumber), "Fields" };
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
		Main test = new Main();
		test.open();
	}

	protected void configureShell() {
		shell.setText("Table Example");
		shell.setImage(loadImage(LOGO));
		shell.setLayout(new FillLayout());
	}
}
