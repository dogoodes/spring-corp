package spring.corp.framework.security;

public class NameHolder {
	static InheritableThreadLocal<String> threadLocal = new InheritableThreadLocal<String>();

	public static void set(String nomeUsuario) {
		threadLocal.set(nomeUsuario);
	}

	public static String get() {
		return threadLocal.get();
	}

	public static void clear() {
		threadLocal.remove();
	}
}