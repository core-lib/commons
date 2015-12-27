
package payne.framework.commons.sql;

public enum Operator implements Enumerable {
	EQUAL("相等于") {

		@Override
		public String operate(String name, String value, Type type) {
			return name + " = " + type.parse(value);
		}
	},
	NOTEQUAL("不等于") {

		@Override
		public String operate(String name, String value, Type type) {
			return name + " != " + type.parse(value);
		}
	},
	STARTWITH("以...开始") {

		@Override
		public String operate(String name, String value, Type type) {
			return name + " LIKE " + type.parse(value + "%");
		}
	},
	ENDWITH("以...结束") {

		@Override
		public String operate(String name, String value, Type type) {
			return name + " LIKE " + type.parse("%" + value);
		}
	},
	LIKE("以...相似") {

		@Override
		public String operate(String name, String value, Type type) {
			return name + " LIKE " + type.parse("%" + value + "%");
		}
	},
	GREATER("大于") {

		@Override
		public String operate(String name, String value, Type type) {
			return name + " > " + type.parse(value);
		}
	},
	GREATEROREQUAL("大于或等于") {

		@Override
		public String operate(String name, String value, Type type) {
			return name + " >= " + type.parse(value);
		}
	},
	LESS("小于") {

		@Override
		public String operate(String name, String value, Type type) {
			return name + " < " + type.parse(value);
		}
	},
	LESSOREQUAL("小于或等于") {

		@Override
		public String operate(String name, String value, Type type) {
			return name + " <= " + type.parse(value);
		}
	},
	IN("在...之内") {

		@Override
		public String operate(String name, String value, Type type) {
			String[] array = value.split(",");
			StringBuilder builder = new StringBuilder();
			for (String temp : array) {
				builder.append(type.parse(temp)).append(",");
			}
			value = builder.substring(0, builder.length() - (builder.toString().endsWith(",") ? 1 : 0));
			return name + " IN " + "(" + value + ")";
		}
	},
	NOTIN("不在...之内") {

		@Override
		public String operate(String name, String value, Type type) {
			String[] array = value.split(",");
			StringBuilder builder = new StringBuilder();
			for (String temp : array) {
				builder.append(type.parse(temp)).append(",");
			}
			value = builder.substring(0, builder.length() - (builder.toString().endsWith(",") ? 1 : 0));
			return name + " NOT IN " + "(" + value + ")";
		}
	},
	ISNULL("...为空") {

		@Override
		public String operate(String name, String value, Type type) {
			return name + " IS NULL";
		}
	},
	ISNOTNULL("...不为空") {

		@Override
		public String operate(String name, String value, Type type) {
			return name + " IS NOT NULL";
		}
	};

	private String key;

	private Operator(String key) {
		this.key = key;
	}

	public abstract String operate(String name, String value, Type type);

	public String getKey() {
		return key;
	}

	public String getValue() {
		return name();
	}

}