package hello.layout.aqua;

import static hello.layout.aqua.ImageFactory.LOGO;
import static hello.layout.aqua.ImageFactory.SCRIPT;
import static hello.layout.aqua.ImageFactory.SERVER;
import static hello.layout.aqua.util.GridDataFactory.gd4text;
import hello.layout.aqua.action.CommitSQLAction;
import hello.layout.aqua.action.ExecuteSQLAction;
import hello.layout.aqua.action.QueryDataAction;
import hello.layout.aqua.action.RegisterServerAction;
import hello.layout.aqua.action.RollbackSQLAction;
import hello.layout.aqua.scriptsView.ScriptsTreeContentProvider;
import hello.layout.aqua.scriptsView.ScriptsTreeLabelProvider;
import hello.layout.aqua.serverView.ServerTreeContentProvider;
import hello.layout.aqua.serverView.ServerTreeLabelProvider;
import hello.layout.aqua.serverView.node.NodeFactory;
import hello.layout.aqua.sqlwindow.SQLWindow;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolder2Adapter;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.ToolBar;

public class AquaDataStudio {
	public final Display display = new Display();
	public final Shell shell = new Shell(display);
	public CTabFolder tabFolder = null;
	public TreeViewer serverTree;

	public AquaDataStudio() {
		configureShell();
		createMenuBar();
		createToolBar();

		Composite parent = new Composite(shell, SWT.NONE);
		parent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		parent.setLayout(new FillLayout());

		createContents(parent);

	}

	private void createContents(Composite parent) {
		SashForm form = new SashForm(parent, SWT.None);
		form.setLayout(new FillLayout());
		{

			SashForm left = new SashForm(form, SWT.VERTICAL);
			left.setLayout(new FillLayout());

			// left top
			// ==================================================
			TabFolder tabFolder = new TabFolder(left, SWT.BOTTOM);
			tabFolder.setLayout(new FillLayout());

			// servers
			{
				TabItem serversTabItem = new TabItem(tabFolder, SWT.None);
				serversTabItem.setText("Servers");
				serversTabItem
						.setImage(ImageFactory.loadImage(display, SERVER));
				Composite panel = new Composite(tabFolder, SWT.NONE);
				panel.setLayout(new FillLayout());
				serverTree = new TreeViewer(panel);
				serverTree.setContentProvider(new ServerTreeContentProvider());
				serverTree.setLabelProvider(new ServerTreeLabelProvider());
				serverTree.setInput(NodeFactory.createNodes());
				createContextMenu(panel);
				
				serversTabItem.setControl(panel);
			}

			// scripts
			{

				TabItem scriptsTabItem = new TabItem(tabFolder, SWT.None);
				scriptsTabItem.setText("Scripts");
				scriptsTabItem
						.setImage(ImageFactory.loadImage(display, SCRIPT));
				Composite p = new Composite(tabFolder, SWT.NONE);
				p.setLayout(new FillLayout());
				TreeViewer tv = new TreeViewer(p);
				tv.setContentProvider(new ScriptsTreeContentProvider());
				tv.setLabelProvider(new ScriptsTreeLabelProvider());
				tv.setInput("anything");
				scriptsTabItem.setControl(p);
			}

			// =====================window list
			Composite leftBottom = new Composite(left, SWT.BORDER);
			GridLayout gl = new GridLayout(1, true);
			gl.verticalSpacing = 0;
			gl.marginWidth = 0;
			leftBottom.setLayout(gl);

			new Label(leftBottom, SWT.NONE).setText("window list");

			Button window1 = new Button(leftBottom, SWT.FLAT | SWT.LEFT);
			window1.setText("SQL Window- Query data of table DEPT");
			window1.setLayoutData(gd4text());

			Button window2 = new Button(leftBottom, SWT.TOGGLE | SWT.LEFT);
			window2.setText("Edit tabel DEPT");
			window2.setLayoutData(gd4text());

			left.setWeights(new int[] { 70, 30 });

		}
		{

			// right top
			// ==================================================
			tabFolder = new CTabFolder(form, SWT.CLOSE | SWT.BORDER);
			tabFolder.setLayout(new FillLayout());
			tabFolder.setSimple(false);
			tabFolder.setMaximizeVisible(true);

//			SQLWindow sw = new SQLWindow(tabFolder);
//			sw.createNewTabItem("11111.sql");
//			sw.createNewTabItem("New SQL.sql");

			tabFolder.setSelection(0);
			tabFolder.addCTabFolder2Listener(new CTabFolder2Adapter() {
				@Override
				public void maximize(CTabFolderEvent event) {
					tabFolder.setMaximized(true);
					CTabItem[] items = tabFolder.getItems();
					if (items != null && items.length > 0) {
						for (int i = 0; i < items.length; i++) {
							SashForm right = (SashForm) items[i].getControl();
							right.setWeights(new int[] { 1000, 0 });
						}
					}
				}

				@Override
				public void restore(CTabFolderEvent event) {
					tabFolder.setMinimized(false);
					tabFolder.setMaximized(false);
					CTabItem[] items = tabFolder.getItems();
					if (items != null && items.length > 0) {
						for (int i = 0; i < items.length; i++) {
							SashForm right = (SashForm) items[i].getControl();
							// show result window if it's hidden.
							if (right.getWeights()[1] == 0) {
								right.setWeights(SQLWindow.DEFAULT_WEIGHTS);
							}
						}
					}
				}
			});

		}
		form.setWeights(new int[] { 20, 80 });
	}

	private void createContextMenu(Composite parent) {
		MenuManager top = new MenuManager();
		top.add(new QueryDataAction(this));
		Menu popupMenu = top.createContextMenu(parent);
		serverTree.getTree().setMenu(popupMenu);
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
		AquaDataStudio test = new AquaDataStudio();
		test.open();
	}

	protected void configureShell() {
		shell.setText("Cyper Data Studio 1.0 preview");
		shell.setImage(ImageFactory.loadImage(display, LOGO));
		shell.setLayout(new GridLayout());
		shell.setMaximized(true);
	}

	protected void createToolBar() {
		Composite tool = new Composite(shell, SWT.None);
		tool.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false));
		ToolBar toolbar = new ToolBar(tool, SWT.None);
		ToolBarManager manager = new ToolBarManager(toolbar);
		manager.add(new RegisterServerAction(shell));
		manager.add(new ExecuteSQLAction(this));
		manager.add(new CommitSQLAction(this));
		manager.add(new RollbackSQLAction(this));

		manager.update(true);
		// ToolItem registerServer = new ToolItem(toolbar, SWT.PUSH);
		// registerServer.setImage(ImageFactory
		// .loadImage(display, REGISTER_SERVER));
		// registerServer.setToolTipText("Register Server(Insert)");
		toolbar.pack();
	}

	protected void createMenuBar() {
		// create main menu
		Menu main = new Menu(shell, SWT.BAR);
		// 主菜单的第一个菜单项
		MenuItem file = new MenuItem(main, SWT.CASCADE);
		file.setText("&File");

		Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);
		// 新建菜单项
		MenuItem newQuery = new MenuItem(fileMenu, SWT.PUSH);
		newQuery.setText("&New Query	Ctrl+N");
		newQuery.setAccelerator(SWT.CTRL + 'N');
		// 新建菜单项
		MenuItem openScript = new MenuItem(fileMenu, SWT.PUSH);
		openScript.setText("&Open Script	Ctrl+O");
		openScript.setAccelerator(SWT.CTRL + 'O');

		file.setMenu(fileMenu);

		// 主菜单的第二个菜单项
		MenuItem Edit = new MenuItem(main, SWT.CASCADE);
		Edit.setText("&Edit");
		shell.setMenuBar(main);
	}

}
