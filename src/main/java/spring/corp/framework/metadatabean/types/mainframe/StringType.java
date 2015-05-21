package spring.corp.framework.metadatabean.types.mainframe;

import spring.corp.framework.metadatabean.types.IType;
import spring.corp.framework.utils.StringUtils;

public class StringType extends AbstractType implements IType {

    public StringType(int length) {
        super(length);
    }

    public Object transform() {
        return StringUtils.normalizeToMainframe((String) valueIn, length);
    }
}