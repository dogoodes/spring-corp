package spring.corp.framework.metadatabean.types.mainframe;

import spring.corp.framework.metadatabean.types.IType;

public class BooleanType implements IType {

    protected Object valueIn;
    protected Object valueOut;
    protected int length = 1;

    public Object transform(Object value) {
        this.valueIn = value;
        if (value instanceof String) {
            this.valueOut = "S".equals(value);
        } else if (value instanceof Boolean) {
            this.valueOut = ((Boolean) value).booleanValue() ? "S" : "N";
        } else {
            this.valueOut = " ";
        }
        return valueOut;
    }

    public Object getValueOut() {
        return valueOut;
    }

    public Object getValueIn() {
        return valueIn;
    }

    public int getLength() {
        return length;
    }
}