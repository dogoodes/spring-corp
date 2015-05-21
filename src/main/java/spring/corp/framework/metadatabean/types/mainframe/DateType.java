package spring.corp.framework.metadatabean.types.mainframe;

import java.util.Calendar;
import java.util.Date;

import spring.corp.framework.metadatabean.types.IType;
import spring.corp.framework.utils.DateUtils;
import spring.corp.framework.utils.StringUtils;

public class DateType extends AbstractType implements IType {

    private String pattern = "yyyyMMdd";

    public DateType(int length) {
        super(length);
        if (length == 6) {
            pattern = "yyyyMM";
        }
    }

    public DateType(int length, String pattern) {
        super(length);
        this.pattern = pattern;
    }

    @SuppressWarnings("static-access")
	public Object transform(Object value) {
        this.valueIn = value;
        if (value == null) {
            this.valueOut = StringUtils.leftPad("", "", this.length);
        } else {
            if (value instanceof String){
                this.valueOut = transform();
            }else if (value instanceof Date) {
                this.valueOut = DateUtils.getInstance().dateToString((Date)value, pattern);
            }else if (value instanceof Calendar){
                this.valueOut = DateUtils.getInstance().dateToString(((Calendar)value).getTime(), pattern);
            }
        }
        return valueOut;
    }
    
	@Override
	@SuppressWarnings("static-access")
    public Object transform() {
        try {
            if (!StringUtils.isBlank((String)this.valueIn)) {
                Date date = DateUtils.getInstance().stringToDate((String)valueIn, pattern);
                this.valueOut = date;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.valueOut;
    }
}