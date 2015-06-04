package spring.corp.framework.generator;

import java.util.Random;

public class Word {

	/**
	 * Gerador de palavras aleatórias
	 * @param maxChar (int) quantidade de caracteres na palavra
	 * @return (String) palavra aleatória
	 */
	public static String get(int maxChar) {
    	Random rand = new Random();
    	char[] letras = "ABCDEFGHIJKLMNOPQRSTUVWXYZÁÉÍÓÚÃÕÂÊÎÔÛÀÈÌÒÙÇ".toCharArray();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < maxChar; i++) {
            int ch = rand.nextInt(letras.length);  
            sb.append(letras [ch]);  
        }      
        return sb.toString();      
    }
	
	/**
	 * Gerador de palavras aleatórias contendo apenas letras
	 * @param maxChar (int) quantidade de caracteres na palavra
	 * @return (String) palavra aleatória
	 */
	public static String getOnlyLetters(int maxChar) {
    	Random rand = new Random();
    	char[] letras = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < maxChar; i++) {
            int ch = rand.nextInt(letras.length);  
            sb.append(letras [ch]);  
        }      
        return sb.toString();      
    }
}