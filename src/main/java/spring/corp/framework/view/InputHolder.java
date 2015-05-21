package spring.corp.framework.view;

import java.util.List;

import spring.corp.framework.exceptions.UserException;

/**
 * Classe thread-local responsavel por guardar uma lista de UserException Uma atencao especial nesta implementacao eh que a lista a ser usada pela 
 * aplicacao deve ser inicializada pela classe cliente, senao um NullPointerException pode ocorrer no momento de utilizar a lista.
 */
public class InputHolder {
	static InheritableThreadLocal<List<UserException>> threadLocal = new InheritableThreadLocal<List<UserException>>();

	public static void set(List<UserException> list) {
		threadLocal.set(list);
	}

	public static List<UserException> get() {
		return threadLocal.get();
	}

	public static void clear() {
		get().clear();
		threadLocal.remove();
	}
}