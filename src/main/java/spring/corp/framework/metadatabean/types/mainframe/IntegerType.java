package spring.corp.framework.metadatabean.types.mainframe;

import spring.corp.framework.metadatabean.TransformerUtils;
import spring.corp.framework.metadatabean.types.IType;

public class IntegerType extends NumberType implements IType {
  
    public IntegerType(int length) {
        super(length);
    }

    public Object transform() {
        return TransformerUtils.toInteger((String)valueIn);
    }
}