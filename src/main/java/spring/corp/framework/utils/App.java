package spring.corp.framework.utils;

import spring.corp.framework.generator.Cnpj;
import spring.corp.framework.generator.Cpf;
import spring.corp.framework.generator.Renavam;

public class App {

	public static void main(String[] args) {
		System.out.println("CPF");
		System.out.println(IdentificacaoValidation.getInstance().validaCPF(Cpf.getInstance().get(true)));
		System.out.println(IdentificacaoValidation.getInstance().validaCPF(Cpf.getInstance().get(false)));
		System.out.println(IdentificacaoValidation.getInstance().validaCPF("000.000.000-00"));
		
		System.out.println();
		
		System.out.println("CNPJ");
		System.out.println(IdentificacaoValidation.getInstance().validaCNPJ(Cnpj.getInstance().get(true)));
		System.out.println(IdentificacaoValidation.getInstance().validaCNPJ(Cnpj.getInstance().get(false)));
		System.out.println(IdentificacaoValidation.getInstance().validaCPF("00.000.000/0000-00"));
		
		System.out.println();
		
		System.out.println("Renavam");
		System.out.println(ValidationUtils.validarRenavam(Renavam.get()));
		System.out.println(ValidationUtils.validarRenavam("0123456789"));
	}
}