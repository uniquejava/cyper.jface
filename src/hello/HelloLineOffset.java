package hello;


import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
/**
 * 注意getText(start,end) = getTextRange(start,end-start+1);
 * <pre>
 * ab
 * c
 * </pre>
 * 假设第一行 有ab，第二行有c<br>
 * 光标可以处在5个位置，对于text.getCarretOffset, a前为0,b前为1,b后为2, c前为4，c后为5<br>
 * 对于text.getOffsetAtLine(0)为0, text.getOffsetAtLine(1)=4<br>
 * text.getText(0, 0)已经返回了a
 * text.getText(0, 1)已经返回了ab
 * text.getText(0, 2)已经返回了ab+回车
 * text.getText(0, 3)已经返回了ab+回车
 * text.getText(0, 4)已经返回了abc
 *  
 * @author cyper.yin
 *
 */
public class HelloLineOffset {
	public static void main(String[] args) {
		// create the widget's shell
		Shell shell = new Shell();
		shell.setLayout(new FillLayout());
		shell.setSize(200, 100);
		Display display = shell.getDisplay();
		// create the styled text widget
		final StyledText text = new StyledText(shell, SWT.BORDER);
		text.setText("ab" + text.getLineDelimiter()+"c");
		text.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				System.out.println("caretOffset" + text.getCaretOffset());
				System.out.println("line0=" + text.getOffsetAtLine(0));
				System.out.println("line1=" + text.getOffsetAtLine(text.getLineCount()-1));
				System.out.println("text.getText(0, 0)=" + text.getText(0, 0));
				System.out.println("text.getText(0, 1)=" + text.getText(0, 1));
				System.out.println("text.getText(0, 2)=" + text.getText(0, 2));
				System.out.println("text.getText(0, 3)=" + text.getText(0, 3));
				System.out.println("text.getText(0, 4)=" + text.getText(0, 4));
//				System.out.println("text.getText(4, 5)=" + text.getText(4, 5));
			}
		});
		

		shell.open();
		while (!shell.isDisposed())
			if (!display.readAndDispatch())
				display.sleep();
	}
}
