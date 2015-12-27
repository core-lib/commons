
package payne.framework.commons.jpql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

public class Criterion {

	private String toString;

	private Map<String, Object> parameters;

	private List<Criterion> children;

	private SimpleJPQL subquery;

	Criterion() {
		this.parameters = new HashMap<String, Object>();
		this.children = new ArrayList<Criterion>();
	}

	Criterion(String toString) {
		this();
		this.toString = toString;
	}

	void setToString(String toString) {
		this.toString = toString;
	}

	void addParameter(String placeholder, Object parameter) {
		this.parameters.put(placeholder, parameter);
	}

	void addChild(Criterion child) {
		this.children.add(child);
	}

	void setSubquery(SimpleJPQL subquery) {
		this.subquery = subquery;
	}

	void assign(Query query) {
		if (this.children.isEmpty()) {
			for (Map.Entry<String, Object> entry : this.parameters.entrySet()) {
				query.setParameter((String) entry.getKey(), entry.getValue());
			}
		}
		else {
			for (Criterion criterion : this.children) {
				criterion.assign(query);
			}
		}
		if (this.subquery != null) {
			this.subquery.assign(query);
		}
	}

	public String toString() {
		return this.toString;
	}
}