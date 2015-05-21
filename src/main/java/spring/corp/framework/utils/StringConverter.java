package spring.corp.framework.utils;

import spring.corp.framework.exceptions.ConverterException;

public class StringConverter implements IConverter<String> {

	private static StringConverter instance = new StringConverter();

	private StringConverter() {}
	public static StringConverter getInstance() {
		return instance;
	}

	@Override
	public String convert(String valor) throws ConverterException {
		return valor;
	}
}