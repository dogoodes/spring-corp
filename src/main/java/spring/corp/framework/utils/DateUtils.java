package spring.corp.framework.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import spring.corp.framework.exceptions.ConverterException;

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
	 * Converte a string informada em data (a string deve ter o formato dd/MM/yyyy)
	 * @param date (String) representa a data
	 * @return (Calendar) data que estava representada na string
	 * @throws ConverterException Caso o formato não seja compatível com a formatação da String
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
	 * Retorna um objeto Calendar
	 * @return (Calendar) calendar com o Locale pt_BR
	 */
	public static Calendar getCalendar() {
		return Calendar.getInstance(LocaleUtils.DEFAULT_LOCALE);
	}
	
	/**
	 * Data atual (hoje)
	 * @param pattern (String) padrão desejado
	 * @return (String) data no formato string com padrão do parametro
	 */
	public static String today(String pattern){
		return dateToString(Calendar.getInstance(), pattern);
	}
	
	/**
	 * Transforma o objeto Date em string seguindo o formato definido no parametro pattern
	 * @param date (Calendar) data a ser transformada
	 * @param pattern (String) formato de saída da data
	 * @return (String) data no formato string com padrão do parametro
	 */
	public static String dateToString(Calendar date, String pattern) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat format = new SimpleDateFormat(pattern, LocaleUtils.DEFAULT_LOCALE);
		return format.format(date.getTime());
	}
	
    /**
     * Transforma uma data (Date) em String
     * Exemplo de pattern: ddMMyyyy
     * @param date (Date) data para conversão
     * @param pattern (String) padrão desejado
     * @return (String) data no formato string com padrão do parametro
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
	 * @param date (String) representa uma data
	 * @param pattern (String) padrão em que a data esta formatada
	 * @return (Calendar) data que estava representada na string
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
     * Cria um objeto Date a partir de uma String e um padrão
     * Exemplo de pattern: ddMMyyyy
     * @param date (String) data a ser criada.
     * @param pattern (String) padrão em que a data esta formatada
     * @return (Date) data que estava representada na string
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
	 * Retorna um Integer no formato yyyyMMdd do calendar informado como parametro
	 * @param date (Calendar) data que será transformada em Integer
	 * @return (Integer) representando a data, ou nulo caso o parametro seja nulo.
	 */
	public static Integer calendarToInteger(Calendar date) {
		if (date != null) {
			return Integer.valueOf(date.get(Calendar.YEAR) + "" + StringUtils.leftPad("" + (date.get(Calendar.MONTH) + 1), "0", 2) + "" + StringUtils.leftPad("" + date.get(Calendar.DAY_OF_MONTH), "0", 2));
		}
		return null;
	}
	
	/**
	 * Retorna um Integer no formato HHmmss do calendar informado como parametro
	 * @param date (Calendar) data que terá sua hora transformada em Integer
	 * @return (Integer) representando a hora, ou nulo caso o parametro seja nulo.
	 */
	public static Integer calendarToIntegerHora(Calendar data) {
		if (data != null) {
			SimpleDateFormat format = new SimpleDateFormat("HHmmss", LocaleUtils.DEFAULT_LOCALE);
			String hora =  format.format(data.getTime());
			return Integer.valueOf(hora);
		}
		return Integer.valueOf(0);
	}
	
	/**
	 * Retorna um Integer no formato yyyyMMdd da string informada como parametro
	 * @param date (Calendar) data que será transformada em Integer
	 * @param pattern (String) padrão em que a data esta formatada
	 * @return (Integer) representando a data, ou nulo caso o parametro seja nulo.
	 */
	public static Integer stringToInteger(String date, String pattern) throws ParseException {
		Calendar c = stringToCalendar(date, pattern);
		return calendarToInteger(c);
	}
	
	/**
	 * Retorna um Integer no formato HHmmss da string informada como parametro
	 * @param date (Calendar) data que será transformada em Integer
	 * @param pattern (String) padrão em que a data esta formatada
	 * @return (Integer) representando a hora, ou nulo caso o parametro seja nulo.
	 */
	public static Integer stringToIntegerHora(String date, String pattern) throws ParseException {
		Calendar c = stringToCalendar(date, pattern);
		return calendarToIntegerHora(c);
	}
}