
package payne.framework.commons.sql;

public interface Transformer<T> {

	void from(T t) throws Exception;

	T to() throws Exception;

}
