package hello.layout.aqua;

import static hello.layout.aqua.ImageFactory.LOGO;
import static hello.layout.aqua.ImageFactory.SCRIPT;
import static hello.layout.aqua.ImageFactory.SERVER;
import static hello.layout.aqua.util.GridDataFactory.gd4text;
import hello.cache.TableCache;
import hello.filter.TableFilter;
import hello.fms.action.QueryEmployeeAction;
import hello.layout.aqua.action.AboutAction;
import hello.layout.aqua.action.BeautifySQLAction;
import hello.layout.aqua.action.CommitSQLAction;
import hello.layout.aqua.action.ExecuteSQLAction;
import hello.layout.aqua.action.ExitAction;
import hello.layout.aqua.action.FillPlaceHolderAction;
import hello.layout.aqua.action.FindReplaceAction;
import hello.layout.aqua.action.LogonAction;
import hello.layout.aqua.action.TabNewAction;
import hello.layout.aqua.action.QueryDataAction;
import hello.layout.aqua.action.RollbackSQLAction;
import hello.layout.aqua.action.SelectionCommentAction;
import hello.layout.aqua.action.SelectionCommentUncommentAction;
import hello.layout.aqua.action.SelectionIndentAction;
import hello.layout.aqua.action.SelectionUncommentAction;
import hello.layout.aqua.action.SelectionUnindentAction;
import hello.layout.aqua.action.TabCloseAction;
import hello.layout.aqua.action.TabOpenAction;
import hello.layout.aqua.action.TabSaveAction;
import hello.layout.aqua.action.TextCopyAction;
import hello.layout.aqua.action.TextCutAction;
import hello.layout.aqua.action.TextPasteAction;
import hello.layout.aqua.action.TextRedoAction;
import hello.layout.aqua.action.TextSelectAllAction;
import hello.layout.aqua.action.TextUndoAction;
import hello.layout.aqua.dialog.LogonDialog;
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
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder2Adapter;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
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
	private TableFilter tableFilter;
	private SQLWindow sqlWindow = null;
	public TreeViewer serverTree;

	private IAction newSQLAction;
	private IAction openSQLWindowAction;
	private IAction saveSQLAction;
	private IAction exitAction;

	private IAction undoAction;
	private IAction redoAction;
	private IAction cutAction;
	private IAction copyAction;
	private IAction pasteAction;
	private IAction selectAllAction;
	private IAction findReplaceAction;
	private IAction beautifyAction;
	private IAction indentAction;
	private IAction unindentAction;
	private IAction commentAction;
	private IAction uncommentAction;
	private IAction commentSingleLineAction;

	// private IAction registerServerAction;
	private IAction logonAction;
	private IAction executeAction;
	private IAction commitAction;
	private IAction rollbackAction;

	private IAction closeSQLWindowTabAction;
	
	private IAction aboutAction;
	
	//FMS
	private IAction queryEmployeeAction;
	
	private IAction fillPlaceHolderAction;

	private static CyperDataStudio studio;

	public static CyperDataStudio getStudio() {
		return studio;
	}

	public SQLWindow getSqlWindow() {
		return sqlWindow;
	}

	public TableFilter getTableFilter() {
		return tableFilter;
	}

	public void setTableFilter(TableFilter tableFilter) {
		this.tableFilter = tableFilter;
	}

	public CyperDataStudio() {
		super(null);
		studio = this;

		// registerServerAction = new RegisterServerAction(this);
		logonAction = new LogonAction(this);
		executeAction = new ExecuteSQLAction(this);
		commitAction = new CommitSQLAction(this);
		rollbackAction = new RollbackSQLAction(this);

		newSQLAction = new TabNewAction(this);
		openSQLWindowAction = new TabOpenAction(this);
		saveSQLAction = new TabSaveAction(this);
		exitAction = new ExitAction();

		undoAction = new TextUndoAction(this);
		redoAction = new TextRedoAction(this);
		cutAction = new TextCutAction(this);
		copyAction = new TextCopyAction(this);
		pasteAction = new TextPasteAction(this);
		selectAllAction = new TextSelectAllAction(this);
		findReplaceAction = new FindReplaceAction(this);
		beautifyAction = new BeautifySQLAction(this);
		indentAction = new SelectionIndentAction(this);
		unindentAction = new SelectionUnindentAction(this);
		commentAction = new SelectionCommentAction(this);
		uncommentAction = new SelectionUncommentAction(this);
		commentSingleLineAction = new SelectionCommentUncommentAction(this);

		closeSQLWindowTabAction = new TabCloseAction();
		aboutAction = new AboutAction();
		
		queryEmployeeAction = new QueryEmployeeAction(this);
		fillPlaceHolderAction = new FillPlaceHolderAction(this);

		this.addMenuBar();
		this.addToolBar(SWT.FLAT);
	}

	public static void main(String[] args) {

		CyperDataStudio studio = new CyperDataStudio();

		// show logon dialog
		LogonDialog logonDialog = new LogonDialog(null);
		int ret = logonDialog.open();

		// not SWT.CANCEL!
		if (ret == Window.CANCEL) {
			System.exit(0);
		}

		// open main window
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
				refreshServerTree();
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

	public void refreshServerTree() {
		serverTree.setInput(NodeFactory.createNodes(
				LogonDialog.currentConnectionName, this.tableFilter));
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
		shell.setText("PL/SQL Developer for DB2(build20120712)");
		shell.setImage(ImageFactory.loadImage(display, LOGO));
		shell.setMaximized(true);
		shell.forceActive();
		shell.forceFocus();
		shell.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				TableCache.getInstance().persist();
			}
		});
	}

	@Override
	protected ToolBarManager createToolBarManager(int style) {
		ToolBarManager toolbar = new ToolBarManager(style);
		toolbar.add(logonAction);
		toolbar.add(new Separator());
		toolbar.add(executeAction);
		toolbar.add(new Separator());
		toolbar.add(commitAction);
		toolbar.add(rollbackAction);
		toolbar.add(new Separator());
		toolbar.add(newSQLAction);
		toolbar.add(openSQLWindowAction);
		toolbar.add(saveSQLAction);
		toolbar.add(new Separator());
		toolbar.add(undoAction);
		toolbar.add(redoAction);
		toolbar.add(new Separator());
		toolbar.add(cutAction);
		toolbar.add(copyAction);
		toolbar.add(pasteAction);
		toolbar.add(new Separator());
		toolbar.add(findReplaceAction);
		toolbar.add(new Separator());
		toolbar.add(beautifyAction);
		toolbar.add(indentAction);
		toolbar.add(unindentAction);
		toolbar.add(commentAction);
		toolbar.add(uncommentAction);
		toolbar.add(fillPlaceHolderAction);

		return toolbar;
	}

	@Override
	protected MenuManager createMenuManager() {
		MenuManager menuBar = new MenuManager();

		MenuManager fileMenu = new MenuManager("&File");
		MenuManager editMenu = new MenuManager("&Edit");
		MenuManager sessionMenu = new MenuManager("&Session");
		MenuManager windowMenu = new MenuManager("&Window");
		MenuManager fmsMenu = new MenuManager("F&MS");
		MenuManager helpMenu = new MenuManager("&Help");

		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		menuBar.add(sessionMenu);
		menuBar.add(windowMenu);
		menuBar.add(fmsMenu);
		menuBar.add(helpMenu);

		// 如果menu上没有添加任何action，menu是不会显示的
		// action只有放到了file menu上，快捷键才会激活.
		fileMenu.add(newSQLAction);
		fileMenu.add(openSQLWindowAction);
		fileMenu.add(saveSQLAction);
		fileMenu.add(new Separator());
		fileMenu.add(exitAction);

		editMenu.add(undoAction);
		editMenu.add(redoAction);
		editMenu.add(new Separator());
		editMenu.add(beautifyAction);
		editMenu.add(new Separator());
		editMenu.add(cutAction);
		editMenu.add(copyAction);
		editMenu.add(pasteAction);
		editMenu.add(selectAllAction);

		MenuManager selectionMenu = new MenuManager("Selection");
		selectionMenu.add(indentAction);
		selectionMenu.add(unindentAction);
		selectionMenu.add(commentAction);
		selectionMenu.add(uncommentAction);
		selectionMenu.add(commentSingleLineAction);

		editMenu.add(selectionMenu);

		editMenu.add(new Separator());
		editMenu.add(findReplaceAction);
		editMenu.add(fillPlaceHolderAction);

		sessionMenu.add(logonAction);
		sessionMenu.add(new Separator());
		sessionMenu.add(executeAction);
		sessionMenu.add(commitAction);
		sessionMenu.add(rollbackAction);

		windowMenu.add(closeSQLWindowTabAction);
		
		helpMenu.add(aboutAction);
		
		fmsMenu.add(queryEmployeeAction);
		
		
		

		return menuBar;
	}

}
