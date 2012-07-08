package hello.filter;


public class DefaultRule implements Rule {
	@Override
	public String[] getDesignatedTableTypes() {
		// return new String[] { "TABLE", "VIEW" };
		return null;
	}

	@Override
	public String[] getDesignatedNames() {
		// return new String[]{"ORG"};
		return null;
	}

	@Override
	public String getSchemaPattern() {
//		return LogonDialog.currentUsername.toUpperCase();
		return null;
	}

	@Override
	public String getTableNamePattern() {
		return null;
	}
}
