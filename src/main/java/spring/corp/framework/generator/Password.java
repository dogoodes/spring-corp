package spring.corp.framework.generator;

public class Password {

	/**
	 * Gerador de senhas aleatório.
	 * @param maxChar int quantidade de caracteres na senha. 
	 * @param level int varia entre 1 e 5
	 * @return senha gerada.
	 */
	public static String get(int maxChar, int level) {
		
		//TODO: Ajustar
		//1 - apenas letras
		//2 - letra e numeros
		//3 - usar o metodo replaceChar()
		//4 - caracter especial a cada 3 caracteres
		//5 - caracter especial a cada 2 caractere
		//6 - somente caracteres especiais
		
		String[] c = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
                      "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z",
                      "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};

		String[] ce = {"`", "~", "!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "_", "-", "+", "=", "{", "}", "[", "]", "\"", "|", ":", ";", "\"", "'", "<", ">", ",", ".", "?", "/"};
       
        StringBuilder passowrd = new StringBuilder();
        for (int i = 0; i < maxChar; i++) {
        	int p = (int) (Math.random() * c.length);
        	int pe = (int) (Math.random() * ce.length);
        	
        	if (level == 1) {
                passowrd.append(c[p]);
        	} else if (level == 2) {
        		String l = c[p];
        		String newL = replaceChar(l);
        		passowrd.append(newL);
        	} else if (level == 3) {
                passowrd.append(c[p]);
                if ((i % 3 == 0) && (i < maxChar - 1)) {
                    passowrd.append(ce[pe]);
                    i++;
                }
        	} else if (level == 4) {
        		passowrd.append(c[p]);
        		if ((i % 2 == 0) && (i < maxChar - 1)) {
                    passowrd.append(ce[pe]);
                    i++;
                }
        	} else if (level == 5) {
                passowrd.append(ce[pe]);
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