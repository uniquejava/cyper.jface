package hello.layout.aqua.dialog;

import static hello.layout.aqua.util.GridDataFactory.*;
import static hello.layout.aqua.ImageFactory.FILTER;
import static hello.layout.aqua.ImageFactory.REGISTER_SERVER;
import static hello.layout.aqua.ImageFactory.loadImage;
import hello.layout.aqua.ImageFactory;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

public class RegisterServerDialog extends Dialog {

	public RegisterServerDialog(Shell parentShell) {
		super(parentShell);

	}
	
	@Override
	protected void configureShell(Shell newShell) {
		// TODO Auto-generated method stub
		super.configureShell(newShell);
		newShell.setText("Register Server");
		newShell.setImage(getParentShell().getImage());
	}

	/**
	 * 添加自己的控件
	 */
	@Override
	protected Control createDialogArea(Composite parent) {

		TabFolder tabFolder = new TabFolder(parent, SWT.NONE);
		tabFolder.setLayoutData(new GridData(GridData.FILL_BOTH));

		TabItem generalTabItem = new TabItem(tabFolder, SWT.NONE);
		generalTabItem.setText("General");
		generalTabItem.setImage(loadImage(getShell().getDisplay(),ImageFactory.LOGON));

		{
			Composite generalPanel = new Composite(tabFolder, SWT.None);
			generalPanel.setLayout(new GridLayout(1, false));

			new Label(generalPanel, SWT.LEFT).setText("RDBMS:");

			Composite mainPanel4General = new Composite(generalPanel, SWT.NONE);
			mainPanel4General.setLayoutData(new GridData(GridData.FILL_BOTH));
			mainPanel4General.setLayout(new GridLayout(2, false));
			{
				List supportedDb = new List(mainPanel4General, SWT.BORDER);
				GridData sgd = new GridData(GridData.FILL_VERTICAL);
				sgd.widthHint = 150;
				supportedDb.setLayoutData(sgd);
				supportedDb.add("Oracle");
				supportedDb.add("DB2 UDB 8.1");
				supportedDb.add("MySQL");
			}
			{
				Composite fillblank = new Composite(mainPanel4General, SWT.NONE);
				{
					GridData gd = new GridData();
					gd.horizontalAlignment = SWT.FILL;
					gd.verticalAlignment = SWT.FILL;
					gd.grabExcessHorizontalSpace = true;
					fillblank.setLayoutData(gd);
					
					GridLayout gl = new GridLayout(2, false);
					gl.horizontalSpacing = 10;
					gl.marginWidth = 10;
					fillblank.setLayout(gl);
				}

				new Label(fillblank, SWT.LEFT).setText("Name:");
				Text nameText = new Text(fillblank, SWT.BORDER);
				nameText.setLayoutData(gd4text());

				new Label(fillblank, SWT.LEFT).setText("Host:");
				Text hostText = new Text(fillblank, SWT.BORDER);
				hostText.setLayoutData(gd4text());

				new Label(fillblank, SWT.LEFT).setText("Port:");
				Text portText = new Text(fillblank, SWT.BORDER);
				portText.setLayoutData(gd4text());

				new Label(fillblank, SWT.LEFT).setText("Database:");
				Text databaseText = new Text(fillblank, SWT.BORDER);
				databaseText.setLayoutData(gd4text());

				new Label(fillblank, SWT.LEFT).setText("User:");
				Text userText = new Text(fillblank, SWT.BORDER);
				userText.setLayoutData(gd4text());

				new Label(fillblank, SWT.LEFT).setText("password:");
				Text passwordText = new Text(fillblank, SWT.BORDER
						| SWT.PASSWORD);
				passwordText.setLayoutData(gd4text());
				
				Button rememberButton = new Button(fillblank, SWT.CHECK);
				rememberButton.setSelection(true);
				rememberButton.setText("记住密码");
				
				Button testButton = new Button(fillblank, SWT.PUSH|SWT.RIGHT);
				testButton.setText("测试连接");

			}

			new Label(generalPanel, SWT.LEFT).setText("Mounted Scripts:");

			Composite bottomPanel = new Composite(generalPanel, SWT.NONE);
			bottomPanel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			bottomPanel.setLayout(new GridLayout(3, false));
			new Label(bottomPanel, SWT.RIGHT).setText("Folder:");
			Text folderText = new Text(bottomPanel, SWT.BORDER);
			folderText.setLayoutData(gd4text());
			new Button(bottomPanel, SWT.NONE).setText("...");

			generalTabItem.setControl(generalPanel);
		}

		TabItem filter = new TabItem(tabFolder, SWT.NONE);
		filter.setText("Filter");
		filter.setImage(loadImage(getShell().getDisplay(), FILTER));

		return parent;
	}

	/**
	 * 改变dialog的大小
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(550, 550);
	}

	/**
	 * 重写Dialog中按钮的个数
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		super.createButtonsForButtonBar(parent);
	}
}
