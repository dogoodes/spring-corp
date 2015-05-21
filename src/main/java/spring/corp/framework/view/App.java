package spring.corp.framework.view;

import spring.corp.framework.generator.Placa;

public class App {

	public static void main(String argv[]){
		System.out.println("Placa");
		System.out.println(Placa.get().matches(RegexValidation.PLACA.expression()));
		System.out.println("AB-1234".matches(RegexValidation.PLACA.expression()));
		System.out.println("ABC-123".matches(RegexValidation.PLACA.expression()));
		
		System.out.println();
		
		System.out.println("E-mail");
		System.out.println("teste@teste.com.br".matches(RegexValidation.EMAIL.expression()));
		System.out.println("testeteste.com.br".matches(RegexValidation.EMAIL.expression()));
		
		System.out.println();
		
		System.out.println("Moedas");
		System.out.println("100.00".matches(RegexValidation.TDEC_0302.expression()));
		System.out.println("10000000.000".matches(RegexValidation.TDEC_0803.expression()));
		System.out.println("10000000.0000".matches(RegexValidation.TDEC_0804.expression()));
		System.out.println("10000000000.0000".matches(RegexValidation.TDEC_1104.expression()));
		System.out.println("100000000000.000".matches(RegexValidation.TDEC_1203.expression()));
		System.out.println("100000000000.0000".matches(RegexValidation.TDEC_1204.expression()));
		System.out.println("1000000000000.00".matches(RegexValidation.TDEC_1302.expression()));
		//000.000.000.000,000 1203
		//000.000.000.000,0000 1204
		//1.000.000.000.000,00 1302  //20
		//12.345.678.0000 0804
	}
}