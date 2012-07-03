package hello.layout.aqua;

import static hello.layout.aqua.ImageFactory.LOGO;
import static hello.layout.aqua.ImageFactory.SCRIPT;
import static hello.layout.aqua.ImageFactory.SERVER;
import static hello.layout.aqua.util.GridDataFactory.gd4text;
import hello.layout.aqua.action.TabCloseAction;
import hello.layout.aqua.action.CommitSQLAction;
import hello.layout.aqua.action.ExecuteSQLAction;
import hello.layout.aqua.action.TabOpenAction;
import hello.layout.aqua.action.QueryDataAction;
import hello.layout.aqua.action.RegisterServerAction;
import hello.layout.aqua.action.RollbackSQLAction;
import hello.layout.aqua.action.TabSaveAction;
import hello.layout.aqua.scriptsView.ScriptsTreeContentProvider;
import hello.layout.aqua.scriptsView.ScriptsTreeLabelProvider;
import hello.layout.aqua.serverView.ServerTreeContentProvider;
import hello.layout.aqua.serverView.ServerTreeLabelProvider;
import hello.layout.aqua.serverView.node.NodeFactory;
import hello.layout.aqua.sqlwindow.SQLWindow;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolder2Adapter;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

public class CyperDataStudio extends ApplicationWindow {
	public final Display display = new Display();
	private SQLWindow sqlWindow = null;
	public TreeViewer serverTree;
	private IAction openSQLWindowAction;
	private IAction saveSQLAction;
	private IAction registerServerAction;
	private IAction executeSQLAction;
	private IAction commitSQLAction;
	private IAction rollbackSQLAction;
	private IAction closeSQLWindowTabAction;
	

	private static CyperDataStudio studio;

	public static CyperDataStudio getStudio() {
		return studio;
	}
	

	public SQLWindow getSqlWindow() {
		return sqlWindow;
	}


	public CyperDataStudio() {
		super(null);
		studio = this;

		registerServerAction = new RegisterServerAction(this);
		executeSQLAction = new ExecuteSQLAction(this);
		commitSQLAction = new CommitSQLAction(this);
		rollbackSQLAction = new RollbackSQLAction(this);
		openSQLWindowAction = new TabOpenAction(this);
		saveSQLAction = new TabSaveAction(this);
		closeSQLWindowTabAction = new TabCloseAction();
		
		this.addMenuBar();
		this.addToolBar(SWT.FLAT);
	}

	public static void main(String[] args) {
		CyperDataStudio studio = new CyperDataStudio();
		studio.setBlockOnOpen(true);
		studio.open();
	}

	protected Control createContents(Composite parent) {
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

			left.setWeights(new int[] { 100, 0 });

		}
		{

			// right top
			// ==================================================
			sqlWindow = new SQLWindow(form, SWT.CLOSE | SWT.BORDER);
			sqlWindow.setLayout(new FillLayout());
			sqlWindow.setSimple(true);
			sqlWindow.setMaximizeVisible(true);

			// SQLWindow sw = new SQLWindow(tabFolder);
			// sw.createNewTabItem("11111.sql");
			// sw.createNewTabItem("New SQL.sql");

			sqlWindow.setSelection(0);
			sqlWindow.addCTabFolder2Listener(new CTabFolder2Adapter() {
				@Override
				public void maximize(CTabFolderEvent event) {
					sqlWindow.setMaximized(true);
					CTabItem[] items = sqlWindow.getItems();
					if (items != null && items.length > 0) {
						for (int i = 0; i < items.length; i++) {
							SashForm right = (SashForm) items[i].getControl();
							right.setWeights(new int[] { 1000, 0 });
						}
					}
				}

				@Override
				public void restore(CTabFolderEvent event) {
					sqlWindow.setMinimized(false);
					sqlWindow.setMaximized(false);
					CTabItem[] items = sqlWindow.getItems();
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
		return parent;
	}

	private void createContextMenu(Composite parent) {
		MenuManager top = new MenuManager();
		top.add(new QueryDataAction(this));
		Menu popupMenu = top.createContextMenu(parent);
		serverTree.getTree().setMenu(popupMenu);
	}

	@Override
	protected void initializeBounds() {
		// setMaximized要生效，必须覆盖父类的这个方法，真是蛋疼!
	};

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText("PL/SQL Devloper for DB2 0.1(build20120703)");
		shell.setImage(ImageFactory.loadImage(display, LOGO));
		shell.setMaximized(true);
		shell.addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				System.out.println(e.keyCode);
				// 打开文件
				/*
				if (e.stateMask == SWT.CTRL && e.keyCode == 'o') {
					openSQLWindowAction.run();

					// 关闭当前SQL编辑器
				}*/ /*else if (e.stateMask == SWT.CTRL && e.keyCode == 'w') {
					System.out.println("wwwww");
					if (sqlWindow.getSelection() != null) {
						sqlWindow.getSelection().dispose();
					}
				}*/
			}
		});
		shell.forceFocus();
	}

	@Override
	protected ToolBarManager createToolBarManager(int style) {
		ToolBarManager toolbar = new ToolBarManager(style);
		toolbar.add(registerServerAction);
		toolbar.add(new Separator());
		toolbar.add(executeSQLAction);
		toolbar.add(new Separator());
		toolbar.add(commitSQLAction);
		toolbar.add(rollbackSQLAction);
		toolbar.add(new Separator());
		toolbar.add(openSQLWindowAction);
		toolbar.add(saveSQLAction);
		
		return toolbar;
	}

	@Override
	protected MenuManager createMenuManager() {
		MenuManager menuBar = new MenuManager();

		MenuManager fileMenu = new MenuManager("&File");
		MenuManager editMenu = new MenuManager("&Edit");
		MenuManager sessionMenu = new MenuManager("&Session");
		MenuManager windowMenu = new MenuManager("&Window");
		MenuManager helpMenu = new MenuManager("&Help");

		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		menuBar.add(sessionMenu);
		menuBar.add(windowMenu);
		menuBar.add(helpMenu);

		// 如果menu上没有添加任何action，menu是不会显示的
		//action只有放到了file menu上，快捷键才会激活.
		fileMenu.add(openSQLWindowAction);
		fileMenu.add(saveSQLAction);
		
		windowMenu.add(closeSQLWindowTabAction);

		return menuBar;
	}

}
