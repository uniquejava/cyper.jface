package hello.filter;

public interface Rule {
	/**
	 * TABLE_TYPE String => table type. Typical types are "TABLE", "VIEW",
	 * "SYSTEM TABLE", "GLOBAL TEMPORARY", "LOCAL TEMPORARY", "ALIAS",
	 * "SYNONYM".
	 * 
	 * @return
	 */
	public String[] getDesignatedTableTypes();

	public String[] getDesignatedNames();

	public String getSchemaPattern();

	public String getTableNamePattern();

}
