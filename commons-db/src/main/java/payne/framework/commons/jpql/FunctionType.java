
package payne.framework.commons.jpql;

enum FunctionType {
	COUNT, AVG, MIN, MAX, SUM, ABS, MOD, SQRT, SIZE, CONCAT, LENGTH, LOCATE, SUBSTRING, TRIM, LOWER, UPPER;

	public String toString() {
		return " " + name() + " ";
	}

}