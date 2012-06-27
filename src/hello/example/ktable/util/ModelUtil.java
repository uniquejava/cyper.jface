package hello.example.ktable.util;

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
}
