
package payne.framework.commons.sql;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Criterion implements Transformer<JSONObject>, SQLGenerator {

	private Connector connector = Connector.AND;

	private List<Criterion> criterions = new ArrayList<Criterion>();

	private List<Rule> rules = new ArrayList<Rule>();

	public void from(JSONObject t) throws Exception {
		connector = Connector.valueOf(t.getString("op").toUpperCase());

		criterions.clear();
		JSONArray _criterions = t.containsKey("groups") ? t.getJSONArray("groups") : new JSONArray();
		for (int i = 0; i < _criterions.size(); i++) {
			Criterion criterion = new Criterion();
			criterion.from(_criterions.getJSONObject(i));
			criterions.add(criterion);
		}

		rules.clear();
		JSONArray _rules = t.containsKey("rules") ? t.getJSONArray("rules") : new JSONArray();
		for (int i = 0; i < _rules.size(); i++) {
			Rule rule = new Rule();
			rule.from(_rules.getJSONObject(i));
			rules.add(rule);
		}
	}

	public JSONObject to() throws Exception {
		JSONObject object = new JSONObject();

		object.accumulate("op", connector.name().toLowerCase());

		JSONArray _criterions = new JSONArray();
		for (Criterion criterion : criterions) {
			_criterions.add(criterion.to());
		}
		object.accumulate("groups", _criterions);

		JSONArray _rules = new JSONArray();
		for (Rule rule : rules) {
			_rules.add(rule.to());
		}
		object.accumulate("rules", _rules);

		return object;
	}

	public String toSQL(Adapter adapter, Class<?> mapper, String alias) throws Exception {
		String result = "";

		for (Criterion criterion : criterions) {
			result = connector.connect(result, "(" + criterion.toSQL(adapter, mapper, alias) + ")");
		}

		for (Rule rule : rules) {
			result = connector.connect(result, rule.toSQL(adapter, mapper, alias));
		}

		return result;
	}

	public Connector getConnector() {
		return connector;
	}

	public void setConnector(Connector connector) {
		this.connector = connector;
	}

	public List<Criterion> getCriterions() {
		return criterions;
	}

	public void setCriterions(List<Criterion> criterions) {
		this.criterions = criterions;
	}

	public List<Rule> getRules() {
		return rules;
	}

	public void setRules(List<Rule> rules) {
		this.rules = rules;
	}

}
