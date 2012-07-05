package hello.layout.aqua.dialog.connect;

import static hello.layout.aqua.util.GridDataFactory.gd4text;
import hello.layout.aqua.Bootstrap;
import hello.layout.aqua.ImageFactory;
import hello.layout.aqua.sqlwindow.Constants;
import hello.layout.aqua.util.DbUtil;
import hello.layout.aqua.util.GridDataFactory;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
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
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.kitten.core.util.DateUtil;
import org.kitten.core.util.StringUtil;
import org.springframework.beans.BeanUtils;

public class ConnectionDialog extends Dialog {

	private Text nameText;
	private Text hostText;
	private Text portText;
	private Text databaseText;
	private Text schemaText;
	private Text usernameText;
	private Text passwordText;
	private Text jdbcUrlText;
	private List connectionCombo;
	final Map<String, ConnectionInfo> connectionInfoMap = getMap();

	public ConnectionDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected void configureShell(Shell newShell) {
		// TODO Auto-generated method stub
		super.configureShell(newShell);
		newShell.setText("New Connection");
		newShell.setImage(getParentShell().getImage());
	}

	private Map<String, ConnectionInfo> getMap() {
		return Bootstrap.getInstance().getConnectionInfoMap();
	}

	private void createContextMenuForList(Composite parent) {
		MenuManager top = new MenuManager();
		top.add(new RemoveConnectionInfoAction(this));
		top.add(new CloneConnectionInfoAction(this));
		Menu popupMenu = top.createContextMenu(parent);
		connectionCombo.setMenu(popupMenu);
	}

	public void removeConnectionInfo() {
		if (connectionCombo.getSelectionCount() > 0) {
			String selection = connectionCombo.getSelection()[0];
			connectionCombo.remove(selection);
			connectionInfoMap.remove(selection);
			clearRightForm();
		}
	}

	public void cloneConnectionInfo() {
		if (connectionCombo.getSelectionCount() > 0) {
			String selection = connectionCombo.getSelection()[0];
			ConnectionInfo info = connectionInfoMap.get(selection);
			ConnectionInfo clonedInfo = new ConnectionInfo();
			BeanUtils.copyProperties(info, clonedInfo);
			clonedInfo.setName(info.getName() + "_"
					+ DateUtil.getFormatedDate("HHmmss"));
			connectionInfoMap.put(clonedInfo.getName(), clonedInfo);

			// refresh list
			connectionCombo.removeAll();
			for (String key : connectionInfoMap.keySet()) {
				connectionCombo.add(key);
			}
			// select the last one(newly cloned one)
			connectionCombo.select(connectionInfoMap.keySet().size() - 1);
			populateRightForm();
		}
	}

	private void populateRightForm() {
		if (connectionCombo.getSelectionCount() > 0) {
			String name = connectionCombo.getSelection()[0];
			ConnectionInfo info = connectionInfoMap.get(name);
			if (info != null) {
				nameText.setText(info.getName());
				hostText.setText(info.getHost());
				portText.setText(info.getPort());
				databaseText.setText(info.getDatabase());
				schemaText.setText(info.getSchema());
				usernameText.setText(info.getUsername());
				passwordText.setText(info.getPassword());
				jdbcUrlText.setText(info.toDB2String());
			}
		}
	}

	private void clearRightForm() {
		nameText.setText("");
		hostText.setText("");
		portText.setText("");
		databaseText.setText("");
		schemaText.setText("");
		usernameText.setText("");
		passwordText.setText("");
		jdbcUrlText.setText("");
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		TabFolder tabFolder = new TabFolder(parent, SWT.NONE);
		tabFolder.setLayoutData(new GridData(GridData.FILL_BOTH));

		TabItem generalTabItem = new TabItem(tabFolder, SWT.NONE);
		generalTabItem.setText("General");
		generalTabItem.setImage(ImageFactory.loadImage(getShell().getDisplay(),
				ImageFactory.LOGON));

		{
			Composite generalPanel = new Composite(tabFolder, SWT.None);
			generalPanel.setLayout(new GridLayout(1, false));

			new Label(generalPanel, SWT.LEFT).setText("Connections");

			Composite mainPanel4General = new Composite(generalPanel, SWT.NONE);
			mainPanel4General.setLayoutData(new GridData(GridData.FILL_BOTH));
			mainPanel4General.setLayout(new GridLayout(2, false));
			{
				connectionCombo = new List(mainPanel4General, SWT.BORDER);
				GridData sgd = new GridData(GridData.FILL_VERTICAL);
				sgd.widthHint = 150;

				for (String key : connectionInfoMap.keySet()) {
					connectionCombo.add(key);
				}
				createContextMenuForList(getShell());

				connectionCombo.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						populateRightForm();
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
				nameText.addModifyListener(new ModifyListener() {
					@Override
					public void modifyText(ModifyEvent e) {
						// final Map<String, ConnectionInfo> connectionInfoMap =
						// Bootstrap
					}
				});
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
				usernameText = new Text(fillblank, SWT.BORDER);
				usernameText.setLayoutData(gd4text());
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
				testButton.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						ConnectionInfo info = populateConnectionInfo();
						if (info != null) {
							Connection conn = null;
							try {
								conn = DbUtil.getConnection(info);
							} catch (Exception ex) {
								//test not ok
								MessageDialog.openError(getShell(),
										Constants.PRODUCT_NAME, ex.getMessage());
							} finally {
								if (conn != null) {
									//test ok
									DbUtil.close(null, null, conn);
									MessageDialog.openInformation(getShell(),Constants.PRODUCT_NAME,"Congratulations!");
								}
							}

						}
					}
				});
				new Label(fillblank, SWT.LEFT).setText("");

			}
			generalTabItem.setControl(generalPanel);
		}

		TabItem filter = new TabItem(tabFolder, SWT.NONE);
		filter.setText("Filter");
		filter.setImage(ImageFactory.loadImage(getShell().getDisplay(),
				ImageFactory.FILTER));

		return parent;
	}

	/**
	 * 改变dialog的大小
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(550, 500);
	}

	@Override
	protected void okPressed() {
		ConnectionInfo info = populateConnectionInfo();
		if (info != null) {
			// 如果列表框有选中，且右侧的表单数据有修改,则为更新操作
			// 先移除key，后面再添加
			if (connectionCombo.getSelectionCount() > 0) {
				String key = connectionCombo.getSelection()[0];
				connectionInfoMap.remove(key);
			}
			connectionInfoMap.put(info.getName(), info);
			Bootstrap.getInstance().saveConnectionInfos();
		}
		super.okPressed();
	}

	private ConnectionInfo populateConnectionInfo() {
		ConnectionInfo info = null;
		String name = nameText.getText();
		String host = hostText.getText();
		String port = portText.getText();
		String database = databaseText.getText();
		String schema = schemaText.getText();
		String username = usernameText.getText();
		String password = passwordText.getText();

		java.util.List<String> msg = new ArrayList<String>();
		if (StringUtil.isBlank(name)) {
			msg.add("name");
		}
		if (StringUtil.isBlank(host)) {
			msg.add("host");
		}
		if (StringUtil.isBlank(port)) {
			msg.add("port");
		}
		if (StringUtil.isBlank(database)) {
			msg.add("database");
		}
		if (msg.size() > 0) {
//			MessageDialog.openWarning(getShell(), Constants.PRODUCT_NAME,
//					msg.toString() + " cannot be empty.");
		} else {
			info = new ConnectionInfo();
			info.setName(name);
			info.setHost(host);
			info.setPort(port);
			info.setDatabase(database);
			info.setSchema(schema);
			info.setUsername(username);
			info.setPassword(password);
		}
		return info;
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
