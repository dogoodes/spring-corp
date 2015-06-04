package spring.corp.framework.utils;

import java.util.UUID;

public class UUIDUtils {

	private UUIDUtils() {
        throw new AssertionError();
    }
	
	/**
	 * Gerador de UUID randômico
	 * @return (String) string UUID randômica
	 */
	public static String generateUUID() {
		return UUID.randomUUID().getLeastSignificantBits() + "";
	}
}