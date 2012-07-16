package hello.layout.aqua.sqlwindow;

import static hello.example.ktable.util.ModelUtil.calcRefRowNumber;
import static hello.example.ktable.util.ModelUtil.getRowSelection;
import static hello.layout.aqua.ImageFactory.ADD;
import static hello.layout.aqua.ImageFactory.COLUMN_MODE;
import static hello.layout.aqua.ImageFactory.DOWN1;
import static hello.layout.aqua.ImageFactory.DOWN2;
import static hello.layout.aqua.ImageFactory.LOCK;
import static hello.layout.aqua.ImageFactory.MYTICK;
import static hello.layout.aqua.ImageFactory.NEXT;
import static hello.layout.aqua.ImageFactory.PREV;
import static hello.layout.aqua.ImageFactory.SQL_EDITOR;
import static hello.layout.aqua.ImageFactory.SUBTRACT;
import static hello.layout.aqua.ImageFactory.WYJ;
import static hello.layout.aqua.ImageFactory.loadImage;
import hello.example.ktable.sort.KTableSortOnClick;
import hello.example.ktable.sort.SortComparatorExample;
import hello.example.ktable.util.ModelUtil;
import hello.example.ktable.util.QueryResult;
import hello.example.ktable.util.RefreshType;
import hello.example.ktable.util.Row;
import hello.layout.aqua.CyperDataStudio;
import hello.layout.aqua.ImageFactory;
import hello.layout.aqua.action.SelectionIndentAction;
import hello.layout.aqua.action.SelectionUnindentAction;
import hello.layout.aqua.sqlwindow.editor.EventManager;
import hello.layout.aqua.sqlwindow.editor.MyDocument;
import hello.layout.aqua.sqlwindow.editor.MySourceViewerConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.text.IUndoManager;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.jface.text.TextViewerUndoManager;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.jface.text.source.VerticalRuler;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.VerifyKeyListener;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.kitten.core.C;
import org.kitten.core.util.ExcelWriter;
import org.kitten.core.util.FileUtil;

import de.kupzog.ktable.KTable;
import de.kupzog.ktable.KTableCellDoubleClickListener;
import de.kupzog.ktable.KTableCellSelectionListener;
import de.kupzog.ktable.KTableSortComparator;
import de.kupzog.ktable.SWTX;

public class SQLWindow extends CTabFolder {
	public SQLWindow(Composite parent, int style) {
		super(parent, style);
	}

	public static int seq = 1;
	public final static int[] DEFAULT_WEIGHTS = new int[] { 45, 55 };
	public final static int[] INITIAL_WEIGHTS = new int[] { 100, 0 };
	public List<CTabItem> tabItemList = new ArrayList<CTabItem>();

	public EventManager eventManager = new EventManager(this);
	public List<SourceViewer> textViewerList = new ArrayList<SourceViewer>();
	public List<MyDocument> documentList = new ArrayList<MyDocument>();
	public List<IUndoManager> undoManagerList = new ArrayList<IUndoManager>();
	public List<KTable> tableList = new ArrayList<KTable>();

	public KTable getTable() {
		int index = getSelectionIndex();
		if (index != -1) {
			return tableList.get(index);
		}
		return null;
	}

	public TextViewer getSourceViewer() {
		int index = getSelectionIndex();
		if (index != -1) {
			return textViewerList.get(index);
		}
		return null;
	}

	public MyDocument getDocument() {
		int index = getSelectionIndex();
		if (index != -1) {
			return documentList.get(index);
		}
		return null;
	}

	public IUndoManager getUndoManager() {
		int index = getSelectionIndex();
		if (index != -1) {
			return undoManagerList.get(index);
		}
		return null;
	}

	public CTabItem createNewTabItem(String title, boolean maximize) {
		// right top
		// sql editor
		final CTabItem tabItem = new CTabItem(this, SWT.None);
		tabItem.setText(title);
		tabItem.setImage(loadImage(SQL_EDITOR));

		SashForm right = new SashForm(this, SWT.VERTICAL | SWT.SMOOTH);
		right.SASH_WIDTH = 5;
		right.setLayout(new FillLayout());

		Composite sqlEditorPanel = new Composite(right, SWT.NONE);
		sqlEditorPanel.setLayout(new GridLayout());
		Composite top = new Composite(sqlEditorPanel, SWT.NONE);
		top.setLayout(new FillLayout());
		top.setLayoutData(new GridData(GridData.FILL_BOTH));
		// VerticalRuler是神马?
		final SourceViewer sourceViewer = new SourceViewer(top,
				new VerticalRuler(10), SWT.V_SCROLL | SWT.H_SCROLL);

		final Font font = new Font(getShell().getDisplay(), /* "Tahoma" */
		"Verdana", 10, SWT.NORMAL);
		sourceViewer.getTextWidget().setFont(font);
		sourceViewer.getTextWidget().addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				font.dispose();
			}
		});

		// 当在文本框中输入文字时
		// 可以通过MyDocument.get()取得输入的文字
		// 同时可以在MyDocument.documentAboutToBeChanged()和documentChanged监听文本的变化
		final MyDocument document = new MyDocument(this);
		sourceViewer.setDocument(document);

		// 语法着色 + 代码提示
		final SourceViewerConfiguration config = new MySourceViewerConfiguration();
		sourceViewer.configure(config);
		final StyledText text = sourceViewer.getTextWidget();

		// 使Alt+/也能触发代码提示
		// 如果要阻止键盘事件的默认行为，就要实现这个listener
		VerifyKeyListener verifyKeyListener = new VerifyKeyListener() {
			public void verifyKey(VerifyEvent event) {
				// Check for Alt+/
				if (event.stateMask == SWT.ALT && event.character == '/') {
					// Check if source viewer is able to perform operation
					if (sourceViewer
							.canDoOperation(SourceViewer.CONTENTASSIST_PROPOSALS))
						// Perform operation
						sourceViewer
								.doOperation(SourceViewer.CONTENTASSIST_PROPOSALS);
					// Veto this key press to avoid further processing
					event.doit = false;
				} else if (event.stateMask == SWT.SHIFT
						&& event.keyCode == SWT.TAB) {
					event.doit = false;
					// 更改shift + tab的默认行为为反缩进
					// 只有在选中了内容时shift+TAB才有用
					new SelectionUnindentAction(CyperDataStudio.getStudio())
							.run();

				} else if (event.keyCode == SWT.TAB) {
					event.doit = false;
					// 更改tab的默认行为为缩进
					if (text.getSelectionCount() > 0) {
						new SelectionIndentAction(CyperDataStudio.getStudio())
								.run();
					} else {
						// 如果没有选中内容，按tab则相当于按下几个连续空格.
						int offset = text.getCaretOffset();
						text.insert(Constants.TAB_SPACE);
						text.setCaretOffset(offset
								+ Constants.TAB_SPACE.length());
					}
				}
			}
		};
		sourceViewer.appendVerifyKeyListener(verifyKeyListener);

		final IUndoManager undoManager = new TextViewerUndoManager(100);
		undoManager.connect(sourceViewer);

		// read here:
		// http://www.eclipse.org/articles/StyledText%201/article1.html
		// 需要要Tab=\t转成空格.
		text.addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(KeyEvent e) {

				/*
				 * if (e.stateMask == SWT.CTRL && e.keyCode == 'a') { // 实现Ctrl
				 * + A text.selectAll();
				 * 
				 * } else
				 */if (e.stateMask == SWT.CTRL && e.keyCode == 'd') {
					// 模拟实现Eclipse中Ctrl+D的功能
					// note that select.y is the length of the selection
					Point select = text.getSelectionRange();
					int startLine = text.getLineAtOffset(select.x);
					int startLineOffset = text.getOffsetAtLine(startLine);
					int endLine = text.getLineAtOffset(select.x + select.y);
					int lineCount = endLine - startLine + 1;
					for (int i = 0; i < lineCount; i++) {
						String line = text.getLine(startLine);
						// 尝试删除行(包含行尾的\r\n，如果有，没有会报异常)
						try {
							text.replaceTextRange(startLineOffset,
									line.length() + 2, "");
						} catch (Exception e2) {
							text.replaceTextRange(startLineOffset,
									line.length(), "");
						}
					}
				}
				/*
				 * else if (e.stateMask == SWT.CTRL && e.keyCode == 'z') {
				 * undoAction.run(); } else if (e.stateMask == SWT.CTRL &&
				 * e.keyCode == 'y') { redoAction.run(); } else if (e.stateMask
				 * == SWT.CTRL && e.keyCode == 'f') { findAction.run(); } else
				 * if (e.stateMask == SWT.CTRL && e.keyCode == 'o') {
				 * openAction.run(); } else if (e.stateMask == SWT.CTRL &&
				 * e.keyCode == 's') { saveAction.run(); }
				 */
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});

		Composite statusLine = new Composite(sqlEditorPanel, SWT.BORDER);
		statusLine.setLayout(new GridLayout(5, false));
		statusLine.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		Text position = new Text(statusLine, SWT.NONE | SWT.READ_ONLY);
		position.setText("20:10");
		Text se = new Text(statusLine, SWT.NONE | SWT.READ_ONLY);
		se.setText("|");
		Text mode = new Text(statusLine, SWT.NONE | SWT.READ_ONLY);
		mode.setText("INS");
		Text se2 = new Text(statusLine, SWT.NONE | SWT.READ_ONLY);
		se2.setText("|");
		Text message = new Text(statusLine, SWT.NONE | SWT.READ_ONLY);
		message.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		message.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				.format(new Date()) + " Script executed.");

		// =====================result window
		ViewForm viewForm = new ViewForm(right, SWT.NONE);
		viewForm.setTopCenterSeparate(true);

		// tool bar
		ToolBar toolbar = new ToolBar(viewForm, SWT.FLAT);
		final ToolItem lock = new ToolItem(toolbar, SWT.PUSH);
		lock.setImage(loadImage(LOCK));

		final ToolItem add = new ToolItem(toolbar, SWT.PUSH);
		add.setImage(loadImage(ADD));

		final ToolItem sub = new ToolItem(toolbar, SWT.PUSH);
		sub.setImage(loadImage(SUBTRACT));

		final ToolItem save = new ToolItem(toolbar, SWT.PUSH);
		save.setImage(loadImage(MYTICK));

		ToolItem down1 = new ToolItem(toolbar, SWT.PUSH);
		down1.setImage(loadImage(DOWN1));

		ToolItem down2 = new ToolItem(toolbar, SWT.PUSH);
		down2.setImage(loadImage(DOWN2));

		ToolItem wyj = new ToolItem(toolbar, SWT.PUSH);
		wyj.setImage(loadImage(WYJ));
		ToolItem cm = new ToolItem(toolbar, SWT.PUSH);
		cm.setImage(loadImage(COLUMN_MODE));
		ToolItem prev = new ToolItem(toolbar, SWT.PUSH);
		prev.setImage(loadImage(PREV));
		ToolItem next = new ToolItem(toolbar, SWT.PUSH);
		next.setImage(loadImage(NEXT));

		final ToolItem excel = new ToolItem(toolbar, SWT.PUSH);
		excel.setImage(loadImage(ImageFactory.EXCEL));

		final ToolItem sql = new ToolItem(toolbar, SWT.PUSH);
		sql.setImage(loadImage(ImageFactory.SQL));

		viewForm.setTopLeft(toolbar);

		// swt table

		Composite contentPanel = new Composite(viewForm, SWT.NONE);
		// contentPanel.setLayout(new GridLayout(1, true));
		contentPanel.setLayout(new FillLayout());

		final KTable table = new KTable(contentPanel, SWT.FULL_SELECTION
				| SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL
				| SWTX.FILL_WITH_LASTCOL | SWTX.EDIT_ON_KEY);

		final SQLResultModel model = new SQLResultModel(table);

		final KTableSortOnClick sort = new KTableSortOnClick(table,
				new SortComparatorExample(model, -1,
						KTableSortComparator.SORT_NONE));
		table.addCellSelectionListener(new KTableCellSelectionListener() {
			public void updateRowIndicator(int rowNew) {
				SQLResultModel model = (SQLResultModel) table.getModel();
				for (int i = 0; i < model.getRowCount(); i++) {
					String indicatorCell = (String) model.getContentAt(0, i);
					if (indicatorCell.equals(">")) {
						model.setContentAt(0, i, "");
						break;
					}
				}
				model.setContentAt(0, rowNew, ">");
				table.redraw();
			}

			public void cellSelected(int col, int row, int statemask) {
				int actualRow = model.mapRowIndexToModel(row);
				System.out.println("checkList=" + model.checkList);
				updateModifiedCell(model, col + "/" + actualRow);
				// shell.setText("[" + row + "," + col + "]");
				// System.out
				// .println("Cell [" + col + ";" + row + "] selected11.");
				// 点表头的时候row竟然不为0，
				// 这是因为在KTableSortOnClick中使用了m_Table.setSelection(2, i, false);
				// 更改的点cell的事件
				// FIXME 这段我都看不懂写的啥了.
				// 如果在refresh的时候已经设定了rowIndicator
				/*
				 * if (model.updateRowIndicatorInRefreshMethod && sort.called )
				 * {
				 * System.out.println("model.updateRowIndicatorInRefreshMethod"
				 * ); model.updateRowIndicatorInRefreshMethod = false; return; }
				 * else
				 */
				if (row != 0) {
					updateRowIndicator(row);
				}

			}

			public void fixedCellSelected(int col, int row, int statemask) {
				updateModifiedCell(model, null);
				if (row != 0) {
					updateRowIndicator(row);
				}
			}
		});
		table.addCellSelectionListener(sort);
		table.addCellDoubleClickListener(new KTableCellDoubleClickListener() {
			@Override
			public void fixedCellDoubleClicked(int coxl, int rowx, int statemask) {
				updateModifiedCell(model, null);
			}

			@Override
			public void cellDoubleClicked(int col, int row, int statemask) {
				// System.out.println("Cell [" + col + ";" + row
				// + "] double clicked.");
				int actualRow = model.mapRowIndexToModel(row);
				updateModifiedCell(model, col + "/" + actualRow);
			}
		});

		viewForm.setContent(contentPanel);

		Listener listener = new Listener() {
			@Override
			public void handleEvent(Event event) {
				SQLResultModel model = (SQLResultModel) table.getModel();
				if (event.widget == add) {
					int refRowNumber = calcRefRowNumber(table);

					// 在此处应该让编辑状态的cell先失去焦点
					table.setSelection(2, refRowNumber, false);
					updateModifiedCell(model, null);

					// 当插入空白行后，row indicator是否指向新的空白行?Yes,!PLD是这么做的
					// 新插入的空白行没有行号
					// 其它行的行号保持不变
					List newList = model.insertBlankRow();
					sort.called = false;
					model.refreshWithSort(newList, refRowNumber,
							RefreshType.ADD);
					// Rectangle r = table.getCellRect(2, refRowNumber);
					// model.getCellEditor(2, refRowNumber).open(table, 2,
					// refRowNumber, r);

				} else if (event.widget == sub) {
					int rowSelectoin = getRowSelection(table);

					// 在此处应该让编辑状态的cell先失去焦点
					table.setSelection(2, rowSelectoin, false);
					updateModifiedCell(model, null);

					// 当删除的是空白行，OK，直接删除,反正它没有行号。
					// 当删除的是普通行，此时其它行的行号保持不变，但删除这行使行号变得不连续了
					if (rowSelectoin != ModelUtil.NO_SELECTION) {
						List newList = model.deleteRow(rowSelectoin);
						sort.called = false;
						model.refreshWithSort(newList, rowSelectoin,
								RefreshType.SUBTRACT);
					}
				} else if (event.widget == save) {
					// 在此处应该让编辑状态的cell先失去焦点
					int rowSelectoin = getRowSelection(table);
					table.setSelection(2, rowSelectoin, false);
					updateModifiedCell(model, null);

				} else if (event.widget == excel) {
					QueryResult queryResult = model.getQueryResult();
					
					FileDialog d = new FileDialog(table.getShell(), SWT.SAVE);
					d.setFileName(queryResult.getTableName());
					d.setFilterExtensions(new String[] { "*.xls" });
					String file = d.open();

					if (file != null) {
						ExcelWriter w = new ExcelWriter();
						w.addHead(queryResult.getTableHeaders());
						List<String[]> data = queryResult.getData();
						
						for (String[] row: data) {
							w.addRow((row));
						}
						try {
							w.save(new FileOutputStream(file));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} else if (event.widget == sql) {
					QueryResult queryResult = model.getQueryResult();

					FileDialog d = new FileDialog(table.getShell(), SWT.SAVE);
					d.setFileName(queryResult.getTableName());
					d.setFilterExtensions(new String[] { "*.sql" });

					String file = d.open();

					StringBuffer sb = new StringBuffer();
					if (file != null) {
						String tableName = queryResult.getTableName();
						String[] tableHeaders = queryResult.getTableHeaders();
						List<Row> data = queryResult.getDataRow();
						String[] types = queryResult.getTypes();
						System.out.println(StringUtils.join(types,","));
						String[] value = new String[queryResult
								.getColumnCount() + 2];
						String[] usefulValue = new String[queryResult
								.getColumnCount()];
						String[] wrappedValue = new String[usefulValue.length];
						for (Row row : data) {
							row.values().toArray(value);
							usefulValue = (String[]) ArrayUtils.subarray(value,
									2, value.length);
							for (int i = 0; i < usefulValue.length; i++) {
								if (types[i].indexOf("TIMESTAMP") != -1) {
									wrappedValue[i] = "CURRENT TIMESTAMP";
								} else {
									if (usefulValue[i] == null){
										wrappedValue[i] = "null";
									} else if (types[i].indexOf("INT") != -1
											|| types[i].indexOf("DECIMAL") != -1) {
										wrappedValue[i] = usefulValue[i];
									} else {
										wrappedValue[i] = "'" + usefulValue[i]
												+ "'";
									}
								}
							}

							StringBuffer line = new StringBuffer();
							line.append("INSERT INTO " + tableName + "(");
							line.append(StringUtils.join(tableHeaders, ","));
							line.append(") values (");
							//join的时候，null值会被忽略！导致出现连续多个逗号的情况.
							line.append(StringUtils.join(wrappedValue, ","));
							line.append(");" + C.LS);
							sb.append(line);
						}
						try {
							FileUtil.setFileContent(new File(file),
									sb.toString(), "UTF-8");
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}

			}
		};
		add.addListener(SWT.Selection, listener);
		sub.addListener(SWT.Selection, listener);
		save.addListener(SWT.Selection, listener);
		excel.addListener(SWT.Selection, listener);
		sql.addListener(SWT.Selection, listener);

		viewForm.setContent(contentPanel);

		// ----important------------
		if (!maximize) {
			right.setWeights(DEFAULT_WEIGHTS);
		} else {
			right.setWeights(INITIAL_WEIGHTS);
			setMaximized(true);
		}

		tabItem.setControl(right);

		tabItemList.add(tabItem);
		textViewerList.add(sourceViewer);
		documentList.add(document);
		undoManagerList.add(undoManager);
		tableList.add(table);

		tabItem.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				System.out.println("I am disposed.");
				tabItemList.remove(tabItem);
				textViewerList.remove(sourceViewer);
				documentList.remove(document);
				undoManagerList.remove(undoManager);
				tableList.remove(table);
			}
		});

		this.setSelection(tabItem);
		// fix bug 打开SQL Tab时，editor没有自动获得输入焦点，还得点一下，不爽。
		text.setFocus();

		return tabItem;
	}

	/**
	 * 持久化先前可能修改过的单元格.同时将将要修改的单元格newKey加入观察列表.
	 * 
	 * @param model
	 * @param newKey
	 */
	private void updateModifiedCell(final SQLResultModel model, String newKey) {
		if (model.checkList.size() > 0) {
			for (Iterator it = model.checkList.iterator(); it.hasNext();) {
				try {
					String key = (String) it.next();
					int slash = key.indexOf("/");
					int col = Integer.parseInt(key.substring(0, slash));
					int row = Integer.parseInt(key.substring(slash + 1));
					String newContent = (String) model.content.get(key);
					Row rowData = (Row) (model.data.get(row));
					String oldContent = (String) rowData
							.get(model.tableHeader[col]);
					System.out.println("oldContent=" + oldContent
							+ ",newContent=" + newContent);
					if (!newContent.equals(oldContent)) {
						rowData.put(model.tableHeader[col], newContent);
						System.out.println(model.checkList);
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}
		model.checkList.clear();
		if (newKey != null) {
			model.checkList.add(newKey);
		}
	}
}
