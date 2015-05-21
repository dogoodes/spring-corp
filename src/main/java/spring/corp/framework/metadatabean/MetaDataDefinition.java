package spring.corp.framework.metadatabean;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class MetaDataDefinition implements IMetaDataDefinition {

    private List<IMetaDataBean> metaDataBeans = new LinkedList<IMetaDataBean>();

    public void addMetaDataBean(IMetaDataBean metaDataBean) {
        metaDataBeans.add(metaDataBean);
    }

    public Iterator<IMetaDataBean> getIterator() {
        return metaDataBeans.iterator();
    }
}