package spring.corp.framework.view.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import spring.corp.framework.Screen;
import spring.corp.framework.exceptions.UserException;
import spring.corp.framework.exceptions.UserLinkException;
import spring.corp.framework.i18n.GerenciadorMensagem;
import spring.corp.framework.json.Consequence;
import spring.corp.framework.json.JSONReturn;
import spring.corp.framework.log.ManagerLog;
import spring.corp.framework.utils.ScreenCollabUtil;
import spring.corp.framework.view.InputHolder;

public class Servlet extends AbstractServlet<JSONReturn> {

	private static final long serialVersionUID = 1L;

	@Override
	public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String webClassId = request.getParameter("webClassId");
		String invoke = request.getParameter("invoke");
		String sendScreen = request.getParameter("sendScreen");
		if (ScreenCollabUtil.isSendScreen(sendScreen)) {
			invoke = "sendScreen";
		}
		//Nao inicializar o objeto pois internamente o relatorio podera ser chamado e com isso o response ser usado.
		PrintWriter out = null; 
		if (webClassId == null) {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json;charset=UTF-8");
			JSONReturn jsonReturn = JSONReturn.newInstance(Consequence.ERRO);
			out = response.getWriter();
			out.print(jsonReturn.serialize());
			out.flush();
		} else {
			try {
				preExecute(request, response);
				JSONReturn jsonReturn = executeWebClassSpring(request, response, webClassId, invoke);
				if (!jsonReturn.getConsequence().equals(Consequence.RELATORIO)) {
					response.setCharacterEncoding("UTF-8");
					response.setContentType("application/json;charset=UTF-8");
					out = getWriter(out, response);
					if (ScreenCollabUtil.isRecoverScreen(invoke) && jsonReturn.getConsequence().equals(Consequence.SUCESSO)) {
						out.print(((Screen)jsonReturn.getDado()).getJsonData());
					} else {
						out.print(jsonReturn.serialize());
					}
				}
			} catch (UserException e) {
				response.setCharacterEncoding("UTF-8");
				response.setContentType("application/json;charset=UTF-8");
				JSONReturn jsonReturn;
				out = getWriter(out, response);
				if (e instanceof UserLinkException) {
					jsonReturn = JSONReturn.newInstance(Consequence.MUITOS_ERROS, InputHolder.get());
				} else {
					jsonReturn = JSONReturn.newInstance(Consequence.ERRO).message(e.getMessage());
				}	
				out.print(jsonReturn.serialize());	
			} catch (Exception e) {
				response.setCharacterEncoding("UTF-8");
				response.setContentType("application/json;charset=UTF-8");
				String message = GerenciadorMensagem.getMessage(GerenciadorMensagem.ERRO_GERAL);
				ManagerLog.critical(Servlet.class, e, message);
				out = getWriter(out, response);
				out.print(JSONReturn.newInstance(Consequence.ERRO).message(message).serialize());
			} finally {
				posExecute(request, response);
			}
		}
		if (out != null) {
			out.flush();
		}
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		setLocale();
		super.init(config);
	}
	
	protected void setLocale() {
		String s = System.getProperty("java.util.Locale");
		if (s != null) {
			String[] parts = s.split("_");
			Locale locale = null;
			if (parts.length==1) {
				locale = new Locale(parts[0]);  
			} else if (parts.length==2) {
				locale = new Locale(parts[0], parts[1]);  
			} else if (parts.length==3) {				
				locale = new Locale(parts[0], parts[1], parts[2]);  
			}
		   
			if (locale != null) {
				Locale.setDefault(locale);  
			}
		}
	}

	@Override
	protected JSONReturn executeWebClassSpring(ServletRequest request, ServletResponse response, String webClassId, String invoke) {
		return null;
	}

	@Override
	protected void preExecute(ServletRequest request, ServletResponse response) {}

	@Override
	protected void posExecute(ServletRequest request, ServletResponse response) {}
}