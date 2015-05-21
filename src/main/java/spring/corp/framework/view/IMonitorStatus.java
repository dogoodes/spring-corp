package spring.corp.framework.view;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import spring.corp.framework.json.JSONReturn;

public interface IMonitorStatus {

	public JSONReturn generateTicket(ServletRequest request, ServletResponse response);
	public JSONReturn cleanTicket(ServletRequest request, ServletResponse response);
	public void updateStatus(ServletRequest request,String ticket, JSONReturn jsonReturn);
	public JSONReturn checkStatus(ServletRequest request,ServletResponse response);
	
		
}
