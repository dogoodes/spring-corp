package spring.corp.framework.metadatabean.types;

public interface IType {

    public Object transform(Object value);
    public Object getValueIn();
    public Object getValueOut();
    public int getLength();
}