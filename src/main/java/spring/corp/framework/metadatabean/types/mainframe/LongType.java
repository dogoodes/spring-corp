package spring.corp.framework.metadatabean.types.mainframe;

import spring.corp.framework.metadatabean.TransformerUtils;
import spring.corp.framework.metadatabean.types.IType;

public class LongType extends NumberType implements IType {

    public LongType(int length) {
        super(length);
    }

    public Object transform() {
        return TransformerUtils.toLong((String) valueIn);
    }
}