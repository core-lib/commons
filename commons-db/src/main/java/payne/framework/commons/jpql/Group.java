
package payne.framework.commons.jpql;

import javax.persistence.Query;

public class Group {

	private String alias;

	private StringBuilder group;

	private Criteria having;

	Group(String alias) {
		this.alias = alias;
		this.group = new StringBuilder();
	}

	private boolean isFirst() {
		return this.group.toString().equals("");
	}

	public Group by(String property) {
		if (!isFirst()) {
			this.group.append(",");
		}
		this.group.append(this.alias).append(".").append(property);
		return this;
	}

	public Criteria having() {
		this.having = new Criteria();
		return this.having;
	}

	void assign(Query query) {
		this.having.assign(query);
	}

	public String toString() {
		StringBuilder statement = new StringBuilder();
		statement.append(this.group);
		if (this.having != null) {
			statement.append(" HAVING ").append(this.having);
		}
		return statement.toString();
	}
}