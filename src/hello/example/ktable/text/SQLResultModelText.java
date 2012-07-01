/*
 * Copyright (C) 2004 by Friederich Kupzog Elektronik & Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 
 Author: Friederich Kupzog  
 fkmk@kupzog.de
 www.kupzog.de/fkmk
 */
package hello.example.ktable.text;

import hello.example.ktable.dao.MyDao;
import hello.example.ktable.util.BlankRow;
import hello.example.ktable.util.HeaderRow;
import hello.example.ktable.util.Row;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;

import de.kupzog.ktable.KTable;
import de.kupzog.ktable.KTableCellEditor;
import de.kupzog.ktable.KTableCellRenderer;
import de.kupzog.ktable.KTableDefaultModel;
import de.kupzog.ktable.editors.KTableCellEditorText;
import de.kupzog.ktable.renderers.FixedCellRenderer;
import de.kupzog.ktable.renderers.TextCellRenderer;

/**
 * @author Friederich Kupzog
 */
public class SQLResultModelText extends KTableDefaultModel {

	public static final int FIXED_HEADER_ROW_COUNT = 1;
	public String[] tableHeader = null;
	public int resultCount = 0;
	public int[] lastRowSelection = { 1 };

	private HashMap content = new HashMap();

	private final FixedCellRenderer m_fixedRenderer = new FixedCellRenderer(
			FixedCellRenderer.STYLE_FLAT
					| TextCellRenderer.INDICATION_FOCUS_ROW);

	private final TextCellRenderer m_textRenderer = new TextCellRenderer(
			TextCellRenderer.INDICATION_FOCUS_ROW);
	private List<Row> list = new ArrayList<Row>();

	/**
	 * @param refRowNumber
	 *            starts from 1, because 0 is header row.
	 */
	public List insertBlankRow(int refRowNumber) {
		// the first row is always header row
		// so blank row at least starts from 1;
		Assert.isTrue(refRowNumber > 0);
		// 因为有HiddenRow的存在，所以refRowNumber已经不准确了
		list.add(refRowNumber, new BlankRow(tableHeader));
		return list;
	}

	public List deleteRow(int refRowNumber) {
		// the first row is always header row
		// 这里传入的refRowNumber实际上是ktableIndex
		// 由此计算它在list中的实际位置很麻烦，我们最好根据row中数据的唯一 标识从list中删除之！
		// 根据下标似乎很不靠谱
		Assert.isTrue(refRowNumber > 0);
		// 因为有HiddenRow的存在，所以refRowNumber已经不准确了
		// 我们需要先把refRowNumber之前的hidden row展开，计算出list中需要删除行的实际位置.
		// int actualRefRowNumber = ModelUtil.getActualRefRowNumberInList(list,
		// refRowNumber);

		list.remove(refRowNumber);

		// 如果是非空白行，删除后还要再添加个马甲进去
		// if (!(deletedRow instanceof BlankRow)) {
		// list.add(actualRefRowNumber, new HiddenRow(deletedRow));
		// }

		return list;
	}

	/**
	 * Initialize the base implementation.
	 */
	public SQLResultModelText(KTable table) {
		setColumnWidth(0, 20);
		setColumnWidth(1, 40);
		refresh(table, new MyDao().querySql("selec * from ORG"), 1);
	}

	/**
	 * after list changed, we need to call this method. it will redraw the UI.
	 * 
	 */
	public void refresh(KTable table, List<Row> list, int indicatorRowNumber) {

		this.list = list;

		this.resultCount = list.size();// ModelUtil.getRowCountWithHeaderRow(list);

		for (int i = 0; i < list.size(); i++) {
			Map row = list.get(i);
			if (row instanceof HeaderRow) {
				HeaderRow headerRow = (HeaderRow) row;
				this.tableHeader = new String[headerRow.size()];
				headerRow.keySet().toArray(tableHeader);
				break;
			}
		}

		// listIndex就是list的遍历下标，值域为[0,list.size());
		// 每次循环都会自增一次.
		int listIndex = -1;
		for (Row rowInList : list) {
			listIndex++;
			if (listIndex == indicatorRowNumber) {
				rowInList.put(Row.KEY_INDICATOR, ">");
			} else {
				rowInList.put(Row.KEY_INDICATOR, "");
			}

			int colj = 0;
			for (Iterator it = rowInList.keySet().iterator(); it.hasNext();) {
				String key = (String) it.next();
				setContentAt(colj, listIndex, rowInList.get(key));
				colj++;
			}
		}
		// this.list = decoratedList;
		// before initializing, you probably have to set some member values
		// to make all model getter methods work properly.
		initialize();

		// we don't want the default foreground color on text cells,
		// so we change it:
		m_textRenderer.setForeground(Display.getCurrent().getSystemColor(
				SWT.COLOR_DARK_GREEN));

		// table.setSelection should be called after table has Model
		// so let's setModel for table first
		table.setModel(this);
		table.setSelection(2, indicatorRowNumber, false);

	}

	@Override
	public void initialize() {

	}

	// Content:
	public Object doGetContentAt(int col, int row) {
		// System.out.println("col "+col+" row "+row);
		String erg = (String) content.get(col + "/" + row);
		if (erg != null)
			return erg;
		return col + "/" + row;
	}

	/*
	 * overridden from superclass
	 */
	public KTableCellEditor doGetCellEditor(int col, int row) {
		if (col < getFixedColumnCount() || row < getFixedRowCount())
			return null;
		/*
		 * if (col % 3 == 1) { KTableCellEditorCombo e = new
		 * KTableCellEditorCombo(); e.setItems(new String[] { "First text",
		 * "Second text", "third text" }); return e; } else if (col % 3 == 2) {
		 * KTableCellEditorComboText e = new KTableCellEditorComboText();
		 * e.setItems(new String[] { "You choose", "or type", "a new content."
		 * }); return e; } else { return new KTableCellEditorText(); }
		 */
		return new KTableCellEditorText();
	}

	/*
	 * overridden from superclass
	 */
	public void doSetContentAt(int col, int row, Object value) {
		content.put(col + "/" + row, value);
	}

	// Table size:
	public int doGetRowCount() {
		return resultCount;
	}

	public int getFixedHeaderRowCount() {
		return FIXED_HEADER_ROW_COUNT;
	}

	public int doGetColumnCount() {
		return tableHeader.length; /* + getFixedColumnCount(); */
	}

	public int getFixedHeaderColumnCount() {
		
		//需要把此方法以下的部分抽象到父类中去，看着就蛋疼
		return 2;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.kupzog.ktable.KTableModel#getFixedSelectableRowCount()
	 */
	public int getFixedSelectableRowCount() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.kupzog.ktable.KTableModel#getFixedSelectableColumnCount()
	 */
	public int getFixedSelectableColumnCount() {
		return 0;
	}

	public boolean isColumnResizable(int col) {
		return true;
	}

	public boolean isRowResizable(int row) {
		return true;
	}

	public int getRowHeightMinimum() {
		return 18;
	}

	// Rendering
	public KTableCellRenderer doGetCellRenderer(int col, int row) {
		if (isFixedCell(col, row))
			return m_fixedRenderer;

		return m_textRenderer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.kupzog.ktable.KTableModel#belongsToCell(int, int)
	 */
	public Point doBelongsToCell(int col, int row) {
		// no cell spanning:
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.kupzog.ktable.KTableDefaultModel#getInitialColumnWidth(int)
	 */
	public int getInitialColumnWidth(int column) {
		return 90;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.kupzog.ktable.KTableDefaultModel#getInitialRowHeight(int)
	 */
	public int getInitialRowHeight(int row) {
		if (row == 0)
			return 22;
		return 18;
	}
}
