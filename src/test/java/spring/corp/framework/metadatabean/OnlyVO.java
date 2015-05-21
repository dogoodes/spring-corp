package spring.corp.framework.metadatabean;

import java.io.Serializable;

public class OnlyVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private final String ignore = "";
	private String campoString;

	public String getCampoString() {
		return campoString;
	}

	public void setCampoString(String campoString) {
		this.campoString = campoString;
	}

	public String getIgnore() {
		return ignore;
	}
}