package spring.corp.framework.exceptions;

import spring.corp.framework.log.ManagerLog;

public class WSResultException extends UserException{
	
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("rawtypes")
	public WSResultException(Class origin, Throwable cause) {
		super(cause);
		ManagerLog.error(origin, cause);
	}
	
	@SuppressWarnings("rawtypes")
	public WSResultException(Class origin, String message) {
		super(message);
		ManagerLog.error(origin, message);
	}

}
