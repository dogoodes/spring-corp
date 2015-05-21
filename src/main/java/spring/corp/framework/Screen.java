package spring.corp.framework;

import java.io.Serializable;

public class Screen implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long codigo;
	private Long cdEmpresa;
	private String webClassId;
	private String fromFriend;
	private String toFriend;
	private String jsonData;
	private String pageToRedirect;
	
	public void setCodigo(Long codigo){
		this.codigo = codigo;
	}
	
	public Long getCodigo() {
		return codigo;
	}

	public String getWebClassId() {
		return webClassId;
	}

	public void setWebClassId(String webClassId) {
		this.webClassId = webClassId;
	}

	public String getFromFriend() {
		return fromFriend;
	}

	public void setFromFriend(String fromFriend) {
		this.fromFriend = fromFriend;
	}

	public String getToFriend() {
		return toFriend;
	}

	public void setToFriend(String toFriend) {
		this.toFriend = toFriend;
	}

	public String getJsonData() {
		return jsonData;
	}

	public void setJsonData(String jsonData) {
		this.jsonData = jsonData;
	}

	public String getPageToRedirect() {
		return pageToRedirect;
	}

	public void setPageToRedirect(String pageToRedirect) {
		this.pageToRedirect = pageToRedirect;
	}

	public Long getCdEmpresa() {
		return cdEmpresa;
	}

	public void setCdEmpresa(Long cdEmpresa) {
		this.cdEmpresa = cdEmpresa;
	}
}