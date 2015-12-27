
package payne.framework.commons.sql;

import net.sf.json.JSONObject;

public class Rule implements Transformer<JSONObject>, SQLGenerator {

	private Operator operator;

	private String field;

	private String value;

	private Type type;

	public void from(JSONObject t) throws Exception {
		operator = Operator.valueOf(t.getString("op").toUpperCase());
		field = t.getString("field");
		value = t.getString("value");
		type = Type.valueOf(t.getString("type").toUpperCase());
	}

	public JSONObject to() throws Exception {
		JSONObject object = new JSONObject();
		object.accumulate("op", operator.name().toLowerCase());
		object.accumulate("field", field);
		object.accumulate("value", value);
		object.accumulate("type", type.name().toLowerCase());
		return object;
	}

	public String toSQL(Adapter adapter, Class<?> mapper, String alias) throws Exception {
		return operator.operate(adapter.adapt(mapper, field, alias), value, type);
	}

	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

}