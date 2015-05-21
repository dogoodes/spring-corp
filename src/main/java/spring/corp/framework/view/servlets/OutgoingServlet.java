package spring.corp.framework.view.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import spring.corp.framework.exceptions.UserException;
import spring.corp.framework.i18n.GerenciadorMensagem;
import spring.corp.framework.log.GerenciadorLog;

public class OutgoingServlet extends AbstractServlet<String> {

	private static final long serialVersionUID = 1L;

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String webClassId = request.getParameter("webClassId");
		String invoke = request.getParameter("invoke");
		response.setContentType("text/xml;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		String xml = "";
		PrintWriter out = response.getWriter();
		if (webClassId == null) {
			String message = GerenciadorMensagem.getMessage("webservice.outgoingservlet.assinaturaxml.webClassId.nao.informado");
			GerenciadorLog.critical(OutgoingServlet.class, message);
		} else {
			try {
				preExecute(request, response);
				xml  = executeWebClassSpring(request, response, webClassId, invoke);
			} catch (UserException e) {
				GerenciadorLog.error(OutgoingServlet.class, e);
			} catch (Exception e) {
				String message = GerenciadorMensagem.getMessage(GerenciadorMensagem.ERRO_GERAL);
				GerenciadorLog.critical(Servlet.class, e, message);
			} finally {
				posExecute(request, response);
			}
		}
		response.setContentLength(xml.length());
		out.print(xml);
		out.flush();
	}

	@Override
	protected String executeWebClassSpring(ServletRequest request, ServletResponse response, String webClassId, String invoke) {
		return null;
	}

	@Override
	protected void preExecute(ServletRequest request, ServletResponse response) {}

	@Override
	protected void posExecute(ServletRequest request, ServletResponse response) {}
}