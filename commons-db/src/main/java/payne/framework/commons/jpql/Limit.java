
package payne.framework.commons.jpql;

import javax.persistence.Query;

public class Limit {

	private Integer firstResult;

	private Integer maxResult;

	public Limit firstResult(int firstResult) {
		this.firstResult = Integer.valueOf(firstResult);
		return this;
	}

	public Limit maxResult(int maxResult) {
		this.maxResult = Integer.valueOf(maxResult);
		return this;
	}

	void assign(Query query) {
		query.setFirstResult(this.firstResult.intValue());
		query.setMaxResults(this.maxResult.intValue());
	}
}