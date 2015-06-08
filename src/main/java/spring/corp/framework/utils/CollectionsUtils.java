package spring.corp.framework.utils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CollectionsUtils {

	/**
	 * Verificar se a Collection esta vazia<br />
	 * Exemplos:
	 * <ul>
	 * 	 <li><code>isEmpty() == true</code></li>
	 *   <li><code>null == true</code></li>
	 * </ul>
	 * @param coll (Collection) collection para verificação
	 * @return (boolean) true para Collection vazia e false caso contrário
	 */
	public static boolean isBlank(Collection coll) {
		return (coll == null || coll.isEmpty());
	}

	/**
	 * Verificar se a List esta vazia<br />
	 * Exemplos:
	 * <ul>
	 * 	 <li><code>isEmpty() == true</code></li>
	 *   <li><code>null == true</code></li>
	 * </ul>
	 * @param list (List) list para verificação
	 * @return (boolean) true para List vazia e false caso contrário
	 */
	public static boolean isBlank(List list) {
		return (list == null || list.isEmpty());
	}

	/**
	 * Verificar se o Set esta vazio<br />
	 * Exemplos:
	 * <ul>
	 * 	 <li><code>isEmpty() == true</code></li>
	 *   <li><code>null == true</code></li>
	 * </ul>
	 * @param set (Set) set para verificação
	 * @return (boolean) true para Set vazio e false caso contrário
	 */
	public static boolean isBlank(Set set) {
		return (set == null || set.isEmpty());
	}

	/**
	 * Verificar se o Map esta vazio<br />
	 * Exemplos:
	 * <ul>
	 * 	 <li><code>isEmpty() == true</code></li>
	 *   <li><code>null == true</code></li>
	 * </ul>
	 * @param map (Map) map para verificação
	 * @return (boolean) true para Map vazio e false caso contrário
	 */
	public static boolean isBlank(Map map) {
		return (map == null || map.isEmpty());
	}
}