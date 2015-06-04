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
	 * Metodo responsável por converter o valor String informado em Integer
	 * @param value (String) valor a ser transformado em Integer
	 * @return (Integer) valor transformado em Integer
	 * @throws ConverterException Caso ocorra algum erro na conversão.
	 */
	public Integer convert(String value) throws ConverterException {
		Integer newInt = null;
		if (value != null && !value.equals("")) {
			try {
				newInt = Integer.valueOf(value);
			} catch (NumberFormatException nfe) {
				throw new ConverterException(this.getClass(), nfe);
			}
		}
		return newInt;
	}
	
	/**
	 * Verifica se a String possui apenas números
	 * @param str (String) string para verificação
	 * @return (boolean) true caso a String possua apenas número e false caso possua algum caracter que não seja número
	 */
	public static boolean isOnlyNumber(String value) {
		boolean ret = false;
		if (!StringUtils.isBlank(value)) {
			ret = value.matches(RegexValidation.OnlyNumbers.expression());
		}
		return ret;
	}
	
	/**
	 * Verificar se a String esta vazia
	 * <p>Exemplos: 
	 * <ul>
	 * 	 <li>" " == false</li>
	 * 	 <li>"" == true</li>
	 *   <li>"0" == true</li>
	 *   <li>"1" == false</li>
	 * </ul></p>
	 * @param value (String) valor para verificação
	 * @return (boolean) true para String vazia e false caso contrário
	 * @see spring.corp.framework.utils.StringUtils.isBlank(String)
	 */
	public static boolean isBlank(String value) {
		boolean isBlank = StringUtils.isBlank(value);
		if (!isBlank) {
			if (isOnlyNumber(value)) {
				isBlank = (Integer.valueOf(value).intValue() == 0);
			}
		}
		return isBlank;
	}
	
	/**
	 * Converte Number em int
	 * @param value (Number) para conversão
	 * @return (int) valor do Number no formato int
	 */
    public static int parseInt(Number value) {
        if (value != null) {
            return value.intValue();
        }
        return 0;
    }

    /**
     * Converte String em int caso o valor contenha apenas números 
     * @param value (String) para conversão
     * @return (int) valor da String no formato int ou 0 (zero) caso isOnlyNumber() seja igual a false
     */
    public static int parseInt(String value) {
        if (!StringUtils.isBlank(value) && isOnlyNumber(value)) {
            return Integer.parseInt(value.trim());
        }
        return 0;
    }

    /**
     * Transforma uma data (Date) em Integer no formato desejado
     * @param date (Date) data para conversão
     * @param pattern (String) padrão desejado
     * @return (int) representando a data, ou nulo caso o parametro seja nulo.
     */
    public static int parseInt(Date data, String pattern) {
        int ret = 0;
        if (data != null) {
			String strData = DateUtils.dateToString(data, pattern);
            ret = parseInt(strData);
        }
        return ret;
    }
    
    /**
     * Transforma uma data (Date) em Integer no formato yyyyMMdd
     * @param date (Date) data para conversão
     * @return (int) representando a data, ou nulo caso o parametro seja nulo.
     * @see spring.corp.framework.utils.IntegerUtils.parseInt(Date, String)
     */
    public static int parseInt(Date data) {
    	return parseInt(data, "yyyyMMdd");
    }
}