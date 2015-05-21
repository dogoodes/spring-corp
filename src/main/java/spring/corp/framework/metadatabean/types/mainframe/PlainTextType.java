package spring.corp.framework.metadatabean.types.mainframe;

public class PlainTextType extends AbstractType {

	public PlainTextType(int length) {
		super(length);
	}

	@Override
	public Object transform() {
		if (valueIn == null) {
			valueOut = "";
		} else {
			valueOut = valueIn;
		}
		return valueOut;
	}
}