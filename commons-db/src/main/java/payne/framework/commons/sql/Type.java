
package payne.framework.commons.sql;

public enum Type implements Enumerable {
	STRING("字符串") {

		@Override
		public String parse(String value) {
			return "'" + value + "'";
		}
	},
	TEXT("文本") {

		@Override
		public String parse(String value) {
			return "'" + value + "'";
		}
	},
	NUMBER("数字") {

		@Override
		public String parse(String value) {
			return value;
		}
	},
	INT("整型") {

		@Override
		public String parse(String value) {
			return value;
		}
	},
	FLOAT("浮点型") {

		@Override
		public String parse(String value) {
			return value;
		}
	},
	DATE("日期") {

		@Override
		public String parse(String value) {
			return "'" + value + "'";
		}
	};

	private String key;

	private Type(String key) {
		this.key = key;
	}

	public abstract String parse(String value);

	public String getKey() {
		return key;
	}

	public String getValue() {
		return name();
	}

}