package hello.cache;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Table {
	private String name;
	private String tabschema;
	private String type;
	private String colcount;
	private String comment;
	private Set<Field> fields = new LinkedHashSet<Field>();
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTabschema() {
		return tabschema;
	}

	public void setTabschema(String tabschema) {
		this.tabschema = tabschema;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getColcount() {
		return colcount;
	}

	public void setColcount(String colcount) {
		this.colcount = colcount;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Set<Field> getFields() {
		return fields;
	}

	public void setFields(Set<Field> fields) {
		this.fields = fields;
	}

}
