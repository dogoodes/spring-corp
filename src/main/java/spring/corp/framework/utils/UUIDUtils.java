package spring.corp.framework.utils;

import java.util.UUID;

public class UUIDUtils {

	private UUIDUtils() {
        throw new AssertionError();
    }
	
	public static String generateUUID(){
		return UUID.randomUUID().getLeastSignificantBits() + "";
	}
}