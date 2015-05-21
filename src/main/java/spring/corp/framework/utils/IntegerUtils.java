package spring.corp.framework.utils;

import java.util.Date;

import spring.corp.framework.exceptions.ConverterException;
import spring.corp.framework.view.RegexValidation;

public class IntegerUtils implements IConverter<Integer> {

	private static IntegerUtils instance = new IntegerUtils();
	
	private IntegerUtils(){}
	public static IntegerUtils getInstance(){
		return instance;
	}
	
	/**
	 * Metodo responsavel por converter o valor String informado em Integer.
	 * @param valor O valor a ser transformado em Integer
	 * @return O Valor transformado em Integer
	 * @throws ConverterException Caso ocorra algum erro na conversao.
	 */
	public Integer convert(String valor) throws ConverterException {
		Integer newInt = null;
		if (valor != null && !valor.equals("")) {
			try {
				newInt = Integer.valueOf(valor);
			} catch (NumberFormatException nfe) {
				throw new ConverterException(this.getClass(), nfe);
			}
		}
		return newInt;
	}
	
	public static boolean isOnlyNumber(String str) {
		boolean ret = false;
		if (!StringUtils.isBlank(str)) {
			ret = str.matches(RegexValidation.OnlyNumbers.expression());
		}
		return ret;
	}
	
	public static boolean isBlank(String valor) {
		boolean isBlank = StringUtils.isBlank(valor);
		if (!isBlank) {
			if (isOnlyNumber(valor)) {
				isBlank = (Integer.valueOf(valor).intValue() == 0);
			}
		}
		return isBlank;
	}
	
    public static int parseInt(Number value) {
        if (value != null) {
            return value.intValue();
        }
        return 0;
    }

    public static int parseInt(String value) {
        if (!StringUtils.isBlank(value)) {
            return Integer.parseInt(value.trim());
        }
        return 0;
    }

    public static int parseInt(Date data, String pattern) {
        int ret = 0;
        if (data != null) {
            @SuppressWarnings("static-access")
			String strData = DateUtils.getInstance().dateToString(data, pattern);
            ret = parseInt(strData);
        }
        return ret;
    }
}