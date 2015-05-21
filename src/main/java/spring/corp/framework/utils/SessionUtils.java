package spring.corp.framework.utils;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import spring.corp.framework.view.FutureRemoveSession;

public final class SessionUtils {

	private static SessionUtils instance = new SessionUtils();

	private SessionUtils() {}
	public static SessionUtils getInstance() {
		return instance;
	}
	
	public Object getValueInSession(ServletRequest request, String key) {
		HttpServletRequest httpRequest = (HttpServletRequest)request;
		HttpSession session = httpRequest.getSession();
		return session.getAttribute(key);
	}
	
	public void setValueInSession(ServletRequest request, String key, Object o) {
		HttpServletRequest httpRequest = (HttpServletRequest)request;
		HttpSession session = httpRequest.getSession();
		session.setAttribute(key, o);
	}
	
	public void setValueInSession(ServletRequest request, String key, Object o, int segundos) {
		HttpServletRequest httpRequest = (HttpServletRequest)request;
		HttpSession session = httpRequest.getSession();
		session.setAttribute(key, o);
		FutureRemoveSession futureRemoveSession = new FutureRemoveSession(session, key, segundos);
		Thread t = new Thread(futureRemoveSession);
		t.start();
	}
}