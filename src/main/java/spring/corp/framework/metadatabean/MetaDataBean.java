package spring.corp.framework.metadatabean;

import spring.corp.framework.metadatabean.types.IType;

public class MetaDataBean implements IMetaDataBean {

    private final String name;
    private final IType type;

    public MetaDataBean(String name, IType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public IType getType() {
        return type;
    }

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
	public IMetaDataBean deepCopy() throws CloneNotSupportedException{
		return (IMetaDataBean)clone();
	}
}