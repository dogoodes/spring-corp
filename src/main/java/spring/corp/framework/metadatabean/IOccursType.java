package spring.corp.framework.metadatabean;

import spring.corp.framework.metadatabean.types.IType;

public interface IOccursType extends IType {
 
	public void addMetaDataBean(IMetaDataBean metaDataBean);
    public void flush();
    public IMetaDataDefinition getOccurMetaDataDefinition(); 
}