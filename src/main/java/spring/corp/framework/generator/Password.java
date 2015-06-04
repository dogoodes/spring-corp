package spring.corp.framework.generator;

public class Password {

	/**
	 * Gerador de senhas aleatórias
	 * @param maxChar (int) quantidade de caracteres na senha
	 * @param level (int) nível da senha (varia entre 1 e 7):
	 * <ol>
	 *   <li>apenas letras minúsculas</li>
	 *   <li>apenas letras minúsculas e maiúsculas</li>
	 *   <li>letra e numeros</li>
	 *   <li>subistitui a por @, e por &, i por 1, s por $, x por %, b por # e o por 0</li>
	 *   <li>caracter especial a cada 3 caracteres</li>
	 *   <li>caracter especial a cada 2 caractere</li>
	 *   <li>somente caracteres especiais</li>
	 * </ol>
	 * @return (String) senha aleatória
	 */
	public static String get(int maxChar, int level) {
		//chars
		String[] c = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
                      "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z",
                      "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};

		//chars special
		String[] cs = {"`", "~", "!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "_", "-", "+", "=", "{", "}", "[", "]", "\"", "|", ":", ";", "\"", "'", "<", ">", ",", ".", "?", "/"};
       
        StringBuilder passowrd = new StringBuilder();
        for (int i = 0; i < maxChar; i++) {
        	int p1 = (int) (Math.random() * 26);//level 1
        	int p2 = (int) (Math.random() * 52);//level 2
        	int p = (int) (Math.random() * c.length);//level 3 or +
        	int ps = (int) (Math.random() * cs.length);//level 3 or +
        	
        	if (level == 1) {
        		passowrd.append(c[p1]);
        	} else if (level == 2) {
        		passowrd.append(c[p2]);
        	} else if (level == 3) {
                passowrd.append(c[p]);
        	} else if (level == 4) {
        		String l = c[p];
        		String newL = replaceChar(l);
        		passowrd.append(newL);
        	} else if (level == 5) {
                passowrd.append(c[p]);
                if ((i % 3 == 0) && (i < maxChar - 1)) {
                    passowrd.append(cs[ps]);
                    i++;
                }
        	} else if (level == 6) {
        		passowrd.append(c[p]);
        		if ((i % 2 == 0) && (i < maxChar - 1)) {
                    passowrd.append(cs[ps]);
                    i++;
                }
        	} else if (level == 7) {
                passowrd.append(cs[ps]);
        	} else {
        		passowrd.append("passowrd");
        		break;
        	}
        }
        return passowrd.toString();
    }
	
	private static String replaceChar(String c) {
		if (c.equalsIgnoreCase("a")) {
			return "@";
		} else if (c.equalsIgnoreCase("e")) {
			return "&";
		} else if (c.equalsIgnoreCase("i")) {
			return "1";
		} else if (c.equalsIgnoreCase("s")) {
			return "$";
		} else if (c.equalsIgnoreCase("x")) {
			return "%";
		} else if (c.equalsIgnoreCase("b")) {
			return "#";
		} else if (c.equalsIgnoreCase("o")) {
			return "0";
		} else {
			return c;
		}
	}
}