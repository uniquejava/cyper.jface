package hello.layout.aqua.dialog;

import static hello.layout.aqua.sqlwindow.Constants.PRODUCT_NAME;
import hello.layout.aqua.Bootstrap;
import hello.layout.aqua.ImageFactory;
import hello.layout.aqua.dialog.connect.ConnectionDialog;
import hello.layout.aqua.dialog.connect.ConnectionInfo;
import hello.layout.aqua.util.DbUtil;

import java.sql.Connection;
import java.util.Map;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.kitten.core.util.StringUtil;

public class LogonDialog extends Dialog {
	public static String currentConnectionName;
	public static String currentUsername;
	public static String currentPassword;

	private Combo databaseText;
	private Text usernameText;
	private Text passwordText;

	public LogonDialog(Shell parentShell) {
		super(parentShell);

	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("DB2 Logon");
		if (getParentShell() != null) {
			newShell.setImage(getParentShell().getImage());
		}
	}

	/**
	 * 添加自己的控件
	 */
	@Override
	protected Control createDialogArea(Composite parent) {

		Composite top = new Composite(parent, SWT.NONE);
		top.setLayout(new GridLayout(2, false));
		top.setLayoutData(new GridData(GridData.FILL_BOTH));

		CLabel label = new CLabel(top, SWT.CENTER);
		label.setImage(ImageFactory.loadImage(ImageFactory.LOGON_BIG));
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Group group = new Group(top, SWT.NONE);
		group.setLayout(new GridLayout(3, false));
		group.setLayoutData(new GridData(GridData.FILL_BOTH));

		new Label(group, SWT.RIGHT).setText("Username");
		usernameText = new Text(group, SWT.BORDER);
		usernameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		Button moreButton = new Button(group, SWT.PUSH);
		moreButton.setText("...");

		new Label(group, SWT.RIGHT).setText("Password");
		passwordText = new Text(group, SWT.BORDER | SWT.PASSWORD);
		passwordText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		new Label(group, SWT.RIGHT).setText("");

		new Label(group, SWT.RIGHT).setText("Database");
		databaseText = new Combo(group, SWT.BORDER|SWT.READ_ONLY);
		databaseText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		final Map<String, ConnectionInfo> connectionInfoMap = Bootstrap
				.getInstance().getConnectionInfoMap();
		for (String key : connectionInfoMap.keySet()) {
			databaseText.add(key);
		}
		if (databaseText.getItemCount()>0) {
			databaseText.select(0);
		}

		databaseText.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				String name = databaseText.getText();
				if (name.length() > 0) {
					ConnectionInfo info = connectionInfoMap.get(name);
					usernameText.setText(info.getUsername());
					passwordText.setText(info.getPassword());
				}
			}
		});

		Button plusButton = new Button(group, SWT.PUSH);
		plusButton.setText("+");
		plusButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ConnectionDialog connectionDialog = new ConnectionDialog(
						getShell());
				int ret = connectionDialog.open();
				if (ret== Window.OK) {
					databaseText.removeAll();
					for(String key: Bootstrap.getInstance().getConnectionInfoMap().keySet()){
						databaseText.add(key);
					}
					if (databaseText.getItemCount()>0) {
						databaseText.select(0);
					}
				}
			}
		});

		new Label(group, SWT.RIGHT).setText("Connect as");
		Combo connectAs = new Combo(group, SWT.BORDER);
		connectAs.setText("Normal");

		return parent;
	}

	/**
	 * 改变dialog的大小
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(380, 210);
	}

	/**
	 * 重写Dialog中按钮的个数
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		super.createButtonsForButtonBar(parent);
	}

	@Override
	protected void okPressed() {
		currentUsername = usernameText.getText().trim();
		currentPassword = passwordText.getText().trim();
		currentConnectionName = databaseText.getText();

		if (StringUtil.isBlank(currentUsername)) {
			MessageDialog.openInformation(getShell(), PRODUCT_NAME,
					"Please input Username.");
			return;

		}
		if (StringUtil.isBlank(currentPassword)) {
			MessageDialog.openInformation(getShell(), PRODUCT_NAME,
					"Please input Password.");
			return;
		}
		if (StringUtil.isBlank(currentConnectionName)) {
			MessageDialog.openInformation(getShell(), PRODUCT_NAME,
					"Please select one Database.");
			return;
		}

		Connection conn = null;
		try {
			conn = DbUtil.getConnection();
		} catch (Exception e) {
			MessageDialog.openWarning(getShell(), PRODUCT_NAME, e.getMessage());
			return;
		} finally {
			DbUtil.close(null, null, conn);
		}
		// save user input
		Bootstrap bs = Bootstrap.getInstance();
		ConnectionInfo info = bs.getConnectionInfoMap().get(currentConnectionName);
		info.setUsername(currentUsername);
		info.setPassword(currentPassword);
		bs.saveConnectionInfos();

		super.okPressed();
	}

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		LogonDialog log = new LogonDialog(shell);
		log.open();

	}
}
