package spring.corp.framework.metadatabean.types.mainframe;

import spring.corp.framework.metadatabean.TransformerUtils;
import spring.corp.framework.metadatabean.types.IType;

public abstract class AbstractType implements IType {

    protected Object valueIn;
    protected Object valueOut;
    protected int length;
    
    public AbstractType(int length){
        this.length = length;
    }
    
    public Object transform(Object value) {
        this.valueIn = value;
        if (value instanceof String) {
            this.valueOut = transform();
        } else {
            this.valueOut = TransformerUtils.toString(this, value);
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
    
    public abstract Object transform();
}