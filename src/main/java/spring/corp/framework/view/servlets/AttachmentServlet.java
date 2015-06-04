package spring.corp.framework.view.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import spring.corp.framework.exceptions.UserException;
import spring.corp.framework.exceptions.UserLinkException;
import spring.corp.framework.i18n.GerenciadorMensagem;
import spring.corp.framework.json.Consequence;
import spring.corp.framework.json.JSONConstant;
import spring.corp.framework.json.JSONFileAttachment;
import spring.corp.framework.json.JSONReturn;
import spring.corp.framework.log.ManagerLog;
import spring.corp.framework.view.FutureRemoveSession;
import spring.corp.framework.view.InputHolder;

/**
 * A classe WaveAttchamentServlet trabalha em dua vias, onde a primeira eh gerada o arquivo e seu nome eh retornado e a segunda onde o arquivo eh 
 * solicitado. Isso eh por que a primeira via deve ser chamada via AJAX e pode ser que a operacao gere alguma excecao logo a consequencia sera de 
 * ERRO e a chamada AJAX podera tratar esse erro e caso seja de SUCESSO o arquivo sera mantido na sessao do usuario e sera retornado apenas o nome 
 * do arquivo. O Cliente devera entao fazer uma segunda chamada solicitando o download do arquivo passando como valor para o invoke o valor 
 * "downloadFile" e  fileName com valor igual ao nome retornado na primeira via, essa chamada deve ser um submit do formulario e nao uma chamada
 * AJAX pois o browser tem que entender que um arquivo atachado esta no response do usuario para poder abrir a opcao de salvar o arquivo.
 */
public abstract class AttachmentServlet extends AbstractServlet<JSONReturn> {

	private static final long serialVersionUID = 1L;
	
	@Override
	public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
		String webClassId = request.getParameter("webClassId");
		String invoke = request.getParameter("invoke");
		if (invoke != null && invoke.equals("downloadFile")) {
			String fileName = request.getParameter("fileName");
			downloadFile(request, response, fileName);
		} else if (invoke != null && invoke.equals("downloadZipFile")) {
			executeWebClassSpring(request, response, webClassId, invoke);
		} else {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json;charset=UTF-8");
			PrintWriter out = null; 
			if (webClassId == null) {
				JSONReturn jsonReturn = JSONReturn.newInstance(Consequence.ERRO);
				out = response.getWriter();
				out.print(jsonReturn.serialize());
				out.flush();
			} else {
				try {
					preExecute(request, response);
					JSONReturn jsonReturn = executeWebClassSpring(request, response, webClassId, invoke);
					if (jsonReturn.getConsequence().equals(Consequence.SUCESSO)) {
						JSONFileAttachment attachment = (JSONFileAttachment)jsonReturn.getDado();
						holdFile(request, attachment);
						JSONConstant jsonConstant = new JSONConstant();
						jsonConstant.setValor(attachment.getFileName());
						jsonReturn = JSONReturn.newInstance(Consequence.SUCESSO, jsonConstant);
					}	
					out = getWriter(out, response);
					out.print(jsonReturn.serialize());
				} catch (UserException e) {
					JSONReturn jsonReturn;
					out = getWriter(out, response);
					if (e instanceof UserLinkException) {
						jsonReturn = JSONReturn.newInstance(Consequence.MUITOS_ERROS, InputHolder.get());
					} else {
						jsonReturn = JSONReturn.newInstance(Consequence.ERRO).message(e.getMessage());
					}	
					out.print(jsonReturn.serialize());	
				} catch (Exception e) {
					String message = GerenciadorMensagem.getMessage(GerenciadorMensagem.ERRO_GERAL);
					ManagerLog.critical(AttachmentServlet.class, e, message);
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
	}

	private void holdFile(ServletRequest request, JSONFileAttachment attachment) {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpSession session = httpRequest.getSession(true);
		session.setAttribute(attachment.getFileName(), attachment);
		//Guarda o arquivo na sessao por 10 segundos, depois disso o mesmo eh reciclado
		FutureRemoveSession futureRemoveSession = new FutureRemoveSession(session, attachment.getFileName(), 10);
		Thread t = new Thread(futureRemoveSession);
		t.start();
	}
	
	private void downloadFile(ServletRequest request, ServletResponse response, String fileName) throws IOException{
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpSession session = httpRequest.getSession(true);
		JSONFileAttachment attachment = (JSONFileAttachment) session.getAttribute(fileName);
		if (attachment != null) {
			response.setContentType(JSONFileAttachment.BEST_PRACTICE_ATTACHMENT);
			HttpServletResponse httpResponse = (HttpServletResponse)response;
			httpResponse.setHeader("Content-Disposition", "attachment;filename=" + attachment.getFileName());
			if (attachment.getContentType().equals(JSONFileAttachment.PDF_ATTACHMENT)) {
				writeBinary((InputStream)attachment.getFile(), (HttpServletResponse)response);
			} else if (attachment.getContentType().equals(JSONFileAttachment.XML_ATTACHMENT) || attachment.getContentType().equals(JSONFileAttachment.TEXT_ATTACHMENT)) {
				PrintWriter out = null;
				out = getWriter(out, httpResponse);
				out.print(attachment.getFile());
				out.flush();
			}
			session.removeAttribute(fileName);
		}
	}
}