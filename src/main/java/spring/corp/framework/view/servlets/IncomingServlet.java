package spring.corp.framework.view.servlets;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import spring.corp.framework.exceptions.UserException;
import spring.corp.framework.i18n.GerenciadorMensagem;
import spring.corp.framework.log.GerenciadorLog;

public class IncomingServlet extends AbstractServlet<Void> {

	private static final long serialVersionUID = 1L;

	protected static String message = "Error during Servlet processing";
	protected static final String ACAO_1 = "acao1";
	protected static final String ACAO_2 = "acao2";

	public void service(HttpServletRequest request, HttpServletResponse response) {
		try {
			System.out.println("Recendo Xml Assinado...");
			int len = request.getContentLength();
			byte[] input = new byte[len];
			ServletInputStream sin = request.getInputStream();
			int c, count = 0;
			while ((c = sin.read(input, count, input.length - count)) != -1) {
				count += c;
			}
			sin.close();
			String inString = new String(input);
			String decodedString = URLDecoder.decode(inString, "UTF-8");
			int fimHeader = decodedString.indexOf("fim")+3;
			String header = decodedString.substring(0,fimHeader);
			String xmlAssinado = decodedString.substring(fimHeader);
			String[] options = header.split("(,)");
			Map<String, String> mapOptions = new HashMap<String, String>();
			for (String option:options) {
				int posQuebra = option.indexOf(":");
				if (posQuebra > 0) {
					String chave = option.substring(0, posQuebra);
					String valor = option.substring(posQuebra+1);
					mapOptions.put(chave.trim(), valor.trim());
				}
			}
			String nidRequest = request.getParameter("nid");
			String sizeFile = mapOptions.get("size");
			String nid = mapOptions.get("nid");
			String acao = mapOptions.get("acao");
			String webClassId = mapOptions.get("webClassId");
			if (acao!= null) {
				if (webClassId == null) {
					webClassId = getWebClassId(acao);
				}
			}
			System.out.println("WebClassId:" + webClassId);
			System.out.println("Acao:" + acao);
			System.out.println("Tamanho informado no header:" + sizeFile);
			System.out.println("Tamanho do arquivo:" + xmlAssinado.length());
			System.out.println("Nid request:" + nidRequest + " <-> Nid Header" + nid);
			request.setAttribute("nid", nid);
			request.setAttribute("xml", xmlAssinado);
			try {
				preExecute(request, response);
				executeWebClassSpring(request, response, webClassId, "atualizarNotaXML");
			} catch (UserException e) {
				GerenciadorLog.error(IncomingServlet.class, e);
			} catch (Exception e) {
				String message = GerenciadorMensagem.getMessage(GerenciadorMensagem.ERRO_GERAL);
				GerenciadorLog.critical(IncomingServlet.class, e, message);
			} finally {
				posExecute(request, response);
			}
			// set the response code and write the response data
			response.setStatus(HttpServletResponse.SC_OK);
		} catch (IOException e) {
			try {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().print(e.getMessage());
				response.getWriter().close();
			} catch (IOException ioe) {
			}
		}
	}
	
	protected String getWebClassId(String acao) {
		String webClassId = null;
		if (acao.equals(ACAO_1) || acao.equals(ACAO_1)) {
			webClassId = "manterAcao";
		} else if (acao.equals(ACAO_2)) {
			webClassId = "manterAcao";
		}
		return webClassId;
	}
	
	@Override
	protected Void executeWebClassSpring(ServletRequest request, ServletResponse response, String webClassId, String invoke) {
		return null;
	}

	@Override
	protected void preExecute(ServletRequest request, ServletResponse response) {}

	@Override
	protected void posExecute(ServletRequest request, ServletResponse response) {}
}