package spring.corp.framework.view;

import javax.servlet.ServletContext;

/**
 * Classe thread-local responsavel por guardar uma lista de UserException Uma atencao especial nesta implementacao eh que a lista a ser usada pela 
 * aplicacao deve ser inicializada pela classe cliente, senao um NullPointerException pode ocorrer no momento de utilizar a lista.
 */
public class ServletContextHolder {
	static InheritableThreadLocal<ServletContext> threadLocal = new InheritableThreadLocal<ServletContext>();

	public static void set(ServletContext servletContext) {
		threadLocal.set(servletContext);
	}

	public static ServletContext get() {
		return threadLocal.get();
	}

	public static void clear() {
		threadLocal.remove();
	}
}