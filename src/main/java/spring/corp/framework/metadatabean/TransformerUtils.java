package spring.corp.framework.metadatabean;

import java.math.BigDecimal;

import spring.corp.framework.metadatabean.types.IType;
import spring.corp.framework.metadatabean.types.mainframe.NumberType;
import spring.corp.framework.utils.BigDecimalUtils;
import spring.corp.framework.utils.IntegerUtils;
import spring.corp.framework.utils.LongUtils;
import spring.corp.framework.utils.StringUtils;

public class TransformerUtils {

    public static Integer toInteger(String dado) {
        return IntegerUtils.getInstance().parseInt(dado);
    }

    public static Long toLong(String dado) {
        return LongUtils.parseLong(dado);
    }

    public static BigDecimal toBigDecimal(String dado) {
        return BigDecimalUtils.getInstance().parseBigDecimal(dado);
    }
   
    public static String toString(IType type, Object value) {
        String pad = "";
        int length = type.getLength();
        String valueStr = (value == null)?"":value.toString();
        if (type instanceof NumberType) {
            pad = "0";
            valueStr = valueStr.replaceAll("[.,]", "");
            valueStr = StringUtils.leftPad(valueStr, pad, length); 
        } else {
            valueStr = StringUtils.rightPad(valueStr, length, pad);
        }
        return valueStr;
    }
}