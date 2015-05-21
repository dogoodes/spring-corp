package spring.corp.framework.security;

public class IdHolder {
	static InheritableThreadLocal<Long> threadLocal = new InheritableThreadLocal<Long>();

	public static void set(Long codUsuario) {
		threadLocal.set(codUsuario);
	}

	public static Long get() {
		return threadLocal.get();
	}

	public static void clear() {
		threadLocal.remove();
	}
}