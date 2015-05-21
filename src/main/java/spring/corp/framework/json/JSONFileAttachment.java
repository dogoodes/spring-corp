package spring.corp.framework.json;

import java.io.Serializable;

public class JSONFileAttachment implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Serializable file;
	private String contentType;
	private String fileName;
	private Long fileSize;
	public static final String XML_ATTACHMENT = "text/xml;charset=UTF-8";
	public static final String PDF_ATTACHMENT = "application/pdf";
	public static final String EMAIL_ATTACHMENT = "multipart/mixed";
	public static final String TEXT_ATTACHMENT = "simple/text;charset=UTF-8";
	public static final String XSL_ATTACHMENT = "application/vnd.ms-excel"; 
	//http://onjava.com/pub/a/onjava/excerpt/jebp_3/index3.html
	public static final String BEST_PRACTICE_ATTACHMENT = "application/x-download";
	
	public Serializable getFile() {
		return file;
	}
	
	public void setFile(Serializable file) {
		this.file = file;
	}
	
	public String getContentType() {
		return contentType;
	}
	
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public Long getFileSize() {
		return fileSize;
	}
	
	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}
}