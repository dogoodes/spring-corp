package spring.corp.framework.utils;

import spring.corp.framework.exceptions.ConverterException;

public class LongUtils implements IConverter<Long> {

	private static LongUtils instance = new LongUtils();
	
	private LongUtils(){}
	
	public static LongUtils getInstance(){
		return instance;
	}
	
	/**
	 * Converter um objeto String em Long
	 * @param valor (String) valor a ser convertido
	 * @return (Long) valor convertido
	 * @throws ConverterException caso string inválida para conversão ou algum outro problema
	 */
	public Long convert(String value) throws ConverterException {
		Long newLong = null;
		if (value != null && !value.equals("")) {
			try {
				newLong = Long.valueOf(value);
			} catch (NumberFormatException e) {
				throw new ConverterException(LongUtils.class, e);
			}
		}
		return newLong;
	}
	
	/**
	 * Verificar se o Long esta vazio
	 * @param value (Long) valor para verificação
	 * @return (boolean) true para Long vazio e false caso contrário
	 */
	public static boolean isBlank(Long value) {
		return (value == null || Long.signum(value) == 0);
	}
	
	/**
	 * Converte String em long
	 * @param value (String) valor para conversão
	 * @return (long) valor convertido
	 */
	public static long parseLong(String value) {
		if (!StringUtils.isBlank(value)) {
			return Long.parseLong(value.trim());
	    }
	    return 0L;
	}

	/**
	 * Converte Number em long
	 * @param value (Number) valor para conversão
	 * @return (long) valor convertido
	 */
	public static long parseLong(Number value) {
		if (value != null) {
			if (value instanceof Long) {
				return value.longValue();
	        } else if (value instanceof Integer) {
	        	long l = (long) value.intValue();
	            return Long.valueOf(l);
	        }
	    }
	    return 0;
	}
}