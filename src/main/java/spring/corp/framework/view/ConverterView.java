package spring.corp.framework.view;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import spring.corp.framework.exceptions.ConverterException;
import spring.corp.framework.utils.BigDecimalUtils;
import spring.corp.framework.utils.BooleanConverter;
import spring.corp.framework.utils.DateUtils;
import spring.corp.framework.utils.IConverter;
import spring.corp.framework.utils.IntegerUtils;
import spring.corp.framework.utils.LongUtils;
import spring.corp.framework.utils.StringConverter;

public final class ConverterView {
	
	private static final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
	private static final Lock r = rwl.readLock();
	private static final Lock w = rwl.writeLock();
	private static Map<Class<?>, IConverter<?>> converters = new HashMap<Class<?>, IConverter<?>>();
	static {
		converters.put(BigDecimal.class, BigDecimalUtils.getInstance());
		converters.put(Integer.class, IntegerUtils.getInstance());
		converters.put(Long.class, LongUtils.getInstance());
		converters.put(Calendar.class, DateUtils.getInstance());
		converters.put(String.class, StringConverter.getInstance());
		converters.put(Boolean.class, BooleanConverter.getInstance());
	}
	
	public static <T> void addConverter(Class<T> typeObject, IConverter<T> converter){
		w.lock();
		try {
			if (converters.get(typeObject) == null) {
				converters.put(typeObject, converter);
			}
		} finally {
			w.unlock();
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T convert(Class<T> typeObjectCoverted, String objToConverter) throws ConverterException {
		IConverter<?> convert = null;
		r.lock();
		try {
			convert =  converters.get(typeObjectCoverted);
		} finally {
			r.unlock();
		}
		return (T) convert.convert(objToConverter);
	}
}