package spring.corp.framework.utils;

import java.math.BigDecimal;

import spring.corp.framework.exceptions.ConverterException;

public class BigDecimalUtils implements IConverter<BigDecimal>{

	public static final BigDecimal CEM = BigDecimal.valueOf(100D);
	private static BigDecimalUtils instance = new BigDecimalUtils();
	
	private BigDecimalUtils(){}
	public static BigDecimalUtils getInstance(){
		return instance;
	}
	
	/**
	 * Metodo responsavel por converter uma String em BigDecimal.
	 * @param valor O valor a ser convertido.
	 * @return O Valor convertido em BigDecimal ou nulo, caso o valor informado seja null.
	 * @exception ConverterException Caso ocorra alguma excecao na conversao.
	 */
	public BigDecimal convert(String valor) throws ConverterException{
		BigDecimal newBig = null;
		if (valor != null && !valor.equals("")) {
			try {
				newBig = FormatCurrency.getInstance().parseCurrency(valor);
			} catch (NumberFormatException e) {
				throw new ConverterException(this.getClass(), e);
			}
		}
		return newBig;
	}
	
	/**
	 * Transforma em String o valor BigDecimal levando em consideracao as casas decimais
	 * Caso o campo opcional seja true e valor informado seja zero, entao NULL sera retornado
	 * @param valor O Valor a ser convertido
	 * @param casaDecimal O numero de casas decimais
	 * @param opcional Indicativo se o campo eh opcional ou nao
	 * @return
	 */
	public String toString(BigDecimal valor, int casaDecimal, boolean opcional){
		String newStr = null;
		boolean isOpcionalAndValorZerado = (opcional && valor != null && valor.signum() == 0);
		if (isOpcionalAndValorZerado){
			newStr = null;
		}else{
			newStr = toString(valor, casaDecimal);
		}
		return newStr;
	}
	
	
	public boolean isBlank(BigDecimal value){
		return (value == null || value.signum() == 0);
	}
	
	public boolean isAllBlank(BigDecimal[] value){
		boolean and = true;
		for(BigDecimal b : value){
			and = and && isBlank(b);
		}
		return and;
	}
	
	public String toString(BigDecimal valor, int casaDecimal){
		String newStr = null;
		if (valor != null){
			try{
				newStr = String.format("%."+casaDecimal+"f", valor);
				int posCorte = newStr.lastIndexOf(".");
				if (posCorte >=0){
					String decimal = newStr.substring(posCorte+1);
					if (decimal.length() > casaDecimal){
						decimal = decimal.substring(0,casaDecimal);
						newStr = newStr.substring(0,posCorte)+"."+decimal;
					}
				}
			}catch(IllegalArgumentException e){
				newStr = "0";
			}
		}
		return newStr;
	}
	
	public BigDecimal parseBigDecimal(Number value) {
        if (value != null) {
            if (value instanceof BigDecimal){
                return (BigDecimal)value;
            }else if (value instanceof Long){
                return new BigDecimal(((Long)value).longValue());
            }else if (value instanceof Integer){
                return new BigDecimal(LongUtils.getInstance().parseLong(value));
            }
        }
        return BigDecimal.ZERO;
    }
    
    public BigDecimal parseBigDecimal(String value) {
        if (value != null) {
            return new BigDecimal(value);
        }
        return BigDecimal.ZERO;
    }
	
    /*
	public static void main(String argv[]){
		BigDecimal v = BigDecimalUtils.getInstance().convert("100.000.000.000,02");
		System.out.println(v);
		BigDecimal x = BigDecimal.valueOf(100.123);
		System.out.println(BigDecimalUtils.getInstance().toString(x,2));
	}
	*/
}