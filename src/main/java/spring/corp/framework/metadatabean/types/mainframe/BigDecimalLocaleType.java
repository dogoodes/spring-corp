package spring.corp.framework.metadatabean.types.mainframe;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class BigDecimalLocaleType extends AbstractType {

	private Locale locale;
	private int decimal;

	public BigDecimalLocaleType(int length, int decimal, Locale locale) {
		super(length);
		this.locale = locale;
		this.decimal = decimal;
	}

	@Override
	public Object transform(Object value) {
		this.valueIn = value;
		this.valueOut = transform();
		return valueOut;
	}

	@Override
	public Object transform() {
		if (valueIn == null) {
			valueOut = "";
		} else {
			BigDecimal value = (BigDecimal) valueIn;
			NumberFormat nf = NumberFormat.getNumberInstance(locale);
			DecimalFormat df = (DecimalFormat) nf;
			df.setMinimumFractionDigits(decimal);
			df.setMaximumFractionDigits(decimal);
			df.setMinimumIntegerDigits(1);
			df.setMaximumIntegerDigits(length);
			df.setGroupingSize(length);
			valueOut = df.format(value);
		}
		return valueOut;
	}
}