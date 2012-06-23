package hello.layout.aqua.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;

public class GridDataFactory {
	public static GridData gd4text() {
		GridData gd = new GridData();
		gd.horizontalAlignment = SWT.FILL;
		gd.verticalAlignment = SWT.FILL;
		gd.grabExcessHorizontalSpace = true;
		return gd;
	}

	public static GridData rowspan(int rowspan) {
		GridData gd = new GridData();
		gd.verticalSpan = rowspan;
		return gd;
	}
	public static GridData colspan(int colspan) {
		GridData gd = new GridData();
		gd.horizontalSpan = colspan;
		return gd;
	}
}
