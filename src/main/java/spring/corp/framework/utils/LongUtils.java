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
	 * @param valor O valor a ser convertido
	 * @return Objeto Long
	 * @throws ConverterException
	 */
	public Long convert(String valor) throws ConverterException {
		Long newLong = null;
		if (valor != null && !valor.equals("")) {
			try {
				newLong = Long.valueOf(valor);
			} catch (NumberFormatException e) {
				throw new ConverterException(LongUtils.class, e);
			}
		}
		return newLong;
	}
	
	public static boolean isBlank(Long valor) {
		return (valor == null || Long.signum(valor) == 0);
	}
	
	public static long parseLong(String value) {
		if (!StringUtils.isBlank(value)) {
			return Long.parseLong(value.trim());
	    }
	    return 0L;
	}

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