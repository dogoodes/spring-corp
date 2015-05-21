package spring.corp.framework.zip;

import java.io.Serializable;

public class ZipContent implements Serializable{

	private static final long serialVersionUID = 1L;
	private String name;
	private byte[] content;
	
	public ZipContent(String name, byte[] content){
		this.name = name;
		this.content = content;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public byte[] getContent() {
		return content;
	}
	
	public void setContent(byte[] content) {
		this.content = content;
	}
}