package hello.example.ktable.sort;

import static hello.layout.aqua.ImageFactory.ADD;
import static hello.layout.aqua.ImageFactory.LOGO;
import static hello.layout.aqua.ImageFactory.SUBTRACT;
import static hello.layout.aqua.ImageFactory.loadImage;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import de.kupzog.ktable.KTable;
import de.kupzog.ktable.KTableCellResizeListener;
import de.kupzog.ktable.KTableCellSelectionListener;
import de.kupzog.ktable.KTableSortComparator;
import de.kupzog.ktable.KTableSortOnClick;
import de.kupzog.ktable.KTableSortedModel;
import de.kupzog.ktable.SWTX;

/**
 * 学习KTableSortedModel.<br>
 * 看SQLResultModelSort类中的注释.
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
		final KTableSortedModel model = new SQLResultModelSort();
		table.setModel(model);
		table.addCellSelectionListener(new KTableCellSelectionListener() {
			public void cellSelected(int col, int row, int statemask) {
				// the idea is to map the row index back to the model index
				// since the given row index
				// changes when sorting is done.
				int modelRow = model.mapRowIndexToModel(row);
				System.out.println("Cell [" + col + ";" + row
						+ "] selected. - Model row: " + modelRow);
			}

			public void fixedCellSelected(int col, int row, int statemask) {
				System.out
						.println("Header [" + col + ";" + row + "] selected.");
			}
		});
		// implement resorting when the user clicks on the table header:
		table.addCellSelectionListener(new KTableSortOnClick(table,
				new SortComparatorExample(model, -1,
						KTableSortComparator.SORT_NONE)));

		table.addCellResizeListener(new KTableCellResizeListener() {
			public void columnResized(int col, int newWidth) {
				System.out.println("Column " + col + " resized to " + newWidth);
			}

			public void rowResized(int row, int newHeight) {
				System.out.println("Row " + row + " resized to " + newHeight);
			}
		});

		viewForm.setContent(contentPanel);

		// Listener listener = new Listener() {
		// @Override
		// public void handleEvent(Event event) {
		// SQLResultModel model = (SQLResultModel) table.getModel();
		// if (event.widget == add) {
		// int refRowNumber = calcRefRowNumber(table);
		// List newList = model.insertBlankRow(refRowNumber);
		// model.refresh(table,newList,refRowNumber);
		//
		// }else if (event.widget==sub) {
		// int rowSelectoin = getRowSelection(table);
		// if (rowSelectoin!=NO_SELECTION) {
		// List newList = model.deleteRow(rowSelectoin);
		// model.refresh(table,newList,rowSelectoin);
		// }
		// }
		//
		// }
		// };
		// add.addListener(SWT.Selection, listener);
		// sub.addListener(SWT.Selection, listener);
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
