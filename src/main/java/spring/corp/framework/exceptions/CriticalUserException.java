package spring.corp.framework.exceptions;

import spring.corp.framework.i18n.GerenciadorMensagem;
import spring.corp.framework.log.GerenciadorLog;

public class CriticalUserException extends UserException {

	private static final long serialVersionUID = -8280945186537174047L;

	public CriticalUserException(Class<?> origin, String messageSystem) {
		super(GerenciadorMensagem.getMessage(GerenciadorMensagem.ERRO_GERAL));
		GerenciadorLog.critical(origin, messageSystem);
	}

	public CriticalUserException(Class<?> origin, String messageSystem, Throwable error) {
		super(GerenciadorMensagem.getMessage(GerenciadorMensagem.ERRO_GERAL), error);
		GerenciadorLog.critical(origin, error, messageSystem);
	}
	
	public CriticalUserException(Class<?> origin, String messageSystem, String messageUser, Throwable error) {
		super(messageUser, error);
		GerenciadorLog.critical(origin, error, messageSystem);
	}
}