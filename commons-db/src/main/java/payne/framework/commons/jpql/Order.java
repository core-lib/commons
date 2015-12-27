
package payne.framework.commons.jpql;

public class Order {

	private String alias;

	private StringBuilder order;

	Order(String alias) {
		this.alias = alias;
		this.order = new StringBuilder();
	}

	private boolean isFirst() {
		return this.order.toString().equals("");
	}

	public Order by(String property, OrderType orderType) {
		if (!isFirst()) {
			this.order.append(",");
		}
		this.order.append(this.alias).append(".").append(property).append(orderType);
		return this;
	}

	public String toString() {
		StringBuilder statement = new StringBuilder();
		statement.append(this.order);
		return statement.toString();
	}
}