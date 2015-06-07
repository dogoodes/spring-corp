package spring.corp.framework.i18n;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.hibernate.exception.ConstraintViolationException;

import spring.corp.framework.configuracao.ManagerSetting;
import spring.corp.framework.utils.StringUtils;

public class GerenciadorMensagem {

	public static String language = ManagerSetting.getSetting("locale.language");
	//private static ResourceBundle bundleERP = ResourceBundle.getBundle("Messages", new Locale(language));
	public static final String ERRO_GERAL = "web.execution.erro.geral";
	
	private static final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
	private static final Lock r = rwl.readLock();
	private static final Lock w = rwl.writeLock();
	
	private static List<ResourceBundle> bundles = new ArrayList<ResourceBundle>();
	static {
		bundles.add(ResourceBundle.getBundle("Messages", new Locale(language)));
		bundles.add(ResourceBundle.getBundle("MessagesSystem", new Locale(language)));
		bundles.add(ResourceBundle.getBundle("MessagesFramework", new Locale(language)));
	}
	
	public static void addResourceBundle(ResourceBundle bundle) {
		w.lock();
		try {
			bundles.add(bundle);
		} finally {
			w.unlock();
		}
	}
	
	public static String getMessageIntegrityConstraint(Throwable e) {
		String messageToShow = null;
		if (e instanceof ConstraintViolationException) {
			Throwable rootCause = null;
			Throwable cause = e.getCause();
			while (cause != null && cause != rootCause) {
				rootCause = cause;
				cause = cause.getCause();
			}
			if (rootCause != null) {
				String message = rootCause.getMessage();
				int fkMessagePosition = message.indexOf("FK_");
				if (fkMessagePosition != -1) {
					int fkMessagePositionFim = message.indexOf(" ", fkMessagePosition);
					String fkMessageName = StringUtils.normalize(message.substring(fkMessagePosition, fkMessagePositionFim));	
					messageToShow = getMessage(fkMessageName.toUpperCase());
				}
			}
		}
		if (messageToShow == null) {
			messageToShow = getMessage("foreign.key.not.found");
		}
		return messageToShow;
	}
	
	public static String getMessage(String key) {
		String message = null;
		r.lock();
		try {
			for (Iterator<ResourceBundle> it = bundles.iterator(); it.hasNext() && message == null;) {
				try {
					message = it.next().getString(key);
				} catch (MissingResourceException e) {
					//Ok pode nao estar no bundle da iteracao
				}
			}
		} finally {
			r.unlock();
		}
		message = message == null?"":message;
		return message;
	}
	
	public static String getMessage(String key, Object...params) {
		String message = null;
		r.lock();
		try {
			for (Iterator<ResourceBundle> it = bundles.iterator(); it.hasNext() && message == null;) {
				try {
					message = it.next().getString(key);
				} catch (MissingResourceException e) {
					//Ok pode nao estar no bundle da iteracao
				}
			}
			if (!StringUtils.isBlank(message)) {
				try {
					MessageFormat format = new MessageFormat(message);
					message = format.format(params);
				} catch (Exception e) {
					message = "";
				}
			} else {
				message = "";
			}
		} finally {
			r.unlock();
		}
		return message;
	}
	
	public static void main(String argv[]) {
		System.out.println(GerenciadorMensagem.getMessage("view.logar.nome.nulo"));
		System.out.println(GerenciadorMensagem.getMessage("ip.machine.user", "123"));
	}
}