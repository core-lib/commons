
package payne.framework.commons.jpql;

public enum Comparation {
	EQUAL(" = "), NOT_EQUAL(" != "), MORE_THAN(" > "), MORE_OR_EQUAL(" >= "), LESS_THAN(" < "), LESS_OR_EQUAL(" <= ");

	private String toString;

	private Comparation(String toString) {
		this.toString = toString;
	}

	public String toString() {
		return this.toString;
	}
}