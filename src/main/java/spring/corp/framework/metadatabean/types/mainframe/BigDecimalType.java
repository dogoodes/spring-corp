package spring.corp.framework.metadatabean.types.mainframe;

import java.math.BigDecimal;

import spring.corp.framework.metadatabean.TransformerUtils;
import spring.corp.framework.metadatabean.types.IType;
import spring.corp.framework.utils.StringUtils;

public class BigDecimalType extends NumberType implements IType {

    private int decimal;

    public BigDecimalType(int length) {
        super(length);
        this.decimal = 2;
    }

    public BigDecimalType(int length, int decimal) {
        super(length);
        this.decimal = decimal;
    }

    public Object transform(Object value) {
        this.valueIn = value;
        if (value instanceof String) {
            this.valueOut = transform();
        } else {
            this.valueOut = transformInString(value);
        }
        return valueOut;
    }

    protected String transformInString(Object value) {
        if (value == null) {
            return TransformerUtils.toString(this, value);
        }
        String valueStr = value.toString();
        String inteiroStr = "";
        String decimalStr = "";
        int inteiro = length - decimal;
        boolean isDecimal = false;
        for (int i = 0, s = valueStr.length(); i < s; i++) {
            char algarismo = valueStr.charAt(i);
            if (algarismo == '.') {
                isDecimal = true;
                continue;
            }
            if (i < inteiro && !isDecimal) {
                inteiroStr += valueStr.charAt(i);
            } else {
                decimalStr += valueStr.charAt(i);
            }
        }
        inteiroStr = (inteiroStr.length() > inteiro) ? inteiroStr.substring(0, inteiro) : StringUtils.leftPad(inteiroStr, "0", inteiro);
        decimalStr = (decimalStr.length() > decimal) ? decimalStr.substring(0, decimal) : StringUtils.rightPad(decimalStr, decimal, '0');
        valueStr = inteiroStr + decimalStr;
        return valueStr;
    }

    public Object transform() {
        String valueStr = (String) this.valueIn;
        valueStr = valueStr.replaceAll("[,.]", "");
        if (StringUtils.isBlank(valueStr)) {
            return BigDecimal.ZERO;
        }
        int walk = 0;
        String inteiroStr = "";
        String decimalStr = "";
        boolean hasDot = false;
        for (int i = valueStr.length() - 1; i >= 0; i--) {
            char algarismo = valueStr.charAt(i);
            if (walk < decimal) {
                decimalStr = algarismo + decimalStr;
            } else {
                if (!hasDot) {
                    inteiroStr = "." + inteiroStr;
                    hasDot = true;
                }
                inteiroStr = algarismo + inteiroStr;
            }
            walk++;
        }
        valueStr = inteiroStr + decimalStr;
        return TransformerUtils.toBigDecimal(valueStr);
    }
}