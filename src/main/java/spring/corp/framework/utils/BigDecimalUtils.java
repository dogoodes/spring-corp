package spring.corp.framework.utils;

import java.math.BigDecimal;

import spring.corp.framework.exceptions.ConverterException;

public class BigDecimalUtils implements IConverter<BigDecimal> {

	public static final BigDecimal CEM = BigDecimal.valueOf(100D);
	private static BigDecimalUtils instance = new BigDecimalUtils();
	
	private BigDecimalUtils(){}
	public static BigDecimalUtils getInstance(){
		return instance;
	}
	
	/**
	 * Metodo responsável por converter uma String em BigDecimal.
	 * @param value (String) O valor a ser convertido.
	 * @return (BigDecimal) valor convertido em BigDecimal ou nulo, caso o valor informado seja null.
	 * @exception ConverterException Caso ocorra alguma exceçãoo na conversão.
	 */
	public BigDecimal convert(String value) throws ConverterException {
		BigDecimal newBig = null;
		if (value != null && !value.equals("")) {
			try {
				newBig = FormatCurrency.parseCurrency(value);
			} catch (NumberFormatException e) {
				throw new ConverterException(this.getClass(), e);
			}
		}
		return newBig;
	}
	
	/**
	 * Transformar em String o valor BigDecimal
	 * @param value (BigDecimal) valor para conversão
	 * @param numberDecimalPlaces (int) número de casas decimais que a String deverá conter
	 * @return (String) valor convertido ou nulo, caso o valor informado seja null.
	 */
	public static String toString(BigDecimal value, int numberDecimalPlaces) {
		String newStr = null;
		if (value != null) {
			try {
				newStr = String.format("%." + numberDecimalPlaces + "f", value);
				int posCorte = newStr.lastIndexOf(".");
				if (posCorte >=0) {
					String decimal = newStr.substring(posCorte+1);
					if (decimal.length() > numberDecimalPlaces) {
						decimal = decimal.substring(0,numberDecimalPlaces);
						newStr = newStr.substring(0,posCorte) + "." + decimal;
					}
				}
			} catch (IllegalArgumentException e) {
				newStr = "0";
			}
		}
		return newStr;
	}
	
	/**
	 * Transforma em String o valor BigDecimal levando em consideracao as casas decimais<br />
	 * Caso o campo opcional seja true e valor informado seja zero, entao NULL sera retornado
	 * @param value (BigDecimal) valor a ser convertido
	 * @param numberDecimalPlaces (int) número de casas decimais que a String deverá conter
	 * @param optional (boolean) Indicativo se o campo eh opcional ou nao
	 * @return (String) valor convertido ou nulo, caso o valor informado seja null. 
	 */
	public static String toString(BigDecimal value, int numberDecimalPlaces, boolean optional) {
		String newStr = null;
		boolean isOptionalAndValueZeroed = (optional && value != null && value.signum() == 0);
		if (isOptionalAndValueZeroed) {
			newStr = null;
		} else {
			newStr = toString(value, numberDecimalPlaces);
		}
		return newStr;
	}
	
	/**
	 * Verificar se o BigDecimal esta vazio
	 * @param value (BigDecimal) valor para verificação
	 * @return (boolean) true para BigDecimal vazio e false caso contrário
	 */
	public static boolean isBlank(BigDecimal value) {
		return (value == null || value.signum() == 0);
	}
	
	/**
	 * Verificar se todos os BigDecimal do array estão vazio
	 * @param values (BigDecimal[]) array de valores para verificação
	 * @return (boolean) true caso todos estejam vazio false caso algum não esteja
	 * @see spring.corp.framework.utils.BigDecimalUtils.isBlank(BigDecimal)
	 */
	public static boolean isAllBlank(BigDecimal[] values) {
		boolean and = true;
		for (BigDecimal value : values) {
			and = and && isBlank(value);
		}
		return and;
	}
	
	/**
	 * Converte Number em BigDecimal
	 * @param value (Number) valor para conversão
	 * @return (BigDecimal) valor convertido
	 */
	public static BigDecimal parseBigDecimal(Number value) {
        if (value != null) {
            if (value instanceof BigDecimal) {
                return (BigDecimal)value;
            } else if (value instanceof Long) {
                return new BigDecimal(((Long)value).longValue());
            } else if (value instanceof Integer) {
                return new BigDecimal(LongUtils.parseLong(value));
            }
        }
        return BigDecimal.ZERO;
    }
    
	/**
	 * Converte String em BigDecimal
	 * @param value (String) valor para conversão
	 * @return (BigDecimal) valor convertido
	 */
    public static BigDecimal parseBigDecimal(String value) {
        if (value != null) {
            return new BigDecimal(value);
        }
        return BigDecimal.ZERO;
    }
}