package spring.corp.framework.exceptions;

import spring.corp.framework.log.ManagerLog;

public class WSSendException extends UserException {
	
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("rawtypes")
	public WSSendException(Class origin, Throwable cause) {
		super(cause);
		ManagerLog.error(origin, cause);
	}
	
	@SuppressWarnings("rawtypes")
	public WSSendException(Class origin, String message) {
		super(message);
		ManagerLog.error(origin, message);
	}
}