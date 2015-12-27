
package payne.framework.commons.jpql;

import java.util.Arrays;
import java.util.List;

public class CriterionFactory {

	private String alias;

	CriterionFactory(String alias) {
		this.alias = alias;
	}

	public Criterion equal(String property, Object parameter) {
		String p = Utils.getUUIDString();
		Criterion criterion = new Criterion(this.alias + "." + property + " = " + ":" + p);
		criterion.addParameter(p, parameter);
		return criterion;
	}

	public Criterion notEqual(String property, Object parameter) {
		String p = Utils.getUUIDString();
		Criterion criterion = new Criterion(this.alias + "." + property + " != " + ":" + p);
		criterion.addParameter(p, parameter);
		return criterion;
	}

	public Criterion moreThan(String property, Object parameter) {
		String p = Utils.getUUIDString();
		Criterion criterion = new Criterion(this.alias + "." + property + " > " + ":" + p);
		criterion.addParameter(p, parameter);
		return criterion;
	}

	public Criterion lessThan(String property, Object parameter) {
		String p = Utils.getUUIDString();
		Criterion criterion = new Criterion(this.alias + "." + property + " < " + ":" + p);
		criterion.addParameter(p, parameter);
		return criterion;
	}

	public Criterion moreOrEqual(String property, Object parameter) {
		String p = Utils.getUUIDString();
		Criterion criterion = new Criterion(this.alias + "." + property + " >= " + ":" + p);
		criterion.addParameter(p, parameter);
		return criterion;
	}

	public Criterion lessOrEqual(String property, Object parameter) {
		String p = Utils.getUUIDString();
		Criterion criterion = new Criterion(this.alias + "." + property + " <= " + ":" + p);
		criterion.addParameter(p, parameter);
		return criterion;
	}

	public Criterion like(String property, Object parameter) {
		String p = Utils.getUUIDString();
		Criterion criterion = new Criterion(this.alias + "." + property + " LIKE " + ":" + p);
		criterion.addParameter(p, parameter);
		return criterion;
	}

	public <T> Criterion in(String property, T[] parameters) {
		return in(property, Arrays.asList(parameters));
	}

	public <T> Criterion in(String property, List<T> parameters) {
		Criterion criterion = new Criterion();
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < parameters.size(); i++) {
			String p = Utils.getUUIDString();
			if (i != 0) {
				sb.append(",");
			}
			sb.append(":" + p);
			criterion.addParameter(p, parameters.get(i));
		}
		criterion.setToString(this.alias + "." + property + " IN " + "(" + sb.toString() + ")");

		return criterion;
	}

	public <T> Criterion notIn(String property, T[] parameters) {
		return notIn(property, Arrays.asList(parameters));
	}

	public <T> Criterion notIn(String property, List<T> parameters) {
		Criterion criterion = new Criterion();
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < parameters.size(); i++) {
			String p = Utils.getUUIDString();
			if (i != 0) {
				sb.append(",");
			}
			sb.append(":" + p);
			criterion.addParameter(p, parameters.get(i));
		}
		criterion.setToString(this.alias + "." + property + " NOT IN " + "(" + sb.toString() + ")");

		return criterion;
	}

	public Criterion isNull(String property) {
		return new Criterion(this.alias + "." + property + " IS NULL ");
	}

	public Criterion notNull(String property) {
		return new Criterion(this.alias + "." + property + " IS NOT NULL ");
	}

	public Criterion empty(String property) {
		return new Criterion(this.alias + "." + property + " IS EMPTY ");
	}

	public Criterion notEmpty(String property) {
		return new Criterion(this.alias + "." + property + " IS NOT EMPTY ");
	}

	public Criterion amongst(String property, Object min, Object max) {
		String minP = Utils.getUUIDString();
		String maxP = Utils.getUUIDString();
		Criterion criterion = new Criterion(this.alias + "." + property + " BETWEEN " + ":" + minP + " AND " + ":" + maxP);
		criterion.addParameter(minP, min);
		criterion.addParameter(maxP, max);
		return criterion;
	}

	public Criterion notAmongst(String property, Object min, Object max) {
		String minP = Utils.getUUIDString();
		String maxP = Utils.getUUIDString();
		Criterion criterion = new Criterion(this.alias + "." + property + " NOT BETWEEN " + ":" + minP + " AND " + ":" + maxP);
		criterion.addParameter(minP, min);
		criterion.addParameter(maxP, max);
		return criterion;
	}

	public Criterion in(String property, SimpleJPQL subquery) {
		Criterion criterion = new Criterion(this.alias + "." + property + " IN " + "(" + subquery + ")");
		criterion.setSubquery(subquery);
		return criterion;
	}

	public Criterion any(String property, Comparation comparation, SimpleJPQL subquery) {
		Criterion criterion = new Criterion(this.alias + "." + property + comparation + " ANY " + "(" + subquery + ")");
		criterion.setSubquery(subquery);
		return criterion;
	}

	public Criterion all(String property, Comparation comparation, SimpleJPQL subquery) {
		Criterion criterion = new Criterion(this.alias + "." + property + comparation + " ALL " + "(" + subquery + ")");
		criterion.setSubquery(subquery);
		return criterion;
	}

	public Criterion and(Criterion... children) {
		Criterion criterion = new Criterion();
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		for (int i = 0; i < children.length; i++) {
			if (i > 0) {
				sb.append(" AND ");
			}
			sb.append(children[i]);
			criterion.addChild(children[i]);
		}
		sb.append(")");
		criterion.setToString(sb.toString());
		return criterion;
	}

	public Criterion or(Criterion... children) {
		Criterion criterion = new Criterion();
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		for (int i = 0; i < children.length; i++) {
			if (i > 0) {
				sb.append(" OR ");
			}
			sb.append(children[i]);
			criterion.addChild(children[i]);
		}
		sb.append(")");
		criterion.setToString(sb.toString());
		return criterion;
	}
}