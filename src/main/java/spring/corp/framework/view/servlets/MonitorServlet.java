package spring.corp.framework.view.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import spring.corp.framework.exceptions.UserException;
import spring.corp.framework.i18n.GerenciadorMensagem;
import spring.corp.framework.json.Consequence;
import spring.corp.framework.json.JSONReturn;
import spring.corp.framework.log.GerenciadorLog;

public abstract class MonitorServlet extends AbstractServlet<JSONReturn>{

	private static final long serialVersionUID = 1L;

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String invoke = request.getParameter("invoke");
		response.setContentType("text/xml;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		JSONReturn jsonReturn = JSONReturn.newInstance(Consequence.ERRO);
		PrintWriter out = response.getWriter();
		try {
			preExecute(request, response);
			jsonReturn  = executeWebClassSpring(request, response, "monitorStatus", invoke);
		} catch (UserException e) {
			GerenciadorLog.error(MonitorServlet.class, e);
		} catch (Exception e) {
			String message = GerenciadorMensagem.getMessage(GerenciadorMensagem.ERRO_GERAL);
			GerenciadorLog.critical(MonitorServlet.class, e, message);	
		} finally {
			posExecute(request, response);
		}
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=UTF-8");
		out = response.getWriter();
		out.print(jsonReturn.serialize());
		out.flush();
	}
}