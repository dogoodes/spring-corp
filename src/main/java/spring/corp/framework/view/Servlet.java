package spring.corp.framework.view;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;

import spring.corp.framework.exceptions.CriticalUserException;
import spring.corp.framework.exceptions.UserException;
import spring.corp.framework.i18n.ManagerMessage;

public abstract class Servlet<T> extends HttpServlet {

	private static final long serialVersionUID = 3299377047422968161L;
	// private Map<String, Object> webClass;
	protected ServletContext servletContext;
	
	@SuppressWarnings("unchecked")
	protected T executeWebClass(ServletRequest request, ServletResponse response, Object webClass, String invoke) {
		T executionReturn = null;
		try {
			//TODO: Poderia fazer um cache do objeto Method, cuja chave poderia ser
			//o webClass name + invoke e.g webClass.getName()+ "$" + invoke, afinal
			//eu posso o mesmo nome de metodo para mais de uma classe 
			//e.g incluir, alterar, excluir
			Method m = webClass.getClass().getMethod(invoke, ServletRequest.class, ServletResponse.class);
			executionReturn = (T) m.invoke(webClass, new Object[] {request, response});
		} catch (UserException e) {
			throw e;
		} catch (NoSuchMethodException e) {
			String message = ManagerMessage.getMessage("view.action.invalido", new Object[] {invoke, webClass.getClass().getSimpleName()});
			throw new UserException(message);
		} catch (InvocationTargetException e) {
			if (e.getTargetException() instanceof UserException) {
				throw (UserException) e.getTargetException();
			}
			String message = ManagerMessage.getMessage(ManagerMessage.ERRO_GERAL);
			throw new CriticalUserException(Servlet.class, message, e);
		} catch (IllegalAccessException e) {
			String message = ManagerMessage.getMessage(ManagerMessage.ERRO_GERAL);
			throw new CriticalUserException(Servlet.class, message, e);
		}
		return executionReturn;
	}

	// TODO: Necessario apenas quando usar webevolution.xml private T 
	// executeWebClassEvolution(ServletRequest request, ServletResponse response, String webClassId, String invoke)
	// throws UserException, IOException {
	// Object webClassObject = webClass.get(webClassId);
	// if (webClass == null) {
	// String message = GerenciadorMensagem.getMessage(
	// "view.webclassid.invalido", new Object[] { webClassId });
	// throw new UserException(message);
	// }
	// return executeWebClass(request, response, webClass, invoke);
	// }
	// @Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		this.servletContext = config.getServletContext();
		// NFeLoader loader = new NFeLoader();
		// this.webClass = loader.getWebClass();
	}

	protected void writeBinary(InputStream inStream, HttpServletResponse response) throws FileNotFoundException, IOException {
		OutputStream out = response.getOutputStream();
		InputStream in = null;
		try {
			in = new BufferedInputStream(inStream);
			byte[] buf = new byte[4 * 1024]; // 4K buffer
			int bytesRead;
			while ((bytesRead = in.read(buf)) != -1) {
				out.write(buf, 0, bytesRead);
			}
		} finally {
			if (in != null) {				
				in.close();
			}
		}
	}

	protected PrintWriter getWriter(PrintWriter out, ServletResponse response) throws IOException {
		if (out == null) {
			out = response.getWriter();
		}
		return out;
	}
}