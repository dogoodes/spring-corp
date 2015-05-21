package spring.corp.framework.io;

import java.io.Serializable;

public class ProgressFileGeneration implements Serializable {

	private static final long serialVersionUID = 577962636625103658L;
	private String currentFileGeneration = "";
	private int currentPercent = 0;
	private int totalFiles = 0;
	private int processed = 0;
	private int waitProcess = 0;
	
	public String getCurrentFileGeneration() {
		return currentFileGeneration;
	}
	
	public void setCurrentFileGeneration(String currentFileGeneration) {
		if (currentFileGeneration != null ) {
			int index =  currentFileGeneration.lastIndexOf("/");
			if (index != -1) {
				this.currentFileGeneration = currentFileGeneration.substring(index+1);
			} else {
				this.currentFileGeneration = currentFileGeneration;
			}
		}
	}
	
	public int getCurrentPercent() {
		return currentPercent;
	}
	
	public void setCurrentPercent(int currentPercent) {
		this.currentPercent = currentPercent;
	}
	
	public int getTotalFiles() {
		return totalFiles;
	}
	
	public void setTotalFiles(int totalFiles) {
		this.totalFiles = totalFiles;
	}
	
	public void setProcessed(int processed){
		this.processed = processed;
	}
	
	public int getProcessed(){
		return processed;
	}
	
	public int getWaitProcess() {
		return waitProcess;
	}
	
	public void setWaitProcess(int waitProcess) {
		this.waitProcess = waitProcess;
	}
	
	public void calculaPercentual(){
		this.currentPercent = (int)Math.floor(100 * processed /totalFiles);
	}
	
	public boolean finalized(){
		return totalFiles <= processed;
	}
}