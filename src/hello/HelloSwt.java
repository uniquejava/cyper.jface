package hello;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class HelloSwt {
	static final Display display = new Display();
	static final Shell shell = new Shell(display);
	public static void main(String[] args) {
		shell.setText("SWT");
		
		Button button = new Button(shell, SWT.CENTER);
		button.setText("welcome use swt。");
		button.pack();
		
		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}
}
