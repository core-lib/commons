
package payne.framework.commons.jpql;

public class Function {

	private String alias;

	private StringBuilder function;

	private String qualifiedEntityName;

	Function(String alias, String qualifiedEntityName) {
		this.alias = alias;
		this.qualifiedEntityName = qualifiedEntityName;
		this.function = new StringBuilder();
	}

	public Function property(String property) {
		this.function.append(",").append(this.alias).append(".").append(property);
		return this;
	}

	public Function count(String property) {
		return singleVariableFunction(FunctionType.COUNT, property);
	}

	public Function average(String property) {
		return singleVariableFunction(FunctionType.AVG, property);
	}

	public Function min(String property) {
		return singleVariableFunction(FunctionType.MIN, property);
	}

	public Function max(String property) {
		return singleVariableFunction(FunctionType.MAX, property);
	}

	public Function sum(String property) {
		return singleVariableFunction(FunctionType.SUM, property);
	}

	public Function abs(String property) {
		return singleVariableFunction(FunctionType.ABS, property);
	}

	public Function sqrt(String property) {
		return singleVariableFunction(FunctionType.SQRT, property);
	}

	public Function size(String property) {
		return singleVariableFunction(FunctionType.SIZE, property);
	}

	public Function length(String property) {
		return singleVariableFunction(FunctionType.LENGTH, property);
	}

	public Function trim(String property) {
		return singleVariableFunction(FunctionType.TRIM, property);
	}

	public Function lower(String property) {
		return singleVariableFunction(FunctionType.LOWER, property);
	}

	public Function upper(String property) {
		return singleVariableFunction(FunctionType.UPPER, property);
	}

	private Function singleVariableFunction(FunctionType type, String property) {
		try {
			this.function.append(",").append(type).append("(");
			if (Utils.isProperty(property, this.qualifiedEntityName)) this.function.append(this.alias).append(".").append(property);
			else {
				this.function.append("'").append(property).append("'");
			}
			this.function.append(")");
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return this;
	}

	public Function mod(String first, String second) {
		try {
			this.function.append(",").append(FunctionType.MOD).append("(");
			if (Utils.isProperty(first, this.qualifiedEntityName)) {
				this.function.append(this.alias).append(".");
			}
			this.function.append(first).append(",");
			if (Utils.isProperty(second, this.qualifiedEntityName)) {
				this.function.append(this.alias).append(".");
			}
			this.function.append(second).append(")");
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return this;
	}

	public Function concat(String first, String second) {
		try {
			this.function.append(",").append(FunctionType.CONCAT).append("(");
			if (Utils.isProperty(first, this.qualifiedEntityName)) this.function.append(this.alias).append(".").append(first);
			else {
				this.function.append("'").append(first).append("'");
			}
			this.function.append(",");
			if (Utils.isProperty(second, this.qualifiedEntityName)) this.function.append(this.alias).append(".").append(second);
			else {
				this.function.append("'").append(second).append("'");
			}
			this.function.append(")");
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return this;
	}

	public Function locate(String target, String source, int start) {
		try {
			this.function.append(",").append(FunctionType.LOCATE).append("(");

			if (Utils.isProperty(target, this.qualifiedEntityName)) this.function.append(this.alias).append(".").append(target);
			else {
				this.function.append("'").append(target).append("'");
			}

			this.function.append(",");

			if (Utils.isProperty(source, this.qualifiedEntityName)) this.function.append(this.alias).append(".").append(source);
			else {
				this.function.append("'").append(source).append("'");
			}

			this.function.append(",").append(start).append(")");
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		return this;
	}

	public Function substring(String property, int start, int length) {
		try {
			this.function.append(",").append(FunctionType.SUBSTRING).append("(");
			if (Utils.isProperty(property, this.qualifiedEntityName)) this.function.append(this.alias).append(".").append(property);
			else {
				this.function.append("'").append(property).append("'");
			}
			this.function.append(",").append(start).append(",").append(length).append(")");
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return this;
	}

	private void removeFirstComma(StringBuilder statement) {
		if (statement.indexOf(",") == 0) statement.deleteCharAt(statement.indexOf(","));
	}

	public String toString() {
		StringBuilder statement = new StringBuilder();
		statement.append(this.function);
		removeFirstComma(statement);
		return statement.toString();
	}
}