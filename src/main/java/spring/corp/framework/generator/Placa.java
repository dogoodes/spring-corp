package spring.corp.framework.generator;

import java.util.Random;

public class Placa {

	/**
	 * Gerador de placas aleatórias
	 * @return (String) placa aleatória
	 */
	public static String get() {
		StringBuilder sb = new StringBuilder(Word.getOnlyLetters(3) + "-");
		Random r = new Random();
		for (int i = 0; i < 4; i++) {
			sb.append(r.nextInt(10));
		}
		return sb.toString();
	}
}