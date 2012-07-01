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
import hello.example.ktable.util.HeaderRow;
import hello.example.ktable.util.RefreshType;
import hello.example.ktable.util.Row;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;

import de.kupzog.ktable.KTable;
import de.kupzog.ktable.KTableCellEditor;
import de.kupzog.ktable.KTableCellRenderer;
import de.kupzog.ktable.KTableSortedModel;
import de.kupzog.ktable.editors.KTableCellEditorText;
import de.kupzog.ktable.renderers.FixedCellRenderer;
import de.kupzog.ktable.renderers.TextCellRenderer;

/**
 * @author Friederich Kupzog
 */
public class SQLResultModel extends KTableSortedModel {

	// 上面的表头行数
	public static final int FIXED_HEADER_ROW_COUNT = 1;

	// 表头，除了数据库表结构外，还包含indicator和行号那两列
	public String[] tableHeader = null;

	// 我们让resultCount=list.size()，即包含表头.
	public int resultCount = 0;

	// 保存上次选中的行的行号，这样指向新行时，容易将上次选中行的indicator清除，而不必遍历list.
	// public int[] lastRowSelection = { 1 };

	// 示例中就有的，ktable只认content，它要从中取出数据展示，key为col + '/' + row
	private HashMap content = new HashMap();

	public boolean updateRowIndicatorComplete = false;

	private final FixedCellRenderer m_fixedRenderer = new FixedCellRenderer(
			FixedCellRenderer.STYLE_FLAT
					| TextCellRenderer.INDICATION_FOCUS_ROW
					| FixedCellRenderer.INDICATION_SORT);

	private final TextCellRenderer m_textRenderer = new TextCellRenderer(
			TextCellRenderer.INDICATION_FOCUS_ROW);
	public List<Row> data = new ArrayList<Row>();

	/**
	 * Initialize the base implementation.
	 */
	public SQLResultModel(KTable table) {
		refreshWithSort(table, new MyDao().query("ORG"), 1, RefreshType.INIT);
		// refreshWithoutSort(table, new MyDao().query("ORG"), 1, true);
	}

	/**
	 * after list changed, we need to call this method. it will redraw the UI.
	 * 
	 */
	public void refreshWithSort(KTable table, List<Row> list,
			int indicatorRowNumber, RefreshType type) {
		this.data = list;
		this.resultCount = list.size();// ModelUtil.getRowCountWithHeaderRow(list);

		if (tableHeader == null) {
			setColumnWidth(0, 20);
			setColumnWidth(1, 40);
			for (int i = 0; i < list.size(); i++) {
				Map row = list.get(i);
				if (row instanceof HeaderRow) {
					HeaderRow headerRow = (HeaderRow) row;
					this.tableHeader = new String[headerRow.size()];
					headerRow.keySet().toArray(tableHeader);
					break;
				}
			}
		}
		if (rowMapping == null) {
			initRowMapping();
		}

		populateDataInNaturalOrder(list);

		int n = indicatorRowNumber;

		System.out.println("n=" + n);
		System.out.println("before=" + rowMapping);
		
		
		//完全由哥来控制rowMapping
		if (type == RefreshType.ADD) {
			rowMapping.add(0);// 扩容.
			//n之后的元素顺次后移
			for (int i = rowMapping.size() - 1; i >= n; i--) {
				rowMapping.set(i, rowMapping.get(i - 1));
			}
			//在n号位插入新的空白行的序号
			rowMapping.set(n - 1, rowMapping.size());
			
		}else if(type == RefreshType.SUBTRACT){
			//移除n号位的mapping
			int removed = (Integer) rowMapping.remove(n-1);
			
			//比n号位的值大于1的值全部减一.
			for (int i = 0; i < rowMapping.size(); i++) {
				int x = (Integer) rowMapping.get(i);
				if(x > removed){
					rowMapping.set(i, x-1);
				}
			}
		}
		System.out.println("after =" + rowMapping);

		// we don't want the default foreground color on text cells,
		// so we change it:
		m_textRenderer.setForeground(Display.getCurrent().getSystemColor(
				SWT.COLOR_DARK_GREEN));

		// table.setSelection should be called after table has Model
		// so let's setModel for table first
		table.setModel(this);
		table.setSelection(2, indicatorRowNumber, false);
		updateRowIndicator(indicatorRowNumber);
		table.redraw();

	}

	private void initRowMapping() {
		int numberOfElems = data.size() - 1;
		rowMapping = new Vector(numberOfElems);
		for (int i = 0; i < numberOfElems; i++) {
			rowMapping.add(i, new Integer(i + 1));
		}
	}

	private void populateDataInNaturalOrder(List<Row> list) {
		System.out.println("here =" + rowMapping);
		// 这里会修改rowMapping的值，如果容量不够，会自动扩容
		// Cyper版的setContentAt()，无需要rowMapping
		// listIndex就是list的遍历下标，值域为[0,list.size());
		// 每次循环都会自增一次.
		int listIndex = -1;
		for (Row rowInList : list) {
			listIndex++;
			// if (listIndex == indicatorRowNumber) {
			// rowInList.put(Row.KEY_INDICATOR, ">");
			// } else {
			// rowInList.put(Row.KEY_INDICATOR, "");
			// }

			int colj = 0;
			for (Iterator it = rowInList.keySet().iterator(); it.hasNext();) {
				String key = (String) it.next();
				// without row mapping!
				setNatureContentAt(colj, listIndex, rowInList.get(key));
				colj++;
			}
		}
		System.out.println("there=" + rowMapping);
	}

	public void updateRowIndicator(int rowNew) {
		System.out.println("updateRowIndex in Sort !!!!!!!!!!!!!!!!!!!!!!");
		// always keep the same row selected when doing sort.
		for (int i = 0; i < this.getRowCount(); i++) {
			String indicatorCell = (String) this.getContentAt(0, i);
			if (indicatorCell.equals(">")) {
				this.setContentAt(0, i, "");
				break;
			}
		}
		this.setContentAt(0, rowNew, ">");
		updateRowIndicatorComplete = true;
	}

	public void setNatureContentAt(int col, int row, Object value) {
		// super.setContentAt(col, row, value);
		doSetContentAt(col, row, value);
	}

	// Content:
	public Object doGetContentAt(int col, int row) {
		// System.out.println("col "+col+" row "+row);
		String erg = (String) content.get(col + "/" + row);
		if (erg != null)
			return erg;
		return col + "/" + row;
	}

	public List insertBlankRow() {
		System.out.println("beforeinsert=" + rowMapping);
		data.add(new BlankRow(tableHeader));
		System.out.println("after insert=" + rowMapping);
		return data;
	}

	public List deleteRow(int refRowNumber) {
		// the first row is always header row
		Assert.isTrue(refRowNumber > 0);
		int actualIndex = (Integer) rowMapping.get(refRowNumber-1);
		System.out.println("remove actualIndex=(" + actualIndex+") from data list");
		data.remove(actualIndex);
		return data;
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

		// 需要把此方法以下的部分抽象到父类中去，看着就蛋疼
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
