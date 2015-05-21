package spring.corp.framework.metadatabean;

import java.util.Iterator;

public interface IMetaDataDefinition {

    public void addMetaDataBean(IMetaDataBean metaDataBean);
    public Iterator<IMetaDataBean> getIterator();
}