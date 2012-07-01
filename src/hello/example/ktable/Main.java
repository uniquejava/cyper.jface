package hello.example.ktable;

import static hello.example.ktable.util.ModelUtil.calcRefRowNumber;
import static hello.example.ktable.util.ModelUtil.getRowSelection;
import static hello.layout.aqua.ImageFactory.ADD;
import static hello.layout.aqua.ImageFactory.LOGO;
import static hello.layout.aqua.ImageFactory.MYTICK;
import static hello.layout.aqua.ImageFactory.SUBTRACT;
import static hello.layout.aqua.ImageFactory.loadImage;
import hello.example.ktable.sort.KTableSortOnClick;
import hello.example.ktable.sort.SortComparatorExample;
import hello.example.ktable.util.ModelUtil;
import hello.example.ktable.util.RefreshType;
import hello.example.ktable.util.Row;

import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import de.kupzog.ktable.KTable;
import de.kupzog.ktable.KTableCellDoubleClickListener;
import de.kupzog.ktable.KTableCellSelectionListener;
import de.kupzog.ktable.KTableSortComparator;
import de.kupzog.ktable.SWTX;

/**
 * 综合示例，向PL/SQL developer看齐.<br>
 * 目前的问题：<br>
 * 1.排序时，indicator依然指向先前的行，但是rowSelection不对，应该修改setSelection()方法，要和indicator位置一致
 * .<br>
 * 06-28解决过程：<br>
 * 折腾的一段时间还是自己解决了。<br>
 * 目前的table有两个listener<br>
 * 第一个onclick是用来 表头的点击，处理排序专用，第二个onclick是用来处理cell的点击，当点击表头时是跳过的。<br>
 * 方法是在第一个onclick排序结束后，table.redraw调用之前加入如下代码：<br>
 * 
 * <pre>
 * 
 * // always keep the same row selected when doing sort.
 * for (int i = 0; i &lt; model.getRowCount(); i++) {
 * 	String indicatorCell = (String) model.getContentAt(0, i);
 * 	if (indicatorCell.equals(&quot;&gt;&quot;)) {
 * 		// keep selection and indicator on the same row.
 * 		m_Table.setSelection(2, i, false);
 * 		break;
 * 	}
 * }
 * </pre>
 * 
 * 2.在排序的状态下，增加和删除行，排序信息会丢失:<br>
 * 解决办法<br>
 * 在refresh方法中手动更新rowMapping，需要先源码，让rowMapping在子类中可以直接访问.
 * 
 * @author cyper.yin
 * 
 */
public class Main {
	public static final Display display = new Display();
	public final Shell shell = new Shell(display);
	private ViewForm viewForm = null;

	public Main() {
		configureShell();
		viewForm = new ViewForm(shell, SWT.NONE);
		viewForm.setTopCenterSeparate(true);

		// tool bar
		ToolBar toolbar = new ToolBar(viewForm, SWT.FLAT);
		final ToolItem add = new ToolItem(toolbar, SWT.PUSH);
		add.setImage(loadImage(ADD));
		final ToolItem sub = new ToolItem(toolbar, SWT.PUSH);
		sub.setImage(loadImage(SUBTRACT));
		final ToolItem save = new ToolItem(toolbar, SWT.PUSH);
		save.setImage(loadImage(MYTICK));
		
		viewForm.setTopLeft(toolbar);

		// table
		Composite contentPanel = new Composite(viewForm, SWT.NONE);
		contentPanel.setLayout(new FillLayout());

		final KTable table = new KTable(contentPanel, SWT.FULL_SELECTION
				| SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL
				| SWTX.FILL_WITH_LASTCOL | SWTX.EDIT_ON_KEY);
		// table.setModel(new SQLResultModel(table));
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
				shell.setText("[" + row + "," + col + "]");
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
					model.refreshWithSort(table, newList, refRowNumber,
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
						model.refreshWithSort(table, newList, rowSelectoin,
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

	}

	/**
	 * @param args
	 */
	public void open() {
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

	public static void main(String[] args) {
		Main test = new Main();
		test.open();
	}

	protected void configureShell() {
		shell.setText("KTable Example");
		shell.setImage(loadImage(LOGO));
		// shell.setSize(500,600);
		shell.setLayout(new FillLayout());
		// shell.pack();
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
}
