package hello.layout.aqua.dialog.connect;

import static hello.layout.aqua.ImageFactory.FILTER;
import static hello.layout.aqua.ImageFactory.loadImage;
import static hello.layout.aqua.util.GridDataFactory.gd4text;
import hello.layout.aqua.Bootstrap;
import hello.layout.aqua.ImageFactory;
import hello.layout.aqua.util.GridDataFactory;

import java.util.Iterator;
import java.util.Map;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

public class ConnectionDialog extends Dialog {

	private Text nameText;
	private Text hostText;
	private Text portText;
	private Text databaseText;
	private Text schemaText;
	private Text userText;
	private Text passwordText;
	private Text jdbcUrlText;

	public ConnectionDialog(Shell parentShell){
		super(parentShell);
	}

	@Override
	protected void configureShell(Shell newShell) {
		// TODO Auto-generated method stub
		super.configureShell(newShell);
		newShell.setText("New Connection");
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
		generalTabItem.setImage(loadImage(getShell().getDisplay(),
				ImageFactory.LOGON));

		{
			Composite generalPanel = new Composite(tabFolder, SWT.None);
			generalPanel.setLayout(new GridLayout(1, false));

			new Label(generalPanel, SWT.LEFT).setText("Connections");

			Composite mainPanel4General = new Composite(generalPanel, SWT.NONE);
			mainPanel4General.setLayoutData(new GridData(GridData.FILL_BOTH));
			mainPanel4General.setLayout(new GridLayout(2, false));
			{
				final List connectionCombo = new List(mainPanel4General,
						SWT.BORDER);
				GridData sgd = new GridData(GridData.FILL_VERTICAL);
				sgd.widthHint = 150;

				final Map<String, ConnectionInfo> connectionInfoMap = Bootstrap
						.getInstance().getConnectionInfoMap();
				for (Iterator it = connectionInfoMap.keySet().iterator(); it
						.hasNext();) {
					String key = (String) it.next();
					connectionCombo.add(key);
				}
				connectionCombo.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						if (connectionCombo.getSelectionCount() > 0) {
							String name = connectionCombo.getSelection()[0];
							ConnectionInfo info = connectionInfoMap.get(name);
							if (info != null) {
								nameText.setText(name);
								hostText.setText(info.getHost());
								portText.setText(info.getPort());
								databaseText.setText(info.getDatabase());
								schemaText.setText(info.getSchema());
								userText.setText(info.getUsername());
								passwordText.setText(info.getPassword());
								jdbcUrlText.setText(info.toDB2String());
							}
						}

					}
				});

				connectionCombo.setLayoutData(sgd);
			}
			{
				Composite fillblank = new Composite(mainPanel4General, SWT.NONE);
				{
					GridData gd = new GridData();
					gd.horizontalAlignment = SWT.FILL;
					gd.verticalAlignment = SWT.FILL;
					gd.grabExcessHorizontalSpace = true;
					fillblank.setLayoutData(gd);

					GridLayout gl = new GridLayout(3, false);
					gl.horizontalSpacing = 10;
					gl.marginWidth = 10;
					fillblank.setLayout(gl);
				}

				new Label(fillblank, SWT.LEFT).setText("Name");
				nameText = new Text(fillblank, SWT.BORDER);
				nameText.setLayoutData(gd4text());
				new Label(fillblank, SWT.LEFT).setText("");

				new Label(fillblank, SWT.LEFT).setText("Host/IP");
				hostText = new Text(fillblank, SWT.BORDER);
				hostText.setLayoutData(gd4text());
				new Label(fillblank, SWT.LEFT).setText("");

				new Label(fillblank, SWT.LEFT).setText("Port");
				portText = new Text(fillblank, SWT.BORDER);
				new Label(fillblank, SWT.LEFT).setText("");

				new Label(fillblank, SWT.LEFT).setText("Database");
				databaseText = new Text(fillblank, SWT.BORDER);
				databaseText.setLayoutData(gd4text());
				new Label(fillblank, SWT.LEFT).setText("");

				new Label(fillblank, SWT.LEFT).setText("Schema");
				schemaText = new Text(fillblank, SWT.BORDER);
				schemaText.setLayoutData(gd4text());
				new Label(fillblank, SWT.LEFT).setText("");

				new Label(fillblank, SWT.LEFT).setText("Username");
				userText = new Text(fillblank, SWT.BORDER);
				userText.setLayoutData(gd4text());
				new Label(fillblank, SWT.LEFT).setText("");

				new Label(fillblank, SWT.LEFT).setText("Password");
				passwordText = new Text(fillblank, SWT.BORDER | SWT.PASSWORD);
				passwordText.setLayoutData(gd4text());
				Button rememberButton = new Button(fillblank, SWT.CHECK);
				rememberButton.setSelection(true);
				rememberButton.setText("Remeber");

				Label jdbcUrlLabel = new Label(fillblank, SWT.LEFT);
				jdbcUrlLabel.setText("JDBC URL");
				jdbcUrlLabel.setLayoutData(GridDataFactory.rowspan(3));

				jdbcUrlText = new Text(fillblank, SWT.BORDER | SWT.MULTI
						| SWT.WRAP | SWT.READ_ONLY);
				GridData gd = new GridData(GridData.FILL_BOTH);
				gd.verticalSpan = 3;
				gd.horizontalSpan = 2;
				jdbcUrlText.setLayoutData(gd);

				new Label(fillblank, SWT.LEFT).setText("");
				Button testButton = new Button(fillblank, SWT.PUSH);
				testButton.setText("Test connection");
				new Label(fillblank, SWT.LEFT).setText("");

			}
			/*
			 * new Label(generalPanel, SWT.LEFT).setText("Mounted Scripts");
			 * 
			 * Composite bottomPanel = new Composite(generalPanel, SWT.NONE);
			 * bottomPanel.setLayoutData(new
			 * GridData(GridData.FILL_HORIZONTAL)); bottomPanel.setLayout(new
			 * GridLayout(3, false)); new Label(bottomPanel,
			 * SWT.RIGHT).setText("Folder"); Text folderText = new
			 * Text(bottomPanel, SWT.BORDER);
			 * folderText.setLayoutData(gd4text()); new Button(bottomPanel,
			 * SWT.NONE).setText("...");
			 */

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
		return new Point(550, 500);
	}

	/**
	 * 重写Dialog中按钮的个数
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		super.createButtonsForButtonBar(parent);
	}

	public static void main(String[] args) throws Exception {
		Display display = new Display();
		Shell shell = new Shell(display);
		ConnectionDialog log = new ConnectionDialog(shell);
		log.open();

	}
}
