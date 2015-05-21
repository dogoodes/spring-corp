package spring.corp.framework.view;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import spring.corp.framework.io.SerializableInputStream;
import spring.corp.framework.json.JSONFileAttachment;

public class GerenciadorUpload {

	//public static final int MAX_FILE_SIZE = 1024 * 2042; //* 20 = 20K //*1024 = 1M //*2024 = 2M
	
	private static GerenciadorUpload instance = new GerenciadorUpload();
	
	public static GerenciadorUpload getInstance(){
		return instance;
	}
	
	private GerenciadorUpload(){}
	
	public void recuperarArquivo(ServletRequest request, ServletResponse response) {
		String fileName = request.getParameter("fileName");
		JSONFileAttachment jsonFileAttachment = recuperarArquivo(request, response, fileName);
		response.setContentType(jsonFileAttachment.getContentType());
		try {
			SerializableInputStream serializableInputStream = (SerializableInputStream)jsonFileAttachment.getFile();
			OutputStream out = response.getOutputStream();
			InputStream in = null;
			try {
				in = new BufferedInputStream((InputStream)serializableInputStream);
				byte[] buf = new byte[jsonFileAttachment.getFileSize().intValue()]; 
				int bytesRead;
				while ((bytesRead = in.read(buf)) != -1) {
					out.write(buf, 0, bytesRead);
				}
				serializableInputStream.reset(buf);
				HttpSession session = ((HttpServletRequest)request).getSession(true);
				session.setAttribute(jsonFileAttachment.getFileName(), jsonFileAttachment);
			} finally {
				if (in != null) {
					in.close();
				}
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public void recuperarImagem(ServletRequest request, ServletResponse response) {
		recuperarArquivo(request, response);
	}
	
	public static JSONFileAttachment recuperarArquivo(ServletRequest request, ServletResponse response, String fileName) {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpSession session = httpRequest.getSession(true);
		return (JSONFileAttachment) session.getAttribute(fileName);
	}
}