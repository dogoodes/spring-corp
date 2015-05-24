package spring.corp.framework.metadatabean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

import spring.corp.framework.metadatabean.DelimiterMetaDataManager;

public class MetaDataBeanTest extends TestCase {

	public static void main(String[] args) {
		SimpleMetaDataBean smdbDem = SimpleMetaDataBean.newInstance(new SimpleVO(), '|');
		DelimiterMetaDataManager managerDem = new DelimiterMetaDataManager(smdbDem, '|');
		managerDem.materialize("TESTE DO METADATABEAN|178|34|20130508|");
		System.out.println(smdbDem.getSimpleVO());
	}
	
	public static void main1(String args[]) {
		DupSimpleVO dupSimpleVO = new DupSimpleVO();
		dupSimpleVO.setUmaString("nada");
		
		List<DupItemListVO> dupListItemListVO = new ArrayList<DupItemListVO>();
		dupListItemListVO.add(new DupItemListVO());
		dupListItemListVO.add(new DupItemListVO());
		dupListItemListVO.add(new DupItemListVO());
		dupSimpleVO.setDupItemListVO(dupListItemListVO);

		SimpleMetaDataBean smdb = SimpleMetaDataBean.newInstanceDup(dupSimpleVO, '|');
		DelimiterMetaDataManager manager = new DelimiterMetaDataManager(smdb, '|');
		System.out.println(manager.deMaterialize());
	}
	
	public void testMetaDataBean() {
		SimpleVO simpleVO = new SimpleVO();
		simpleVO.setCampoDate(new Date());
		simpleVO.setCampoInteger(Integer.valueOf(34));
		simpleVO.setCampoLong(Long.valueOf(178));
		simpleVO.setCampoString("Teste do MetaDataBean");
		
		List<ItemListVO> listItemListVO = new ArrayList<ItemListVO>();
		listItemListVO.add(new ItemListVO("Item 1", Integer.valueOf(1)));
		listItemListVO.add(new ItemListVO("Item 2", Integer.valueOf(2)));
		listItemListVO.add(new ItemListVO("Item 3", Integer.valueOf(3)));
		simpleVO.setItemListVO(listItemListVO);
		
		SimpleMetaDataBean smdb = SimpleMetaDataBean.newInstance(simpleVO, '|');
		DelimiterMetaDataManager manager = new DelimiterMetaDataManager(smdb, '|');
		System.out.println(manager.deMaterialize());
		
		/*SimpleMetaDataBean smdbDem = SimpleMetaDataBean.newInstance(simpleVO, '|');
		DelimiterMetaDataManager managerDem = new DelimiterMetaDataManager(smdbDem, '|');
		managerDem.materialize("TESTE DO METADATABEAN|178|34|20130508|ITEM 1|1|ITEM 2|2|ITEM 3|3||");
		System.out.println(smdbDem.getSimpleVO());*/
	}
}