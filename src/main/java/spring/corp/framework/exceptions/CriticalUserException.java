package spring.corp.framework.exceptions;

import spring.corp.framework.i18n.ManagerMessage;
import spring.corp.framework.log.ManagerLog;

public class CriticalUserException extends UserException {

	private static final long serialVersionUID = -8280945186537174047L;

	public CriticalUserException(Class<?> origin, String messageSystem) {
		super(ManagerMessage.getMessage(ManagerMessage.ERRO_GERAL));
		ManagerLog.critical(origin, messageSystem);
	}

	public CriticalUserException(Class<?> origin, String messageSystem, Throwable error) {
		super(ManagerMessage.getMessage(ManagerMessage.ERRO_GERAL), error);
		ManagerLog.critical(origin, error, messageSystem);
	}
	
	public CriticalUserException(Class<?> origin, String messageSystem, String messageUser, Throwable error) {
		super(messageUser, error);
		ManagerLog.critical(origin, error, messageSystem);
	}
}