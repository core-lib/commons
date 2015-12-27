
package payne.framework.commons.sql;

public interface Adapter {

	String adapt(Class<?> mapper, String property, String alias) throws Exception;

}
