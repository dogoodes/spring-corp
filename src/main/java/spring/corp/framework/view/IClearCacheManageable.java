package spring.corp.framework.view;

import javax.servlet.http.HttpSession;

public interface IClearCacheManageable {

	public void clearCache(HttpSession session);
}
