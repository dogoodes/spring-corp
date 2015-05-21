package spring.corp.framework.metadatabean;

import spring.corp.framework.metadatabean.DelimiterMetaDataManager;
import spring.corp.framework.metadatabean.IMetaData;
import spring.corp.framework.metadatabean.IMetaDataDefinition;
import spring.corp.framework.metadatabean.MetaDataBean;
import spring.corp.framework.metadatabean.MetaDataDefinition;
import spring.corp.framework.metadatabean.OccursMetaDataBean;
import spring.corp.framework.metadatabean.types.ConstructorType;
import spring.corp.framework.metadatabean.types.mainframe.CarryReturn;
import spring.corp.framework.metadatabean.types.mainframe.DateType;
import spring.corp.framework.metadatabean.types.mainframe.IntegerType;
import spring.corp.framework.metadatabean.types.mainframe.LongType;
import spring.corp.framework.metadatabean.types.mainframe.StringType;

public class SimpleMetaDataBean implements IMetaData {
	
	private final IMetaDataDefinition definition = new MetaDataDefinition();
	private final char delimiter;
	private SimpleVO simpleVO;
	private DupSimpleVO dupSimpleVO;
	
	@Override
	public IMetaDataDefinition getMetaDataDefinition() {
		return definition;
	}
	
	private SimpleMetaDataBean(SimpleVO simpleVO, char delimiter) {
		this.simpleVO = simpleVO;
		this.delimiter = delimiter;
	}
	
	private SimpleMetaDataBean(DupSimpleVO dupSimpleVO, char delimiter) {
		this.dupSimpleVO = dupSimpleVO;
		this.delimiter = delimiter;
	}
	
	public static SimpleMetaDataBean newInstance(SimpleVO simpleVO, char delimiter) {
		SimpleMetaDataBean smdb = new SimpleMetaDataBean(simpleVO, delimiter);
		smdb.loadDefinition();
		return smdb;
	}
	
	public static SimpleMetaDataBean newInstanceDup(DupSimpleVO dupSimpleVO, char delimiter) {
		SimpleMetaDataBean smdb = new SimpleMetaDataBean(dupSimpleVO, delimiter);
		smdb.loadDefinitionDup();
		return smdb;
	}
	
	private void loadDefinition() {
		definition.addMetaDataBean(new MetaDataBean("simpleVO.campoString", new StringType(0)));
		definition.addMetaDataBean(new MetaDataBean("simpleVO.campoLong", new LongType(0)));
		definition.addMetaDataBean(new MetaDataBean("simpleVO.campoInteger", new IntegerType(0)));
		definition.addMetaDataBean(new MetaDataBean("simpleVO.campoDate", new DateType(0)));
		
		ConstructorType constructorType = new ConstructorType(ItemListVO.class, ConstructorType.DEFAULT);
		OccursMetaDataBean occursMetaDataBean = new OccursMetaDataBean(constructorType, new DelimiterMetaDataManager(null, this.delimiter));
		occursMetaDataBean.addMetaDataBean(new MetaDataBean("itemString", new StringType(0)));
		occursMetaDataBean.addMetaDataBean(new MetaDataBean("itemInteger", new IntegerType(0)));
		occursMetaDataBean.flush(); //TODO: Altera o Occurs para que ele trabalhe com o Pattern Builder, com isso tira a dependencia do flush()
		definition.addMetaDataBean(new MetaDataBean("simpleVO.itemListVO", occursMetaDataBean));
	}
	
	private void loadDefinitionDup() {
		definition.addMetaDataBean(new MetaDataBean("dupSimpleVO.ignore", new StringType(0)));
		definition.addMetaDataBean(new MetaDataBean("dupSimpleVO.umaString", new StringType(0)));
		definition.addMetaDataBean(new MetaDataBean("dupSimpleVO.ignore", new CarryReturn(0)));
		
		ConstructorType constructorTypeDup = new ConstructorType(DupItemListVO.class, ConstructorType.DEFAULT);
		OccursMetaDataBean occursMetaDataBeanDup = new OccursMetaDataBean(constructorTypeDup, new DelimiterMetaDataManager(null, this.delimiter));
		ConstructorType constructorType = new ConstructorType(ItemListVO.class, ConstructorType.DEFAULT);
		OccursMetaDataBean occursMetaDataBean = new OccursMetaDataBean(constructorType, new DelimiterMetaDataManager(null, this.delimiter));
		occursMetaDataBean.addMetaDataBean(new MetaDataBean("itemString", new StringType(0)));
		occursMetaDataBean.addMetaDataBean(new MetaDataBean("itemInteger", new IntegerType(0)));
		occursMetaDataBean.addMetaDataBean(new MetaDataBean("ignore", new CarryReturn(0)));
		occursMetaDataBean.flush();
		occursMetaDataBeanDup.addMetaDataBean(new MetaDataBean("umaString", new StringType(0)));
		occursMetaDataBeanDup.addMetaDataBean(new MetaDataBean("ignore", new CarryReturn(0)));
		occursMetaDataBeanDup.addMetaDataBean(new MetaDataBean("listItemListVO", occursMetaDataBean));
		//occursMetaDataBeanDup.addMetaDataBean(new MetaDataBean("ignore", new CarryReturn(0)));
		occursMetaDataBeanDup.flush();
		definition.addMetaDataBean(new MetaDataBean("dupSimpleVO.dupItemListVO", occursMetaDataBeanDup));
	}

	public SimpleVO getSimpleVO() {
		return simpleVO;
	}

	public void setSimpleVO(SimpleVO simpleVO) {
		this.simpleVO = simpleVO;
	}

	public DupSimpleVO getDupSimpleVO() {
		return dupSimpleVO;
	}

	public void setDupSimpleVO(DupSimpleVO dupSimpleVO) {
		this.dupSimpleVO = dupSimpleVO;
	}
}