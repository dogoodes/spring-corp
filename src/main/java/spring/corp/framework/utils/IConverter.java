package spring.corp.framework.utils;

import spring.corp.framework.exceptions.ConverterException;

public interface IConverter<T> {

	public T convert(String valor) throws ConverterException;
}