package spring.corp.framework.view.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import spring.corp.framework.io.SerializableInputStream;
import spring.corp.framework.json.JSONFileAttachment;
import spring.corp.framework.json.JSONReturn;
import spring.corp.framework.view.FutureRemoveSession;

public abstract class UploadServlet extends AbstractServlet<JSONReturn>{
	
	private static final long serialVersionUID = 7112658308012232108L;

	@Override
	public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
		HttpServletRequest httpRequest = (HttpServletRequest)request;
		boolean isMultipart = ServletFileUpload.isMultipartContent(httpRequest);
		if (isMultipart) {
			uploadFile(httpRequest, (HttpServletResponse) response);
		} else {
			String webClassId = request.getParameter("webClassId");
			String invoke = request.getParameter("invoke");
			if ("gerenciadorUpload".equals(webClassId)) {
				executeWebClassSpring(httpRequest, response, webClassId, invoke);
			}
		}
	}
	
	private void uploadFile(HttpServletRequest httpRequest, HttpServletResponse response) {
		HttpSession session = httpRequest.getSession(true);
		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		try {
			List items = upload.parseRequest(httpRequest);
			Iterator iter = items.iterator();
			while (iter.hasNext()) {
				FileItem item = (FileItem) iter.next();
				if (!item.isFormField()) {
				    //String fieldName = item.getFieldName();
				    String fileName = item.getName();
				    String contentType = item.getContentType();
				    //boolean isInMemory = item.isInMemory();
				    long sizeInBytes = item.getSize();
				    InputStream uploadedStream = item.getInputStream();
				    SerializableInputStream si = new SerializableInputStream(uploadedStream);
				    JSONFileAttachment jsonFileAttachment = new JSONFileAttachment();
				    jsonFileAttachment.setContentType(contentType);
				    jsonFileAttachment.setFileName(fileName);
				    jsonFileAttachment.setFile(si);
				    jsonFileAttachment.setFileSize(sizeInBytes);
				    String fileNameKey = null;
				    if (fileName.indexOf('/') > 0) {
				    	fileNameKey = fileName.substring(fileName.lastIndexOf('/')+1);
				    } else if (fileName.indexOf('\\') > 0) {
				    	fileNameKey = fileName.substring(fileName.lastIndexOf('\\')+1);
				    } else {
				    	fileNameKey = fileName;
				    }
				    session.setAttribute(fileNameKey, jsonFileAttachment);
				    FutureRemoveSession futureRemoveSession = new FutureRemoveSession(session, fileNameKey, 120);
					Thread t = new Thread(futureRemoveSession);
					t.start();
				}
			}
		} catch (FileUploadException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();			
		}
	}
}