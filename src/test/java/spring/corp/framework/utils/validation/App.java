package spring.corp.framework.utils.validation;

import spring.corp.framework.generator.Cnpj;
import spring.corp.framework.generator.Cpf;
import spring.corp.framework.generator.Renavam;
import spring.corp.framework.utils.IdentificacaoValidation;
import spring.corp.framework.utils.ValidationUtils;

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
		System.out.println(ValidationUtils.renavamValid(Renavam.get()));
		System.out.println(ValidationUtils.renavamValid("0123456789"));
		
		System.out.println();
		
		System.out.println("Email");
		System.out.println(ValidationUtils.emailValid("alberto.cerqueira1990@gmail.com"));
		System.out.println(ValidationUtils.emailValid("alberto.cerqueira1990gmail.com"));
		System.out.println(ValidationUtils.emailValid("alberto.cerqueira1990@"));
		System.out.println(ValidationUtils.emailValid("alberto.cerqueira1990@gmail"));
	}
}