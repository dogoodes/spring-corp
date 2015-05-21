package spring.corp.framework.view;

public enum RegexValidation implements IRegexValidation {
	
	Cpf("^[0-9]{11}$"),
	CpfFormatado("^[0-9]{3}.[0-9]{3}.[0-9]{3}-[0-9]{2}$"),
	Cnpj("^[0-9]{14}$"),
	CnpjFormatadoComum("^[0-9]{2}.[0-9]{3}.[0-9]{3}/[0-9]{4}-[0-9]{2}$"),
	CnpjFormatadoIncomum("^[0-9]{2}.[0-9]{3}.[0-9]{3}/[0-9]{4}-[0-9]{2}$"),
	InscricaoEstadualDestinatario("[0-9]{0,14}|ISENTO|PR[0-9]{4,8}"),
	InscricaoEstadualEmitente("[0-9]{2,14}|ISENTO"),
	Suframa("[0-9]{8,9}"),
	EAN("[0-9]{0}|[0-9]{8}|[0-9]{12,14}"),
	Serie("0|[1-9]{1}[0-9]{0,2}"),
	Renavam9digitos("^[0-9]{9}$"),
	Renavam11digitos("^[0-9]{11}$"),
	Renavam("^[0-9]{9}$|^[0-9]{10}$|^[0-9]{11}$"),
	TDEC_0302("0|0\\.[0-9]{2}|[1-9]{1}[0-9]{0,2}(\\.[0-9]{2})?", true),
	TDEC_0302Opc("0\\.[0-9]{1}[1-9]{1}|0\\.[1-9]{1}[0-9]{1}|[1-9]{1}[0-9]{0,2}(\\.[0-9]{2})?", true),
	TDEC_0803("0|0\\.[0-9]{3}|[1-9]{1}[0-9]{0,7}(\\.[0-9]{3})?", true),
	TDEC_0803Opc("0\\.[1-9]{1}[0-9]{2}|0\\.[0-9]{2}[1-9]{1}|0\\.[0-9]{1}[1-9]{1}[0-9]{1}|[1-9]{1}[0-9]{0,7}(\\.[0-9]{3})?", true),
	TDEC_0804("0|0\\.[0-9]{4}|[1-9]{1}[0-9]{0,7}(\\.[0-9]{4})?", true),
	TDEC_0804Opc("0\\.[1-9]{1}[0-9]{3}|0\\.[0-9]{3}[1-9]{1}|0\\.[0-9]{2}[1-9]{1}[0-9]{1}|0\\.[0-9]{1}[1-9]{1}[0-9]{2}|[1-9]{1}[0-9]{0,7}(\\.[0-9]{4})?", true),
	TDEC_1104("0|0\\.[0-9]{4}|[1-9]{1}[0-9]{0,10}(\\.[0-9]{4})?", true),
	TDEC_1104Opc("0\\.[1-9]{1}[0-9]{3}|0\\.[0-9]{3}[1-9]{1}|0\\.[0-9]{2}[1-9]{1}[0-9]{1}|0\\.[0-9]{1}[1-9]{1}[0-9]{2}|[1-9]{1}[0-9]{0,10}(\\.[0-9]{4})?", true),
	TDEC_1203("0|0\\.[0-9]{3}|[1-9]{1}[0-9]{0,11}(\\.[0-9]{3})?", true),
	TDEC_1203Opc("0\\.[1-9]{1}[0-9]{2}|0\\.[0-9]{2}[1-9]{1}|0\\.[0-9]{1}[1-9]{1}[0-9]{1}|[1-9]{1}[0-9]{0,11}(\\.[0-9]{3})?", true),
	TDEC_1204("0|0\\.[0-9]{4}|[1-9]{1}[0-9]{0,11}(\\.[0-9]{4})?", true),
	TDEC_1204Opc("0\\.[1-9]{1}[0-9]{3}|0\\.[0-9]{3}[1-9]{1}|0\\.[0-9]{2}[1-9]{1}[0-9]{1}|0\\.[0-9]{1}[1-9]{1}[0-9]{2}|[1-9]{1}[0-9]{0,11}(\\.[0-9]{4})?", true),
	TDEC_1302("0|0\\.[0-9]{2}|[1-9]{1}[0-9]{0,12}(\\.[0-9]{2})?", true),
	TDEC_1302Opc("0\\.[0-9]{1}[1-9]{1}|0\\.[1-9]{1}[0-9]{1}|[1-9]{1}[0-9]{0,12}(\\.[0-9]{2})?", true),
	NCM("[0-9]{2}|[0][1-9][0-9]{6}|[1-9][0-9]{7}"),
	EMAIL(".+@.+\\.[a-z]+"),
	CEP("^[0-9]{2}.[0-9]{3}-[0-9]{3}$|^[0-9]{5}-[0-9]{3}$|^[0-9]{8}$"),
	PLACA("[A-Z]{3}[0-9]{4}|[A-Z]{3}-[0-9]{4}"),
	OnlyNumbers("^[0-9]+$"),
	OnlyLetters("^[a-zA-Z]+$"),
	OnlyLettersNumbers("^[a-zA-Z0-9]+$");
	
	private final String regexExpression;
	private final boolean applyFormat;
	private final Format formatInstance = new Format();
	
	private RegexValidation(String regexExpression) {
		this.regexExpression = regexExpression;
		this.applyFormat = false;
	}
	
	private RegexValidation(String regexExpression, boolean applyFormat) {
		this.regexExpression = regexExpression;
		this.applyFormat = applyFormat;
	}
	
	public String expression() {
		return regexExpression;
	}
	
	public boolean evaluate(String value) {
		if (applyFormat) {
			value = formatInstance.format(value);
		}
		return !value.matches(regexExpression);
	}
	
	final class Format {
		private  String format(String value) {
			if (value != null) {
				value = value.replace(',', '|');
				value = value.replaceAll("[.]","");
				value = value.replace('|', '.');
			}	
			return value;
		}
	}
}