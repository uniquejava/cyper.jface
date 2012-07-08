package hello.cache;

public class Field {
	private String name;
	private String tbname;
	private String coltype;
	private String length;
	private String scale;
	private String colno;
	private String nulls;
	private String defaults;
	private String comment;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTbname() {
		return tbname;
	}

	public void setTbname(String tbname) {
		this.tbname = tbname;
	}

	public String getColtype() {
		return coltype;
	}

	public void setColtype(String coltype) {
		this.coltype = coltype;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public String getScale() {
		return scale;
	}

	public void setScale(String scale) {
		this.scale = scale;
	}

	public String getColno() {
		return colno;
	}

	public void setColno(String colno) {
		this.colno = colno;
	}

	public String getNulls() {
		return nulls;
	}

	public void setNulls(String nulls) {
		this.nulls = nulls;
	}

	public String getDefaults() {
		return defaults;
	}

	public void setDefaults(String defaults) {
		this.defaults = defaults;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	@Override
	public int hashCode() {
		return getName().toUpperCase().trim().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Field) {
			Field other = (Field) obj;
			return other.getName().trim()
					.equalsIgnoreCase(this.getName().trim())
					&& other.getTbname().trim()
							.equalsIgnoreCase(this.getTbname().trim());
		}
		return false;
	}

}
