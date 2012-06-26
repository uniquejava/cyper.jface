package hello.example.ktable;

import static hello.layout.aqua.ImageFactory.ADD;
import static hello.layout.aqua.ImageFactory.LOGO;
import static hello.layout.aqua.ImageFactory.SUBTRACT;
import static hello.layout.aqua.ImageFactory.loadImage;

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
		
		viewForm.setTopLeft(toolbar);
		
		// table
		Composite contentPanel = new Composite(viewForm, SWT.NONE);
		contentPanel.setLayout(new FillLayout());
		
		final KTable table = new KTable(contentPanel, SWT.FULL_SELECTION | SWT.MULTI | SWT.V_SCROLL 
				| SWT.H_SCROLL | SWTX.FILL_WITH_LASTCOL | SWTX.EDIT_ON_KEY);
		table.setModel(new SQLResultModel());
		//select first row by default
		table.setSelection(2, 1,false);
		table.addCellSelectionListener(
			new KTableCellSelectionListener()
			{
				public void updateRowIndicator(int rowNew){
					SQLResultModel model = (SQLResultModel) table.getModel();
					int[] rowsSelected = model.lastRowSelection;
					if (rowsSelected!=null) {
						for (int i = 0; i < rowsSelected.length; i++) {
							model.setContentAt(0, rowsSelected[i], " ");
						}
					}
					model.setContentAt(0, rowNew, ">");
					model.lastRowSelection = table.getRowSelection();
					table.redraw();
				}
			    public void cellSelected(int col, int row, int statemask) {
					System.out.println("Cell ["+col+";"+row+"] selected11.");
					if (row!=0) {
						updateRowIndicator(row);
					}
				}
				
				public void fixedCellSelected(int col, int row, int statemask) {
					System.out.println("Header ["+col+";"+row+"] selected11.");
					if (row!=0) {
						updateRowIndicator(row);
					}
				}
			}
		);
		
		
		viewForm.setContent(contentPanel);

		
		Listener listener = new Listener() {
			@Override
			public void handleEvent(Event event) {
				SQLResultModel model = (SQLResultModel) table.getModel();
				if (event.widget == add) {
					int[] rowSelection = table.getRowSelection();
					int refRowNumber = 1;
					if (rowSelection!=null && rowSelection.length>0) {
						refRowNumber = rowSelection[0];
					}
					if (refRowNumber==0) {
						refRowNumber = 1;
					}
					model.insertBlankRow(refRowNumber);
					model.refresh();
					table.redraw();
				}else if (event.widget==sub) {
					int[] rowSelection = table.getRowSelection();
					if (rowSelection!=null && rowSelection.length>0) {
						int row = rowSelection[0];
//						model.removePerson(row);
						table.setSelection(4, row-1, true);
						table.redraw();
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
}
