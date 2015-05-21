package spring.corp.framework.metadatabean.types.mainframe;

import spring.corp.framework.metadatabean.TransformerUtils;
import spring.corp.framework.metadatabean.types.IType;

public class IgnoreType extends AbstractType implements IType {

    public IgnoreType(int length) {
        super(length);
    }

    public Object transform() {
        return TransformerUtils.toString(this, (String) valueIn);
    }
}