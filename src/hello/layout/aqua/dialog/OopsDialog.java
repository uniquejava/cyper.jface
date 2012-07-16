package hello.layout.aqua.dialog;

import hello.layout.aqua.ImageFactory;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.kitten.core.util.ErrorUtil;

public class OopsDialog extends Dialog {
	private Exception ex;

	public OopsDialog(Shell parentShell, Exception ex) {
		super(parentShell);
		this.ex = ex;
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setImage(newShell.getImage());
		newShell.setSize(600, 500);
		newShell.setLocation(350, 150);
		newShell.setText("oh. shit!");
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		// parent.setLayout(new GridLayout());
		Composite top = new Composite(parent, SWT.None);
		top.setLayoutData(new GridData(GridData.FILL_BOTH));
		top.setLayout(new GridLayout(2, false));
		CLabel lb = new CLabel(top, SWT.WRAP);

		lb.setImage(ImageFactory.loadImage(ImageFactory.ERROR));
		{
			Text summary = new Text(top, SWT.WRAP | SWT.READ_ONLY | SWT.MULTI);
			summary.setText(ex.getMessage());
			GridData gd = new GridData(GridData.FILL_HORIZONTAL);
			summary.setLayoutData(gd);
		}
		{

			Text text = new Text(top, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL
					| SWT.H_SCROLL);
			text.setText(ErrorUtil.getError(ex));
			GridData gd = new GridData(GridData.FILL_BOTH);
			gd.horizontalSpan = 2;
			text.setLayoutData(gd);
		}
		return parent;
	}

	public static void main(String[] args) {

		Display d = Display.getDefault();
		try {
			int a = 1 / 0;
		} catch (Exception e) {
			e.printStackTrace();
			OopsDialog od = new OopsDialog(new Shell(d), e);
			od.open();
		}
	}

}
