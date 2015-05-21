package spring.corp.framework.metadatabean.types.mainframe;

import spring.corp.framework.metadatabean.TransformerUtils;
import spring.corp.framework.metadatabean.types.IType;

public class CarryReturn extends AbstractType implements IType {

	public CarryReturn(int length) {
		super(length);
	}

	@Override
	public Object transform() {
		return TransformerUtils.toString(this, (String) "\r\n");
	}
}