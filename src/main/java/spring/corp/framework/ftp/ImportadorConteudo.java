package spring.corp.framework.ftp;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ImportadorConteudo implements Serializable {

	private static final long serialVersionUID = 1932599965672925522L;
	private final Long cdEmpresa;
	private final ImportadorDado importadorDado;
	private final String importadorClassId;
	private String resultProcessing = null;
	private Map<String,Object> holder = new HashMap<String,Object>();
	
	public ImportadorConteudo(Long cdEmpresa, ImportadorDado importContent, String importadorClassId) {
		this.cdEmpresa = cdEmpresa;
		this.importadorDado = importContent;
		this.importadorClassId = importadorClassId;
	}
	
	public Long getCdEmpresa() {
		return cdEmpresa;
	}
	public ImportadorDado getImportadorDado() {
		return importadorDado;
	}

	public String getImportadorClassId() {
		return importadorClassId;
	}
	
	public String getResultProcessing() {
		return resultProcessing;
	}

	public void setResultProcessing(String resultProcessing) {
		this.resultProcessing = resultProcessing;
	}
	
	public void addHolder(String key, Object o){
		holder.put(key, o);
	}
	
	public Map<String,Object> getHolder(){
		return holder;
	}
	
	public String toString() {
		String info =  "ImportadorConteudo CD_EMPRESA [" + cdEmpresa + "] ImportadorClassId [" + importadorClassId + "] ";
		if (importadorDado != null) {
			info += importadorDado.toString();
		}
		return info;
	}
}