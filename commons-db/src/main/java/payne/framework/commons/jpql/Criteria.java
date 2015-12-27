
package payne.framework.commons.jpql;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

public class Criteria {

	private List<Criterion> criteria;

	Criteria() {
		this.criteria = new ArrayList<Criterion>();
	}

	public Criteria add(Criterion criterion) {
		this.criteria.add(criterion);
		return this;
	}

	void assign(Query query) {
		for (Criterion criterion : this.criteria) {
			criterion.assign(query);
		}
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < this.criteria.size(); i++) {
			if (i != 0) {
				sb.append(" AND ");
			}
			sb.append(this.criteria.get(i));
		}
		return sb.toString();
	}
}