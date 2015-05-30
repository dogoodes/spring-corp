package spring.corp.framework.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import spring.corp.framework.exceptions.ConverterException;
import spring.corp.framework.i18n.LocaleUtils;

/**
 * Classe utilitaria de data. E fortemente recomendado que essa classe seja utilizado quando um objeto Calendar precisar ser criado pois esta classe
 * forca o Locale para pt_BR padrao esse a ser utilizado pela aplicacao.
 */
public class DateUtils implements IConverter<Calendar> {
	
	private static DateUtils instance = new DateUtils();
	public static final String BRAZILIAN_PATTERN = "dd/MM/yyyy";
	public static final SimpleDateFormat completeTimer = new SimpleDateFormat("ss.SSSSSSZ");
	
	private DateUtils() {}
	public static DateUtils getInstance() {
		return instance;
	}
	
	/**
	 * Converte a string informada em data, a string deve ter o formato dd/MM/yyyy
	 * @param date String que representa uma data
	 * @return A Data que estava representada na string
	 * @throws ConverterException Caso o formato nao seja compativel com a formatacao da String
	 */
	public Calendar convert(String date) throws ConverterException {
		Calendar data = null;
		if (date != null && !date.equals("")) {
			try {
				data = stringToCalendar(date, BRAZILIAN_PATTERN);
			} catch (ParseException e) {
				throw new ConverterException(this.getClass(), e);
			}
		}
		return data;
	}
	
	/**
	 * Retorna um objeto Calendar com o Locale pt_BR
	 */
	public static Calendar getCalendar() {
		return Calendar.getInstance(LocaleUtils.DEFAULT_LOCALE);
	}
	
	public static String today(String pattern){
		return dateToString(Calendar.getInstance(), pattern);
	}
	
	/**
	 * Transforma o objeto Date em string seguindo o formato definido no parametro pattern
	 * @param date Data a ser transformada
	 * @param pattern Formato de saida da data
	 * @return Uma string com a data formatada
	 */
	public static String dateToString(Calendar date, String pattern) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat format = new SimpleDateFormat(pattern, LocaleUtils.DEFAULT_LOCALE);
		return format.format(date.getTime());
	}
	
    /**
     * Transforma uma data (Date) em String. Exemplo de pattern: ddMMyyyy.
     * @param date Data.
     * @param pattern padrao desejado.
     * @return Data formato String.
     */
    public static String dateToString(Date date, String pattern) {
        if (date == null) {
        	return "";        	
        }
        SimpleDateFormat format = new SimpleDateFormat(pattern, LocaleUtils.DEFAULT_LOCALE);
        return format.format(date);
    }
    
	/**
	 * Converte a string informada em data, a string deve ter o formado informado em pattern
	 * @param date String que representa uma data
	 * @param pattern Formato de como a data esta representado pelo string
	 * @return A Data que estava representada na string
	 * @throws ParseException Caso o formato nao seja compativel com a formatacao da String
	 */
	public static Calendar stringToCalendar(String date, String pattern) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat(pattern, LocaleUtils.DEFAULT_LOCALE);
		Date d = format.parse(date);
		Calendar c = getCalendar();
		c.setTime(d);
		return c;
	}
	
	/**
     * Cria um objeto Date a partir de uma String e um padao. Exemplo de pattern: ddMMyyyy.
     * @param date Data a ser criada.
     * @param pattern padrao.
     * @return Data criada
     * @throws ParseException Caso ocorra algum erro.
     */
    public static Date stringToDate(String date, String pattern) throws ParseException {
        if (date.length() == 7) {
            date = "0" + date;
        }
        SimpleDateFormat format = new SimpleDateFormat(pattern, LocaleUtils.DEFAULT_LOCALE);
        return format.parse(date);
    }
	
	/**
	 * Retorna um Integer no formato YYYYMMDD do calendar informado como parametro;
	 * @param data Data que sera transformanda em Integer
	 * @return Integer representando a data, ou nulo caso o parametro seja nulo.
	 */
	public static Integer calendarToInteger(Calendar data) {
		if (data != null) {
			return Integer.valueOf(data.get(Calendar.YEAR) + "" + StringUtils.leftPad("" + (data.get(Calendar.MONTH) + 1), "0", 2) + "" + StringUtils.leftPad("" + data.get(Calendar.DAY_OF_MONTH), "0", 2));
		}
		return null;
	}
	
	public static Integer calendarToIntegerHora(Calendar data) {
		if (data != null) {
			SimpleDateFormat format = new SimpleDateFormat("HHmmss", LocaleUtils.DEFAULT_LOCALE);
			String hora =  format.format(data.getTime());
			return Integer.valueOf(hora);
		}
		return Integer.valueOf(0);
	}
	
	public static Integer stringToInteger(String date, String pattern) throws ParseException {
		Calendar c = stringToCalendar(date, pattern);
		return calendarToInteger(c);
	}
	
	public static Integer stringToIntegerHora(String date, String pattern) throws ParseException {
		Calendar c = stringToCalendar(date, pattern);
		return calendarToIntegerHora(c);
	}
}