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
package hello.example.ktable;

import hello.example.ktable.dao.MyDao;
import hello.example.ktable.util.BlankRow;
import hello.example.ktable.util.DecoratedRow;
import hello.example.ktable.util.HeaderRow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;

import de.kupzog.ktable.KTableCellEditor;
import de.kupzog.ktable.KTableCellRenderer;
import de.kupzog.ktable.KTableDefaultModel;
import de.kupzog.ktable.editors.KTableCellEditorText;
import de.kupzog.ktable.renderers.FixedCellRenderer;
import de.kupzog.ktable.renderers.TextCellRenderer;

/**
 * @author Friederich Kupzog
 */
public class SQLResultModel extends KTableDefaultModel {

	public String[] tableHeader = null;
	public int resultCount = 0;
	public int[] lastRowSelection = { 1 };

	private HashMap content = new HashMap();

	private final FixedCellRenderer m_fixedRenderer = new FixedCellRenderer(
			FixedCellRenderer.STYLE_FLAT
					| TextCellRenderer.INDICATION_FOCUS_ROW);

	private final TextCellRenderer m_textRenderer = new TextCellRenderer(
			TextCellRenderer.INDICATION_FOCUS_ROW);
	private List<LinkedHashMap<String, Object>> list = new ArrayList<LinkedHashMap<String, Object>>();

	/**
	 * @param refRowNumber
	 *            starts from 1, because 0 is header row.
	 */
	public void insertBlankRow(int refRowNumber) {
		//the first row is always header row
		//so blank row at least starts from 1;
		list.add(refRowNumber, new BlankRow(tableHeader));
	}

	/**
	 * Initialize the base implementation.
	 */
	public SQLResultModel() {
		setColumnWidth(0, 20);
		setColumnWidth(1, 40);

		MyDao dao = new MyDao();
		list = dao.query("EMPLOYEE");
		
		refresh();
	}

	public void refresh() {
		this.resultCount = list.size() - 1;
		for (int i = 0; i < list.size(); i++) {
			Map row = list.get(i);
			if (row instanceof HeaderRow) {
				HeaderRow headerRow = (HeaderRow) row;
				this.tableHeader = new String[headerRow.size()];
				headerRow.values().toArray(tableHeader);
				break;
			}
		}
	
		int rowNo = 0;
		int blankRowCount = 0;
		for (LinkedHashMap<String, Object> rowx : list) {
			Map row = null;
			if (rowx instanceof BlankRow) {
				row = rowx;
				blankRowCount++;
			}else{
				row = new DecoratedRow(rowx, rowNo, rowNo == 1,blankRowCount);
			}
			int colj = 0;
			for (Iterator it = row.keySet().iterator(); it.hasNext();) {
				String key = (String) it.next();
				setContentAt(colj, rowNo, row.get(key));
				colj++;
			}
			rowNo++;
		}

		// before initializing, you probably have to set some member values
		// to make all model getter methods work properly.
		initialize();

		// we don't want the default foreground color on text cells,
		// so we change it:
		m_textRenderer.setForeground(Display.getCurrent().getSystemColor(
				SWT.COLOR_DARK_GREEN));
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
		return resultCount + getFixedRowCount();
	}

	public int getFixedHeaderRowCount() {
		return 1;
	}

	public int doGetColumnCount() {
		return tableHeader.length; /* + getFixedColumnCount(); */
	}

	public int getFixedHeaderColumnCount() {
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
