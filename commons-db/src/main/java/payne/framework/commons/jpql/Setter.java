
package payne.framework.commons.jpql;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Query;

public class Setter {

	private String alias;

	private StringBuilder sb;

	Map<String, Object> parameters;

	Setter(String alias) {
		this.alias = alias;
		this.sb = new StringBuilder();
		this.parameters = new HashMap<String, Object>();
	}

	public Setter set(String property, Object parameter) {
		if (!this.sb.toString().equals("")) {
			this.sb.append(",");
		}
		String p = Utils.getUUIDString();
		this.sb.append(this.alias).append(".").append(property).append(" = ").append(":").append(p);
		this.parameters.put(p, parameter);
		return this;
	}

	void assign(Query query) {
		for (Map.Entry<String, Object> entry : this.parameters.entrySet()) {
			query.setParameter((String) entry.getKey(), entry.getValue());
		}
	}

	public String toString() {
		return this.sb.toString();
	}
}
