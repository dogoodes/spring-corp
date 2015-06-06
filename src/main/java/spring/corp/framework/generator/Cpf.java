package spring.corp.framework.generator;

public class Cpf {

	private static Cpf instance = new Cpf();
	
	private Cpf() {}
	public static Cpf getInstance() {
		return instance;
	}
	
	private int randomiza(int n) {
		return (int) (Math.random() * n);
	}

	private int mod(int dividendo, int divisor) {
		return (int) Math.round(dividendo - (Math.floor(dividendo / divisor) * divisor));
	}
	
	/**
	 * Gerar CPF randômico
	 * @param withPoints (boolean) true para retornar valor com pontuação e false para que não contenha pontuação
	 * @return (String) CPF randômico
	 */
	public String get(boolean withPoints) {
		int n = 9;
		int n1 = randomiza(n);
		int n2 = randomiza(n);
		int n3 = randomiza(n);
		int n4 = randomiza(n);
		int n5 = randomiza(n);
		int n6 = randomiza(n);
		int n7 = randomiza(n);
		int n8 = randomiza(n);
		int n9 = randomiza(n);
		int d1 = n9 * 2 + n8 * 3 + n7 * 4 + n6 * 5 + n5 * 6 + n4 * 7 + n3 * 8 + n2 * 9 + n1 * 10;

		d1 = 11 - (mod(d1, 11));

		if (d1 >= 10) {
			d1 = 0;
		}

		int d2 = d1 * 2 + n9 * 3 + n8 * 4 + n7 * 5 + n6 * 6 + n5 * 7 + n4 * 8 + n3 * 9 + n2 * 10 + n1 * 11;

		d2 = 11 - (mod(d2, 11));

		if (d2 >= 10) {
			d2 = 0;
		}
		
		String ret = null;

		if (withPoints) {
			ret = "" + n1 + n2 + n3 + '.' + n4 + n5 + n6 + '.' + n7 + n8 + n9 + '-' + d1 + d2;
		} else {
			ret = "" + n1 + n2 + n3 + n4 + n5 + n6 + n7 + n8 + n9 + d1 + d2;
		}

		return ret;
	}
}