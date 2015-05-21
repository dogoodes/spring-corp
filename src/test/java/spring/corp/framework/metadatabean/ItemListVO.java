package spring.corp.framework.metadatabean;

import java.io.Serializable;

public class ItemListVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private final String ignore = "";
	private String itemString;
	private Integer itemInteger;
	
	public ItemListVO(){}
	
	public ItemListVO(String itemString, Integer itemInteger){
		this.itemString = itemString;
		this.itemInteger = itemInteger;
	}
	
	public String getIgnore() {
		return ignore;
	}
	
	public String getItemString() {
		return itemString;
	}
	
	public void setItemString(String itemString) {
		this.itemString = itemString;
	}
	
	public Integer getItemInteger() {
		return itemInteger;
	}
	
	public void setItemInteger(Integer itemInteger) {
		this.itemInteger = itemInteger;
	}
}