package spring.corp.framework.metadatabean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class SimpleVO implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private final String ignore = "";
	private String campoString;
	private Long campoLong;
	private Integer campoInteger;
	private Date campoDate;
	private List<ItemListVO> itemListVO;
	
	public String getIgnore() {
		return ignore;
	}
	
	public String getCampoString() {
		return campoString;
	}
	
	public void setCampoString(String campoString) {
		this.campoString = campoString;
	}
	
	public Long getCampoLong() {
		return campoLong;
	}
	
	public void setCampoLong(Long campoLong) {
		this.campoLong = campoLong;
	}
	
	public Integer getCampoInteger() {
		return campoInteger;
	}
	
	public void setCampoInteger(Integer campoInteger) {
		this.campoInteger = campoInteger;
	}
	
	public Date getCampoDate() {
		return campoDate;
	}
	
	public void setCampoDate(Date campoDate) {
		this.campoDate = campoDate;
	}
	
	public List<ItemListVO> getItemListVO() {
		return itemListVO;
	}
	
	public void setItemListVO(List<ItemListVO> itemListVO) {
		this.itemListVO = itemListVO;
	}
}