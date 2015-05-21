package spring.corp.framework.utils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CollectionUtils {

	public static boolean isBlank(Collection coll) {
		return (coll == null || coll.isEmpty());
	}
	
	public static boolean isBlank(List list) {
		return (list == null || list.isEmpty());
	}
	
	public static boolean isBlank(Set set) {
		return (set == null || set.isEmpty());
	}
	
	public static boolean isBlank(Map map) {
		return (map == null || map.isEmpty());
	}
}