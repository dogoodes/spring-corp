package spring.corp.framework.generator;

public class App {

	public static void main(String[] args) {
		System.out.println("CPF");
		System.out.println(Cpf.getInstance().get(true));
		System.out.println(Cpf.getInstance().get(false));
		
		System.out.println();
		
		System.out.println("CNPJ");
		System.out.println(Cnpj.getInstance().get(true));
		System.out.println(Cnpj.getInstance().get(false));
		
		System.out.println();
		
		System.out.println("Renavam");
		System.out.println(Renavam.get());
		
		System.out.println();
		
		System.out.println("Senha");
		System.out.println(Password.get(10, 1));
		System.out.println(Password.get(10, 2));
		System.out.println(Password.get(10, 3));
		System.out.println(Password.get(10, 4));
		System.out.println(Password.get(10, 5));
		System.out.println(Password.get(10, 6));
		System.out.println(Password.get(10, 7));
		
		System.out.println();
		
		System.out.println("Palavra");
		System.out.println(Word.get(10));
		System.out.println(Word.getOnlyLetters(10));
		
		System.out.println();
		
		System.out.println("Placa");
		System.out.println(Placa.get());
	}
}