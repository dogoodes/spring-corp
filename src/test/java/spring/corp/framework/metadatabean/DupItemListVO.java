package spring.corp.framework.metadatabean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DupItemListVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private final String ignore = "";
	private String umaString;
	private OnlyVO onlyVO;
	private List<ItemListVO> listItemListVO = new ArrayList<ItemListVO>();
	
	public DupItemListVO() {
		this.umaString = "nada";
		this.listItemListVO.add(new ItemListVO("Dup Item 1", Integer.valueOf(1)));
		this.listItemListVO.add(new ItemListVO("Dup Item 2", Integer.valueOf(2)));
		this.listItemListVO.add(new ItemListVO("Dup Item 3", Integer.valueOf(3)));
		
		OnlyVO vo = new OnlyVO();
		vo.setCampoString("Testando");
		this.onlyVO = vo;
	}
	
	public String getIgnore() {
		return ignore;
	}
	
	public String getUmaString() {
		return umaString;
	}
	
	public void setUmaString(String umaString) {
		this.umaString = umaString;
	}
	
	public List<ItemListVO> getListItemListVO() {
		return listItemListVO;
	}
	
	public void setListItemListVO(List<ItemListVO> listItemListVO) {
		this.listItemListVO = listItemListVO;
	}
	
	public void addItemListVO(ItemListVO itemListVO){
		this.listItemListVO.add(itemListVO);
	}
	
	public OnlyVO getOnlyVO() {
		return onlyVO;
	}
	
	public void setOnlyVO(OnlyVO onlyVO) {
		this.onlyVO = onlyVO;
	}
}