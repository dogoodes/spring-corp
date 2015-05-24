package spring.corp.framework.view;

import javax.servlet.http.HttpSession;

public class FutureRemoveSession implements Runnable {

	private final HttpSession session;
	private final String chave;
	private final long millis;
	
	public FutureRemoveSession(HttpSession session, String chave, int segundos) {
		this.session = session;
		this.chave = chave;
		this.millis = segundos * 1000;
	}
	
	@SuppressWarnings("static-access")
	public void run() {
		try {
			Thread.currentThread().sleep(this.millis);
			session.removeAttribute(this.chave);
		} catch (InterruptedException e) {
			//Ok nada a fazer
		}
	}
}