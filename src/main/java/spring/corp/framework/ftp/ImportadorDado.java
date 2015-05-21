package spring.corp.framework.ftp;

import java.io.Serializable;

public class ImportadorDado implements Serializable {

	private static final long serialVersionUID = 1L;
	private final String fileName;
	private final String file;
	private final String contentType;
	
	public ImportadorDado(String fileName, String file, String contentType){
		this.fileName = fileName;
		this.file = file;
		this.contentType = contentType;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public String getSimpleFileName() {
		String simpleName = null;
		int i = fileName.lastIndexOf("/");
		if (i != -1) {
			simpleName = fileName.substring(i);
		} else {
			simpleName = fileName;
		}
		return simpleName;
	}
	
	public String getFile() {
		return file;
	}

	public String getContentType() {
		return contentType;
	}
	
	public String toString(){
		return "ImportadorDado FileName [" + fileName + "] Contenttype [" + contentType + "]";
	}
}