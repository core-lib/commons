package com.qfox.commons.math;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;

import com.googlecode.openbeans.PropertyDescriptor;
import com.qfox.commons.math.operator.Operator;
import com.qfox.commons.xml2bean.TransformerContext;

/**
 * 
 * 
 * 
 * @Description: 通用表达式计算工具
 * @author Change
 * @date 2013年11月24日
 * 
 *       修订记录<br/>
 *       姓名:Change<br/>
 *       时间:2013年11月24日<br/>
 *       内容:初始<br/>
 * 
 */
public class Calculator {
	// 规则一: 先算括号内 再算括号外 所以先得到最里面的括号,即括号里面不会包含括号的部分
	private static final Pattern PATTERN = Pattern.compile("\\(([^()]+)\\)");

	// 按优先值排好序的运算器
	private static final Operator[][] ORDERING;

	private Map<String, String> variables = new HashMap<String, String>();

	static {
		try {
			SAXReader reader = new SAXReader();
			Document document = reader.read(Calculator.class.getResource("/calculator.xml"));
			ORDERING = (Operator[][]) TransformerContext.transform(document);
		} catch (Exception e) {
			throw new RuntimeException("读取calculator配置文件出错!", e);
		}
	}

	private Calculator() {
		variables.put("e", String.valueOf(Math.E));
		variables.put("pi", String.valueOf(Math.PI));
	}

	/**
	 * 化简表达式得到字符串结果
	 * 
	 * @param expression
	 *            表达式
	 * @return 化简到最后的字符串
	 */
	public static String simplify(String expression, Object entity) {
		return simplify(assign(expression, entity));
	}

	/**
	 * 如果明确知道结果就是一个数值那么可以采用该方法
	 * 
	 * @param expression
	 *            表达式
	 * @return 计算结果
	 */
	public static double calculate(String expression, Object entity) {
		return calculate(assign(expression, entity));
	}

	/**
	 * 如果明确知道结果就是一个boolean值那么可以采用该方法
	 * 
	 * @param expression
	 *            表达式
	 * @return 判断结果
	 */
	public static boolean judge(String expression, Object entity) {
		return judge(assign(expression, entity));
	}

	public static String simplify(String expression) {
		Calculator calculator = new Calculator();
		// 读取变量定义
		if (expression.contains(";")) {
			String defination = expression.substring(0, expression.indexOf(";"));
			expression = expression.substring(expression.indexOf(";") + 1);
			String[] variables = defination.split(",");
			for (String variable : variables) {
				calculator.variables.put(variable.substring(0, variable.indexOf("=")), variable.substring(variable.indexOf("=") + 1));
			}
		}
		// 规范化
		expression = expression.replaceAll("[\\s\\t\\r\\n]", "");
		// 变量赋值
		for (Entry<String, String> entry : calculator.variables.entrySet()) {
			expression = expression.replaceAll("\\b" + entry.getKey() + "\\b", entry.getValue());
		}
		// 迭代运算
		String result = calculator.iterate(expression);
		// 返回结果
		return result;
	}

	/**
	 * 如果明确知道结果就是一个数值那么可以采用该方法
	 * 
	 * @param expression
	 *            表达式
	 * @return 计算结果
	 */
	public static double calculate(String expression) {
		return Double.valueOf(simplify(expression));
	}

	/**
	 * 如果明确知道结果就是一个boolean值那么可以采用该方法
	 * 
	 * @param expression
	 *            表达式
	 * @return 判断结果
	 */
	public static boolean judge(String expression) {
		return Boolean.valueOf(simplify(expression));
	}

	public static String assign(String expression, Object entity) throws IllegalArgumentException {
		Pattern pattern = Pattern.compile("\\$\\{(.+?)\\}");
		Matcher matcher = pattern.matcher(expression);
		while (matcher.find()) {
			String variable = matcher.group(1).replaceAll("\\s+", "");
			String[] splits = variable.split("\\.");
			Object value = entity;
			for (String split : splits) {
				try {
					if (value instanceof Map<?, ?>) {
						Map<?, ?> map = (Map<?, ?>) value;
						value = map.get(split);
					} else {
						PropertyDescriptor descriptor = new PropertyDescriptor(split, value.getClass());
						value = descriptor.getReadMethod().invoke(value);
					}
				} catch (Exception e) {
					throw new IllegalArgumentException("can not resolve variable of " + variable + " by entity " + entity, e);
				}
			}
			expression = expression.replace(matcher.group(), String.valueOf(value));
		}
		return expression;
	}

	/**
	 * 迭代运算表达式
	 * 
	 * @param expression
	 *            表达式
	 * @return 表达式运算结果
	 */
	private String iterate(String expression) {
		Matcher _matcher = PATTERN.matcher(expression);
		// 规则二: 顺序运算,先左后右
		while (_matcher.find()) {
			// 交给运算符去运算
			String result = iterate(_matcher.group(1));
			// 将运算结果替换掉之前的运算的表达式对应部分
			expression = expression.replace(_matcher.group(), result);
		}
		// 如果表达式还存在括号,继续迭代
		if (PATTERN.matcher(expression).find()) {
			return iterate(expression);
		}
		// 最后一次也运算一遍
		return figure(expression);
	}

	/**
	 * 按操作符的优先值计算出表达式的结果
	 * 
	 * @param expression
	 *            表达式
	 * @return 表达式的结果
	 */
	private String figure(String expression) {
		String origin = new String(expression.getBytes());
		// 规则三:优先级高的运算符优先运算, 通过之前配置的运算符 优先级关系,顺序遍历运算符,例如 先算乘除 再算加减...
		for (Operator[] operators : ORDERING) {
			Operator matched = null;
			String group = null;
			int index = 0;
			for (Operator operator : operators) {
				Matcher matcher = Pattern.compile(operator.pattern()).matcher(expression);
				if (matcher.find()) {
					if ((matched == null && group == null) || matcher.start() < index) {
						matched = operator;
						group = matcher.group();
						index = matcher.start();
					}
				}
			}
			if (matched != null && group != null) {
				String result = matched.operate(group, this);
				expression = expression.replace(group, result);
				break;
			}
		}

		// 如果不是数字 继续计算
		if (expression.matches(Operator.NUMBER) || expression.matches(Operator.BOOLEAN)) {
			return expression;
		}
		// 如果本次递归的结果和原来的输入一样 那么将会造成无限递归 直接抛出异常
		else if (expression.equals(origin)) {
			throw new RuntimeException("无法运算到最终结果,请验证表达式是否正确规范!当前结果为:[" + expression + "]");
		}
		// 如果不是继续计算
		else {
			return iterate(expression);
		}
	}

}
