
package payne.framework.commons.sql;

public interface SQLGenerator {

	String toSQL(Adapter adapter, Class<?> mapper, String alias) throws Exception;

}