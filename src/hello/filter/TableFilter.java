package hello.filter;

public class TableFilter {

	public final static TableFilter DEFAULT = new TableFilter("default",new DefaultRule());
	public final static TableFilter FMS = new TableFilter("fms",new FmsRule());

	private String filterName;
	private Rule rule;

	public TableFilter(String filterName, Rule rule) {
		super();
		this.filterName = filterName;
		this.rule = rule;
	}

	public String getFilterName() {
		return filterName;
	}

	public void setFilterName(String filterName) {
		this.filterName = filterName;
	}

	public Rule getRule() {
		return rule;
	}

	public void setRule(Rule rule) {
		this.rule = rule;
	}

}
