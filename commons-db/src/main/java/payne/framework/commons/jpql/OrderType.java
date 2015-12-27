
package payne.framework.commons.jpql;

public enum OrderType {
	ASC, DESC;

	public String toString() {
		return " " + name() + " ";
	}
}