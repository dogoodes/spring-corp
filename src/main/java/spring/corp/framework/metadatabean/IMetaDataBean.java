package spring.corp.framework.metadatabean;

import spring.corp.framework.metadatabean.types.IType;

public interface IMetaDataBean extends Cloneable {

    public String getName();
    public IType getType();
    public IMetaDataBean deepCopy() throws CloneNotSupportedException;
}