
package payne.framework.commons.jpql;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.persistence.Entity;

public abstract class Utils {

	public static String getUUIDString() {
		String name = UUID.randomUUID().toString();
		name = "name_" + name.replace("-", "_");
		return name;
	}

	public static String getEntityName(Class<?> entityClass) {
		String entityName = entityClass.getSimpleName();

		Entity entity = (Entity) entityClass.getAnnotation(Entity.class);

		if (entity == null) {
			return entityName;
		}

		String name = entity.name();
		if ((name != null) && (!name.equals(""))) {
			entityName = name;
		}

		return entityName;
	}

	public static boolean isProperty(String property, String className) throws Exception {
		List<String> properties = new ArrayList<String>();
		properties.addAll(Arrays.asList(property.split("\\.")));
		return isProperty(properties, Class.forName(className));
	}

	private static <T> boolean isProperty(List<String> properties, Class<T> clazz) throws Exception {
		if (properties.isEmpty()) {
			return true;
		}

		BeanInfo info = Introspector.getBeanInfo(clazz);
		PropertyDescriptor[] descriptors = info.getPropertyDescriptors();
		String property = (String) properties.remove(0);
		for (int i = 0; i < descriptors.length; i++) {
			if (descriptors[i].getName().equals(property)) {
				return isProperty(properties, descriptors[i].getPropertyType());
			}
		}

		return false;
	}
}