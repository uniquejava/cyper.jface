package hello.example.ktable.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;

public class QueryResult {
	private String tableName;
	
	private Row typeRow;
	private String[] types;

	private int columnCount;

	private Row headerRow;
	private String[] tableHeaders;

	private List<Row> dataRow;
	private List<String[]> data;

	private List<Row> oldDataRow;

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public void setTypeRow(Row typeRow) {
		this.typeRow = typeRow;
	}

	public void setHeaderRow(Row headerRow) {
		this.headerRow = headerRow;
	}

	public void setDataRow(List<Row> dataRow) {
		this.dataRow = dataRow;
	}

	public void setOldDataRow(List<Row> oldDataRow) {
		this.oldDataRow = oldDataRow;
	}

	public String getTableName() {
		return tableName;
	}

	public int getColumnCount() {
		return columnCount;
	}

	public void setColumnCount(int columnCount) {
		this.columnCount = columnCount;
	}

	@Deprecated
	public Row getTypeRow() {
		return typeRow;
	}

	@Deprecated
	public Row getHeaderRow() {
		return headerRow;
	}

	public List<Row> getDataRow() {
		return dataRow;
	}
	

	public List<String[]> getData() {
		if (data==null) {
			data = new ArrayList<String[]>();
			String[] value = new String[columnCount+2];
			String[] usefuleValue = new String[columnCount];
			for(Row row: dataRow){
				row.values().toArray(value);
				usefuleValue = (String[]) ArrayUtils.subarray(value, 2, value.length);
				data.add(usefuleValue);
			}
		}
		return data;
	}

	public String[] getTypes() {
		return types;
	}

	public void setTypes(String[] types) {
		this.types = types;
	}

	public String[] getTableHeaders() {
		return tableHeaders;
	}

	public void setTableHeaders(String[] tableHeaders) {
		this.tableHeaders = tableHeaders;
	}

	@Deprecated
	public List<Row> getOldDataRow() {
		if (oldDataRow == null) {
			oldDataRow = new ArrayList<Row>();
			oldDataRow.add(headerRow);
			oldDataRow.addAll(dataRow);
		}
		return oldDataRow;
	}

}
