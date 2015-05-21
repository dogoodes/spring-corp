package spring.corp.framework.ftp;

public class FileTransferTicketsStatus implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private String ticket;
	private Integer status;
	private int qtFiles = 0;
	private int qtFilesProcessed = 0;
	private int retryFileReceived = 0;
	private int qtFilesNotProcessed = 0;
	private String fileName;
	private int fileSize;
	private String webClassId;
	
	public String getTicket() {
		return ticket;
	}
	
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	
	public Integer getStatus() {
		return status;
	}
	
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public int getQtFiles() {
		return qtFiles;
	}
	
	public void setQtFiles(int qtFiles) {
		this.qtFiles = qtFiles;
	}
	
	public int getQtFilesProcessed() {
		return qtFilesProcessed;
	}
	
	public void addQtFilesProcessed() {
		this.qtFilesProcessed++;
	}
	
	public void addQtFilesNotProcessed() {
		this.qtFilesNotProcessed++;
	}
	
	public int getQtFilesNotProcessed() {
		return qtFilesNotProcessed;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public int getFileSize() {
		return fileSize;
	}
	
	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}
	
	public int getRetryFileReceived() {
		return retryFileReceived;
	}
	
	public void addRetryFileReceived() {
		this.retryFileReceived++;
	}
	
	public void resetRetryFileReceived() {
		this.retryFileReceived = 0;
	}
	
	public String getWebClassId() {
		return webClassId;
	}
	
	public void setWebClassId(String webClassId) {
		this.webClassId = webClassId;
	}
}