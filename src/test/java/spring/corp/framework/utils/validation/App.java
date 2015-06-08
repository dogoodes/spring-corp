package spring.corp.framework.utils.validation;

import spring.corp.framework.generator.Cnpj;
import spring.corp.framework.generator.Cpf;
import spring.corp.framework.generator.Placa;
import spring.corp.framework.generator.Renavam;
import spring.corp.framework.utils.IdentificationValidation;
import spring.corp.framework.utils.ValidationUtils;

public class App {

	public static void main(String[] args) {
		System.out.println("CPF");
		System.out.println(IdentificationValidation.cpfInstance.validaCPF(Cpf.getInstance().get(true)));
		System.out.println(IdentificationValidation.cpfInstance.validaCPF(Cpf.getInstance().get(false)));
		System.out.println(IdentificationValidation.cpfInstance.validaCPF("000.000.000-00"));
		System.out.println(ValidationUtils.cpfValid(Cpf.getInstance().get(true)));
		System.out.println(ValidationUtils.cpfValid(Cpf.getInstance().get(false)));
		System.out.println(ValidationUtils.cpfValid("000.000.000-00"));
		
		System.out.println();
		
		System.out.println("CNPJ");
		System.out.println(IdentificationValidation.cnpjInstance.validaCNPJ(Cnpj.getInstance().get(true)));
		System.out.println(IdentificationValidation.cnpjInstance.validaCNPJ(Cnpj.getInstance().get(false)));
		System.out.println(IdentificationValidation.cnpjInstance.validaCNPJ("00.000.000/0000-00"));
		System.out.println(ValidationUtils.cnpjValid(Cnpj.getInstance().get(true)));
		System.out.println(ValidationUtils.cnpjValid(Cnpj.getInstance().get(false)));
		System.out.println(ValidationUtils.cnpjValid("00.000.000/0000-00"));
		
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
		
		System.out.println();
		
		System.out.println("Placa");
		System.out.println(ValidationUtils.placaValid(Placa.get()));
		System.out.println(ValidationUtils.placaValid(Placa.get()));
		System.out.println(ValidationUtils.placaValid("ABCD-123"));
	}
}