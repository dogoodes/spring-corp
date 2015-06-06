package spring.corp.framework.utils;

import java.util.HashMap;
import java.util.Map;

import spring.corp.framework.exceptions.ConverterException;
import spring.corp.framework.view.RegexValidation;

import com.ibm.icu.text.Normalizer;

public class StringUtils implements IConverter<String> {

	private static final int PAD_LIMIT = 8192;
	
	private static StringUtils instance = new StringUtils();

	public static StringUtils getInstance() {
		return instance;
	}
	
	@Override
	public String convert(String valor) throws ConverterException {
		return valor;
	}

	/**
	 * Verificar se a String esta vazia<br />
	 * Exemplos:
	 * <ul>
	 * 	 <li><code>(length() == 0) == true</code></li>
	 * 	 <li><code>" " == false</code></li>
	 *   <li><code>null == true</code></li>
	 * </ul>
	 * @param value (String) valor para verificação
	 * @return (boolean) true para String vazia e false caso contrário
	 */
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }
    
    /**
	 * Verificar se a String esta vazia<br />
	 * Exemplos:
	 * <ul>
	 * 	 <li><code>" " == false</code></li>
	 * 	 <li><code>"" == true</code></li>
	 *   <li><code>"null" == true</code></li>
	 *   <li><code>null == true</code></li>
	 * </ul>
	 * @param value (String) valor para verificação
	 * @return (boolean) true para String vazia e false caso contrário
	 */
    public static boolean isBlank(String value) {
		return (value == null || value.equals("") || value.equals("null") || value.trim().equals(""));
	}
    
    /**
	 * Verifica se a String possui apenas letras
	 * @param str (String) string para verificação
	 * @return (boolean) true caso a String possua apenas letras e false caso contrário
	 */
    public static boolean isOnlyLetters(String str) {
		boolean ret = false;
		if (!StringUtils.isBlank(str)) {
			ret = str.matches(RegexValidation.OnlyLetters.expression());
		}
		return ret;
	}
    
    /**
	 * Verifica se a String possui apenas letras e números
	 * @param str (String) string para verificação
	 * @return (boolean) true caso a String possua apenas letras e números e false caso contrário
	 */
    public static boolean isOnlyLettersNumbers(String str) {
		boolean ret = false;
		if (!StringUtils.isBlank(str)) {
			ret = str.matches(RegexValidation.OnlyLettersNumbers.expression());
		}
		return ret;
	}
	
	public static String leftPad(String str, String character, int size) {
		if (str != null) {
			int delta = size - str.length();
			for (int i = 0; i < delta; i++) {
				str = character + str;
			}
		}
		return str;
	}
	
    public static String rightPad(String str, int size) {
        return rightPad(str, size, ' ');
    }
    
    public static String rightPad(String str, int size, char padChar) {
        if (str == null) {
            return null;
        }
        int pads = size - str.length();
        if (pads <= 0) {
            return str; // returns original String when possible
        }
        if (pads > PAD_LIMIT) {
            return rightPad(str, size, String.valueOf(padChar));
        }
        return str.concat(padding(pads, padChar));
    }
    
    public static String rightPad(String str, int size, String padStr) {
        if (str == null) {
            return null;
        }
        if (isEmpty(padStr)) {
            padStr = " ";
        }
        int padLen = padStr.length();
        int strLen = str.length();
        int pads = size - strLen;
        if (pads <= 0) {
            return str; // returns original String when possible
        }
        if (padLen == 1 && pads <= PAD_LIMIT) {
            return rightPad(str, size, padStr.charAt(0));
        }

        if (pads == padLen) {
            return str.concat(padStr);
        } else if (pads < padLen) {
            return str.concat(padStr.substring(0, pads));
        } else {
            char[] padding = new char[pads];
            char[] padChars = padStr.toCharArray();
            for (int i = 0; i < pads; i++) {
                padding[i] = padChars[i % padLen];
            }
            return str.concat(new String(padding));
        }
    }
    
    private static String padding(int repeat, char padChar) throws IndexOutOfBoundsException {
        if (repeat < 0) {
            throw new IndexOutOfBoundsException("Cannot pad a negative amount: " + repeat);
        }
        final char[] buf = new char[repeat];
        for (int i = 0; i < buf.length; i++) {
            buf[i] = padChar;
        }
        return new String(buf);
    }
    
    public static String normalizeToMainframe(String str, int size) {
        if (str != null) {
            str = normalize(str);
            str = str.toUpperCase();
            str = StringUtils.rightPad(str, size, " ");
        } else {
            str = StringUtils.rightPad(" ", size);
        }
        return str;
    }
    
    public static String normalizeOnlyLettersNumbers(String str) {
    	if (!StringUtils.isBlank(str)) {
    		return str.replaceAll("[^\\p{L}\\p{Nd}]+", "");
    	} else {
    		return "";
    	}
    }
    
    public static String normalize(String str) {
        if (str != null) {
            String decomposed = Normalizer.normalize(str, Normalizer.NFD);
            String accentsGone = decomposed.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
            accentsGone = accentsGone.replaceAll("[&]", "");
            accentsGone = accentsGone.replaceAll("[º|°|ª]", "");
            accentsGone = accentsGone.replaceAll("[<]", "");
            accentsGone = accentsGone.replaceAll("[>]", "");
            accentsGone = accentsGone.replaceAll("[\"]", "");
            accentsGone = accentsGone.replaceAll("[/]", "");
            accentsGone = accentsGone.replaceAll("[']", "");
            accentsGone = accentsGone.replaceAll("[`]", "");
            accentsGone = accentsGone.replaceAll("[-]", "");
            accentsGone = accentsGone.replaceAll("[$]", "");
            str = accentsGone;
            str = str.trim();
        }
        return str;
    }
    
    public static Map<String, String> makeOptions(String value) {
    	String[] options = value.split("(;)");
    	Map<String, String> mapOptions = new HashMap<String, String>();
		for (String option:options) {
			int posQuebra = option.indexOf(":");
			if (posQuebra > 0) {
				String chave = option.substring(0, posQuebra);
				String valor = option.substring(posQuebra+1);
				mapOptions.put(chave.trim(), valor.trim());
			}
		}
		return mapOptions;
    }
}