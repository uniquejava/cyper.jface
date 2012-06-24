package hello.example.table;

import static hello.ResourceManager.*;
import static hello.layout.aqua.ImageFactory.*;
import static hello.layout.aqua.ImageFactory.loadImage;
import hello.ResourceManager;
import hello.layout.aqua.util.GridDataFactory;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

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

		final Table table = new Table(contentPanel, SWT.FULL_SELECTION);
		table.setHeaderVisible(true);
		table.setLayoutData(GridDataFactory.fill_both());
		table.setLinesVisible(true);

		String[] tableHeader = { "  ", "  ", "COLUMN_1", "COLUMN_2",
				"COLUMN_3", "COLUMN_4", "COLUMN_5" };
		for (int i = 0; i < tableHeader.length; i++) {
			TableColumn th = new TableColumn(table, SWT.NONE);
			th.setText(tableHeader[i]);
			if (i == 0) {
				th.setResizable(false);
				th.setMoveable(false);
			} else if (i == 1) {
				th.setResizable(false);
				th.setMoveable(false);
			}
		}

		// row
		int rowCount = 5;
		for (int i = 0; i < rowCount; i++) {
			TableItem row = new TableItem(table, SWT.NONE);
			row.setText(new String[] { " ", String.valueOf(i + 1), "abcd",
					"cyper test for fg", "GBS", "2012-06-12 23:23:14", "5" });
			if (i % 2 == 1) {
				row.setBackground(getColor(COLOR_SQL_HIGHLIGHT));
			}
		}

		for (int i = 0; i < tableHeader.length; i++) {
			table.getColumn(i).pack();
		}

		//set row indicator
		table.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int total = table.getItemCount();
				for (int i = 0; i < total; i++) {
					TableItem item = table.getItem(i);
					if (table.isSelected(i)) {
						item.setImage(0, loadImage(ROW_INDICATOR));
					} else {
						item.setImage(0, null);
					}
				}
			}
		});
		//show line numbers
		TableItem[] items = table.getItems();
		for (int i = 0; i < items.length; i++) {
			final TableEditor editor = new TableEditor(table);
			final Label label = new Label(table, SWT.CENTER);
			label.setText(items[i].getText(1));
			editor.grabHorizontal = true;
			editor.setEditor(label, items[i], 1);
		}
		
		
		//set line height!
		table.addListener(SWT.MeasureItem, new Listener() {
			@Override
			public void handleEvent(Event event) {
				event.height = 15;
			}
		});

		
		viewForm.setContent(contentPanel);

		
		Listener listener = new Listener() {
			@Override
			public void handleEvent(Event event) {
				if (event.widget == add) {
					TableItem row = new TableItem(table, SWT.NONE);
					row.setText(new String[] { "1", "cyper test for fg", "GBS",
							"2012-06-12 23:23:14", "5" });
				}

			}
		};
		add.addListener(SWT.Selection, listener);
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

	public static void main(String[] args) {
		Main test = new Main();
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
