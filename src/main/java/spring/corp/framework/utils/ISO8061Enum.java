package spring.corp.framework.utils;

public enum ISO8061Enum {
	
	YEAR("yyyy"), 
	YEAR_MONTH("yyyy-MM"), 
	COMPLETE_DATE("yyyy-MM-dd"), 
	COMPLETE_DATE_PLUS_HOUR_MINUTE("yyyy-MM-dd'T'HH:mm"), 
	COMPLETE_DATE_PLUS_HOUR_MINUTE_SECONDS("yyyy-MM-dd'T'HH:mm:ss"), 
	COMPLETE_DATE_PLUS_HOUR_MINUTE_SECOND_FRACTION_SEC("yyyy-MM-dd'T'HH:mm:ss.s");
	
	private final String formatter;
	
	private ISO8061Enum(String formatter){
		this.formatter = formatter;
	}
	
	public String getFormatter(){
		return formatter;
	}
}