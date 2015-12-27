
package payne.framework.commons.sql;

public enum Connector implements Enumerable {
	AND("并且") {

		@Override
		public String connect(String left, String right) throws Exception {
			StringBuilder builder = new StringBuilder();
			if (left != null && !left.trim().equals("")) {
				builder.append(left);
			}
			if (left != null && !left.trim().equals("") && right != null && !right.trim().equals("")) {
				builder.append(" AND ");
			}
			if (right != null && !right.trim().equals("")) {
				builder.append(right);
			}
			return builder.toString();
		}
	},
	OR("或者") {

		@Override
		public String connect(String left, String right) throws Exception {
			StringBuilder builder = new StringBuilder();
			if (left != null && !left.trim().equals("")) {
				builder.append(left);
			}
			if (left != null && !left.trim().equals("") && right != null && !right.trim().equals("")) {
				builder.append(" OR ");
			}
			if (right != null && !right.trim().equals("")) {
				builder.append(right);
			}
			return builder.toString();
		}
	};

	private String key;

	private Connector(String key) {
		this.key = key;
	}

	public abstract String connect(String left, String right) throws Exception;

	public String getKey() {
		return key;
	}

	public String getValue() {
		return name();
	}

}