package hello.example.ktable.text;

import static hello.example.ktable.util.ModelUtil.NO_SELECTION;
import static hello.example.ktable.util.ModelUtil.calcRefRowNumber;
import static hello.example.ktable.util.ModelUtil.getRowSelection;
import static hello.layout.aqua.ImageFactory.ADD;
import static hello.layout.aqua.ImageFactory.LOGO;
import static hello.layout.aqua.ImageFactory.SUBTRACT;
import static hello.layout.aqua.ImageFactory.loadImage;

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
import de.kupzog.ktable.KTableCellSelectionListener;
import de.kupzog.ktable.SWTX;

/**
 * 学习KTableDefaultModel.
 * 
 * @author cyper.yin
 * 
 */
public class Main {
	public static final Display display = new Display();
	public final Shell shell = new Shell(display);
	private ViewForm viewForm = null;

	public Main() throws Exception {
		configureShell();
		viewForm = new ViewForm(shell, SWT.NONE);
		viewForm.setTopCenterSeparate(true);

		// tool bar
		ToolBar toolbar = new ToolBar(viewForm, SWT.FLAT);
		final ToolItem add = new ToolItem(toolbar, SWT.PUSH);
		add.setImage(loadImage(ADD));
		final ToolItem sub = new ToolItem(toolbar, SWT.PUSH);
		sub.setImage(loadImage(SUBTRACT));

		viewForm.setTopLeft(toolbar);

		// table
		Composite contentPanel = new Composite(viewForm, SWT.NONE);
		contentPanel.setLayout(new FillLayout());

		final KTable table = new KTable(contentPanel, SWT.FULL_SELECTION
				| SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL
				| SWTX.FILL_WITH_LASTCOL | SWTX.EDIT_ON_KEY);
		// table.setModel(new SQLResultModel(table));
		new SQLResultModelText(table);
		table.addCellSelectionListener(new KTableCellSelectionListener() {
			public void updateRowIndicator(int rowNew) {
				SQLResultModelText model = (SQLResultModelText) table
						.getModel();
				int[] rowsSelected = model.lastRowSelection;
				if (rowsSelected != null) {
					for (int i = 0; i < rowsSelected.length; i++) {
						model.setContentAt(0, rowsSelected[i], " ");
					}
				}
				model.setContentAt(0, rowNew, ">");
				model.lastRowSelection = table.getRowSelection();
				table.redraw();
			}

			public void cellSelected(int col, int row, int statemask) {
				System.out
						.println("Cell [" + col + ";" + row + "] selected11.");
				if (row != 0) {
					updateRowIndicator(row);
				}
			}

			public void fixedCellSelected(int col, int row, int statemask) {
				// 当点击表头时，table.getRowSelection()不会变化，先前在哪，现在依然指向哪儿
				System.out.println("Header [" + col + ";" + row
						+ "] selected11.");
				if (row != 0) {
					updateRowIndicator(row);
				}
			}
		});

		viewForm.setContent(contentPanel);

		Listener listener = new Listener() {
			@Override
			public void handleEvent(Event event) {
				SQLResultModelText model = (SQLResultModelText) table
						.getModel();
				if (event.widget == add) {
					int refRowNumber = calcRefRowNumber(table);
					// 当插入空白行后，row indicator是否指向新的空白行?Yes,!PLD是这么做的
					// 新插入的空白行没有行号
					// 其它行的行号保持不变
					List newList = model.insertBlankRow(refRowNumber);
					model.refresh(table, newList, refRowNumber);
					// table.redraw();

				} else if (event.widget == sub) {
					int rowSelectoin = getRowSelection(table);
					if (rowSelectoin != NO_SELECTION) {
						// 当删除的是空白行，OK，直接删除,反正它没有行号。
						// 当删除的是普通行，此时其它行的行号保持不变，但删除这行使行号变得不连续了
						// 为做到此效果，应该在list中保留此行，为此本屌引入HiddenRow（占着行号但不显示)
						// 注意rowCount不再是list.size()而是list是排除HiddenRow之后的size。
						List newList = model.deleteRow(rowSelectoin);
						model.refresh(table, newList, rowSelectoin);
					}
				}

			}
		};
		add.addListener(SWT.Selection, listener);
		sub.addListener(SWT.Selection, listener);
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

	public static void main(String[] args) throws Exception {
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
}
