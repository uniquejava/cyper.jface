/*
 * Copyright (C) 2004 by Friederich Kupzog Elektronik & Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 */
package hello.example.ktable.sort;

import hello.example.ktable.dao.MyDao;
import hello.example.ktable.util.HeaderRow;
import hello.example.ktable.util.Row;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import de.kupzog.ktable.KTableCellEditor;
import de.kupzog.ktable.KTableCellRenderer;
import de.kupzog.ktable.KTableSortedModel;
import de.kupzog.ktable.renderers.DefaultCellRenderer;
import de.kupzog.ktable.renderers.FixedCellRenderer;

/**
 * Shows how to create a table model that allows sorting the table! Also
 * demonstrates: - How the sorting works when spanned table cells exist (they
 * get "unspanned" ;-) - Shows that is it possible to fix also body cells (@see
 * de.kupzog.ktable.KTableModel#getFixedSelectableRowCount())<br>
 * 
 * 和Default相比，玩转SORT有以下几个步骤:<br>
 * 1.让Model继承KTableSortedModel<br>
 * 2.给m_FixedRenderer加上FixedCellRenderer.INDICATION_SORT样式<br>
 * 3.initialize()方法使用父类中的，不要覆盖了.<br>
 * 4.setContentAt()的调用必须放在initialize()方法调用之后<br>
 * 5.给table增加addCellSelectionListener（实现fixedCellSelected方法）<br>
 * 
 * @author Lorenz Maierhofer <lorenz.maierhofer@logicmindguide.com>
 */
public class SQLResultModelSort extends KTableSortedModel {

	private Random rand = new Random();
	private HashMap content = new HashMap();

	private KTableCellRenderer m_FixedRenderer = new FixedCellRenderer(
			FixedCellRenderer.STYLE_PUSH | FixedCellRenderer.INDICATION_SORT
					| FixedCellRenderer.INDICATION_FOCUS
					/*| FixedCellRenderer.INDICATION_CLICKED*/);

	private KTableCellRenderer m_DefaultRenderer = new DefaultCellRenderer(0);

	private List<Row> list = new ArrayList<Row>();
	private int resultCount;
	public String[] tableHeader = null;

	/**
	 * Initialize the underlying model
	 */
	public SQLResultModelSort() {
		setColumnWidth(0, 20);
		setColumnWidth(1, 40);
		list = new MyDao().querySql("select * from ORG");
		this.resultCount = list.size() - 1;
		for (int i = 0; i < list.size(); i++) {
			Map row = list.get(i);
			if (row instanceof HeaderRow) {
				HeaderRow headerRow = (HeaderRow) row;
				this.tableHeader = new String[headerRow.size()];
				headerRow.keySet().toArray(tableHeader);
				break;
			}
		}
		// before initializing, you probably have to set some member values
		// to make all model getter methods work properly.
		initialize();
		
		
		//原来这段setContentAt要放在initialize()之后啊。
		//因为setContentAt()需要调用mapRowIndexToModel()
		//而mapRowIndexToModel需要使用rowMapping
		//而rowMapping是在initialize()中实例化的
		//after initialize();
		int listIndex = -1;
		for (Row rowInList : list) {
			listIndex++;
			// if (listIndex == indicatorRowNumber) {
			// rowInList.put(Row.KEY_INDICATOR, ">");
			// } else {
			rowInList.put(Row.KEY_INDICATOR, "");
			// }

			int colj = 0;
			for (Iterator it = rowInList.keySet().iterator(); it.hasNext();) {
				String key = (String) it.next();
				setContentAt(colj, listIndex, rowInList.get(key));
				colj++;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.kupzog.ktable.KTableDefaultModel#doGetContentAt(int, int)
	 */
	public Object doGetContentAt(int col, int row) {
		String c = (String) content.get(col + "/" + row);
		if (c == null) {
			c = rand.nextInt(100) + " (" + col + "/" + row + ")";
			content.put(col + "/" + row, c);
		}
		return c;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.kupzog.ktable.KTableDefaultModel#doGetCellRenderer(int, int)
	 */
	public KTableCellRenderer doGetCellRenderer(int col, int row) {
		if (isHeaderCell(col, row))
			return m_FixedRenderer;

		return m_DefaultRenderer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.kupzog.ktable.KTableDefaultModel#doGetCellEditor(int, int)
	 */
	public KTableCellEditor doGetCellEditor(int col, int row) {
		// no celleditors:
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.kupzog.ktable.KTableDefaultModel#doSetContentAt(int, int,
	 * java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public void doSetContentAt(int col, int row, Object value) {
		// no editors, so not needed.
		content.put(col + "/" + row, value);
	}

	/**
	 * Implement also cell spans so that it can be demonstrated how the sorting
	 * algorithm works in this case:
	 * 
	 * @see de.kupzog.ktable.KTableDefaultModel#doBelongsToCell(int, int)
	 */
//	public Point doBelongsToCell(int col, int row) {
//		if ((col == 2 || col == 3) && !isFixedCell(col, row)) {
//			int newRow = row;
//			if ((row - getFixedRowCount()) % 2 == 1)
//				newRow--;
//			return new Point(2, newRow);
//		}
//		return new Point(col, row);
//	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.kupzog.ktable.KTableDefaultModel#getInitialColumnWidth(int)
	 */
	public int getInitialColumnWidth(int column) {
		return 125;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.kupzog.ktable.KTableDefaultModel#getInitialRowHeight(int)
	 */
	public int getInitialRowHeight(int row) {
		return 18;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.kupzog.ktable.KTableModel#getRowCount()
	 */
	public int doGetRowCount() {
		return resultCount + getFixedRowCount();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.kupzog.ktable.KTableModel#getColumnCount()
	 */
	public int doGetColumnCount() {
		return tableHeader.length;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.kupzog.ktable.KTableModel#getFixedRowCount()
	 */
	public int getFixedHeaderRowCount() {
		return 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.kupzog.ktable.KTableModel#getFixedColumnCount()
	 */
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
		return 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.kupzog.ktable.KTableModel#isColumnResizable(int)
	 */
	public boolean isColumnResizable(int col) {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.kupzog.ktable.KTableModel#getFirstRowHeight()
	 */
	public int getInitialFirstRowHeight() {
		return 22;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.kupzog.ktable.KTableModel#isRowResizable()
	 */
	public boolean isRowResizable(int row) {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.kupzog.ktable.KTableModel#getRowHeightMinimum()
	 */
	public int getRowHeightMinimum() {
		return 18;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.kupzog.ktable.KTableDefaultModel#doGetTooltipAt(int, int)
	 */
	public String doGetTooltipAt(int col, int row) {
		return "Tooltip for cell: " + col + "/" + row;
	}
}
