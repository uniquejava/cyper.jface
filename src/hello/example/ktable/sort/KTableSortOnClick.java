package hello.example.ktable.sort;

import de.kupzog.ktable.KTable;
import de.kupzog.ktable.KTableCellSelectionAdapter;
import de.kupzog.ktable.KTableSortComparator;
import de.kupzog.ktable.KTableSortedModel;


/**
 * This class provides the code that makes the table sort when the user
 * clicks on the table header.
 * 
 */
public class KTableSortOnClick extends KTableCellSelectionAdapter {

	KTable m_Table;
	KTableSortComparator m_SortComparator; 
	public boolean called = false;
	
	
	public KTableSortOnClick(KTable table, KTableSortComparator comparator) {
		m_Table = table;
		m_SortComparator = comparator;
	}

	/**
	 * Implements sorting behavior when clicking on the fixed header row.
	 */
	public void fixedCellSelected(int col, int row, int statemask) {
		called = true;
		
		if (m_Table.getModel() instanceof KTableSortedModel) {
			KTableSortedModel model = (KTableSortedModel) m_Table.getModel();
			
			// implement the sorting when clicking on the header.
			if (row<model.getFixedHeaderRowCount() && 
					col>=model.getFixedHeaderColumnCount()) {
				int type = KTableSortComparator.SORT_UP;
				if (model.getSortColumn()==col) {
					if (model.getSortState()==KTableSortComparator.SORT_UP) {
						type = KTableSortComparator.SORT_DOWN;
					} else if (model.getSortState()==KTableSortComparator.SORT_DOWN) {
						type = KTableSortComparator.SORT_NONE;
					}
				}
				
				// update the comparator properly: 
				m_SortComparator.setColumnToCompare(col);
				m_SortComparator.setSortDirection(type);
				
				// perform the sorting
				model.sort(m_SortComparator);
				
				
				//always keep the same row selected when doing sort.
				for (int i = 0; i < model.getRowCount(); i++) {
					String indicatorCell = (String) model.getContentAt(0, i);
					if (indicatorCell.equals(">")) {
						//keep selection and indicator on the same row.
						m_Table.setSelection(2, i, false);
						break;
					}
				}
				
				
				// needed to make the resorting visible!
				m_Table.redraw(); 
				
				System.out.println("rowMapping-onClick="+ model.getRowMapping());
			}
		}
	}
}
