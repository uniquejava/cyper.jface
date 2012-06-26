package hello.example.ktable.util;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.Assert;

import de.kupzog.ktable.KTable;

public class ModelUtil {
	public static final int NO_SELECTION = 0;

	public static int getRowSelection(KTable table) {
		int[] rowSelection = table.getRowSelection();
		if (rowSelection != null && rowSelection.length > 0) {
			return rowSelection[0];
		}
		return NO_SELECTION;
	}

	public static int calcRefRowNumber(KTable table) {
		int[] rowSelection = table.getRowSelection();
		int refRowNumber = 1;
		if (rowSelection != null && rowSelection.length > 0) {
			refRowNumber = rowSelection[0];
		}
		Assert.isTrue(refRowNumber != 0);
		return refRowNumber;
	}

	public static int getRowCountWithHeaderRow(
			List<LinkedHashMap<String, Object>> list) {
		int result = 0;
		for (Map<String, Object> map : list) {
			if (!(map instanceof HiddenRow)) {
				result++;
			}
		}
		return result;
	}

	public static int getActualRefRowNumberInList(
			List<LinkedHashMap<String, Object>> list, int refRowNumber) {
		int hiddenRowCount = 0;
		int size = list.size();
		for (int i = 0; i < size; i++) {
			Map row = list.get(i);
			if (row instanceof HiddenRow) {
				if (Integer.parseInt((String) row
						.get(DecoratedRow.KEY_ROW_NUMBER)) <= refRowNumber) {
					hiddenRowCount++;
				} else {
					break;
				}
			}
		}
		System.out.println("refRowNumber=" + refRowNumber + ",hiddenRowCount="
				+ hiddenRowCount);
		return refRowNumber + hiddenRowCount;
	}
}
