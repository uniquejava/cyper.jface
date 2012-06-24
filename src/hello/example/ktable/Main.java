package hello.example.ktable;

import static hello.layout.aqua.ImageFactory.LOGO;
import static hello.layout.aqua.ImageFactory.loadImage;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import de.kupzog.ktable.KTable;
import de.kupzog.ktable.KTableCellSelectionListener;
import de.kupzog.ktable.SWTX;

public class Main {
	public static final Display display = new Display();
	public final Shell shell = new Shell(display);

	public Main() {
		configureShell();
		
		final KTable table = new KTable(shell, SWT.FULL_SELECTION | SWT.MULTI | SWT.V_SCROLL 
				| SWT.H_SCROLL | SWTX.FILL_WITH_LASTCOL | SWTX.EDIT_ON_KEY);
		table.setModel(new TextModelExample());
		table.addCellSelectionListener(
			new KTableCellSelectionListener()
			{
			    public void cellSelected(int col, int row, int statemask) {
					System.out.println("Cell ["+col+";"+row+"] selected.");
				}
				
				public void fixedCellSelected(int col, int row, int statemask) {
					System.out.println("Header ["+col+";"+row+"] selected.");
				}
			}
		);
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
