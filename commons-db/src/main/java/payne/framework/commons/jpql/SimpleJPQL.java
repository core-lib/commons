
package payne.framework.commons.jpql;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public class SimpleJPQL {

	private String alias;

	private String qualifiedEntityName;

	private CriterionFactory criterionFactory;

	private String entityName;

	private OperateType operateType;

	private Function function;

	private Setter setter;

	private Criteria where;

	private Group group;

	private Order order;

	private Limit limit;

	private boolean distinct;

	public SimpleJPQL(Class<?> entityClass) {
		this(entityClass, OperateType.SELECT);
	}

	public SimpleJPQL(Class<?> entityClass, OperateType operateType) {
		this.alias = "o";
		this.qualifiedEntityName = entityClass.getName();
		this.criterionFactory = new CriterionFactory(this.alias);
		this.entityName = Utils.getEntityName(entityClass);
		this.operateType = operateType;
	}

	public Function function() {
		if (this.function == null) {
			this.function = new Function(this.alias, this.qualifiedEntityName);
		}
		return this.function;
	}

	public Setter setter() {
		if (this.setter == null) {
			this.setter = new Setter(this.alias);
		}
		return this.setter;
	}

	public Criteria where() {
		if (this.where == null) {
			this.where = new Criteria();
		}
		return this.where;
	}

	public Group group() {
		if (this.group == null) {
			this.group = new Group(this.alias);
		}
		return this.group;
	}

	public Order order() {
		if (this.order == null) {
			this.order = new Order(this.alias);
		}
		return this.order;
	}

	public Limit limit() {
		if (this.limit == null) {
			this.limit = new Limit();
		}
		return this.limit;
	}

	public SimpleJPQL distinct() {
		this.distinct = true;
		return this;
	}

	void assign(Query query) {
		if (this.where != null) {
			this.where.assign(query);
		}
		if (this.group != null) {
			this.group.assign(query);
		}
		if (this.limit != null) {
			this.limit.assign(query);
		}
		if (this.setter != null) {
			this.setter.assign(query);
		}
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> multiple(EntityManager entityManager, Class<T> resultType) {
		String jpql = toString();
		Query query = entityManager.createQuery(jpql);
		assign(query);
		return query.getResultList();
	}

	public Object singleton(EntityManager entityManager) {
		String jpql = toString();
		Query query = entityManager.createQuery(jpql);
		assign(query);
		return query.getSingleResult();
	}

	public int execute(EntityManager entityManager) {
		String jpql = toString();
		Query query = entityManager.createQuery(jpql);
		assign(query);
		return query.executeUpdate();
	}

	public CriterionFactory getCriterionFactory() {
		return this.criterionFactory;
	}

	public String toString() {
		StringBuilder statement = new StringBuilder();

		switch (operateType) {
		case SELECT:
			statement.append("SELECT ");

			if (this.distinct) {
				statement.append(" DISTINCT ");
			}

			if ((this.function != null) && (!this.function.toString().equals(""))) {
				statement.append(this.function);
			}
			else {
				statement.append(this.alias);
			}

			statement.append(" FROM ").append(this.entityName).append(" AS ").append(this.alias);
			break;
		case UPDATE:
			statement.append("UPDATE ").append(this.entityName).append(" AS ").append(this.alias);
			statement.append(" SET ").append(setter);
			break;
		case DELETE:
			statement.append("DELETE FROM ").append(this.entityName).append(" AS ").append(this.alias);
			break;
		default:
			break;
		}

		if ((this.where != null) && (!this.where.toString().equals(""))) {
			statement.append(" WHERE ").append(this.where);
		}
		if ((this.group != null) && (!this.group.toString().equals(""))) {
			statement.append(" GROUP BY ").append(this.group);
		}
		if ((this.order != null) && (!this.order.toString().equals(""))) {
			statement.append(" ORDER BY ").append(this.order);
		}

		return statement.toString();
	}
}