package spring.corp.framework.metadatabean;

import java.io.Serializable;
import java.util.List;

public class DupSimpleVO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private final String ignore = "";
	private String umaString;
	private List<DupItemListVO> dupItemListVO;

	public DupSimpleVO(){}
	
	public String getUmaString() {
		return umaString;
	}
	
	public void setUmaString(String umaString) {
		this.umaString = umaString;
	}
	
	public List<DupItemListVO> getDupItemListVO() {
		return dupItemListVO;
	}
	
	public void setDupItemListVO(List<DupItemListVO> dupItemListVO) {
		this.dupItemListVO = dupItemListVO;
	}
	
	public String getIgnore() {
		return ignore;
	}
}