package spring.corp.framework.utils;

import spring.corp.framework.view.RegexValidation;

public class ValidationUtils {

	/**
	 * Validação de códigos de renavam
	 * @param renavam (String) código de renavam para validação
	 * @return (boolean) true para código válido e false para código inválido
	 */
	public static boolean renavamValid(String renavam) {
		// Pegando como exemplo o renavam = 639884962

		// Completa com zeros a esquerda se for no padrao antigo de 9 digitos
		// renavam = 00639884962
		if (renavam.matches(RegexValidation.Renavam9digitos.expression())) {
			renavam = "00" + renavam;
		}

		// Valida se possui 11 digitos pos formatacao
		if (!renavam.matches(RegexValidation.Renavam11digitos.expression())) {
			return false;
		}

		// Remove o digito (11 posicao)
		// renavamSemDigito = 0063988496
		String renavamSemDigito = renavam.substring(0, 10);

		// Inverte os caracteres (reverso)
		// renavamReversoSemDigito = 69488936
		String renavamReversoSemDigito = new StringBuffer(renavamSemDigito).reverse().toString();

		int soma = 0;

		// Multiplica as strings reversas do renavam pelos numeros
		// multiplicadores
		// para apenas os primeiros 8 digitos de um total de 10
		// Exemplo: renavam reverso sem digito = 69488936
		// 6, 9, 4, 8, 8, 9, 3, 6
		// * * * * * * * *
		// 2, 3, 4, 5, 6, 7, 8, 9 (numeros multiplicadores - sempre os mesmos
		// [fixo])
		// 12 + 27 + 16 + 40 + 48 + 63 + 24 + 54
		// soma = 284
		for (int i = 0; i < 8; i++) {
			Integer algarismo = Integer.parseInt(renavamReversoSemDigito.substring(i, i + 1));
			Integer multiplicador = i + 2;
			soma += algarismo * multiplicador;
		}

		// Multiplica os dois ultimos digitos e soma
		soma += Character.getNumericValue(renavamReversoSemDigito.charAt(8)) * 2;
		soma += Character.getNumericValue(renavamReversoSemDigito.charAt(9)) * 3;

		// mod11 = 284 % 11 = 9 (resto da divisao por 11)
		int mod11 = soma % 11;

		// Faz-se a conta 11 (valor fixo) - mod11 = 11 - 9 = 2
		int ultimoDigitoCalculado = 11 - mod11;

		// ultimoDigito = Caso o valor calculado anteriormente seja 10 ou 11,
		// transformo ele em 0
		// caso contrario, eh o proprio numero
		ultimoDigitoCalculado = (ultimoDigitoCalculado >= 10 ? 0 : ultimoDigitoCalculado);

		// Pego o ultimo digito do renavam original (para confrontar com o
		// calculado)
		int digitoRealInformado = Integer.valueOf(renavam.substring(renavam.length() - 1, renavam.length()));

		// Comparo os digitos calculado e informado
		if (ultimoDigitoCalculado == digitoRealInformado) {
			return true;
		}
		return false;
	}

	/**
	 * Validação de emails
	 * @param email (String) email para validação
	 * @return (boolean) true para email válido e false para email inválido
	 */
	public static boolean emailValid(String email) {
		if (StringUtils.isBlank(email)) {
			return false;
		}
		// Valida se esta no formato correto
		if (!email.matches(RegexValidation.EMAIL.expression())) {
			return false;
		}
		if (email.indexOf(" ") != -1 || email.indexOf("..") != -1) {
			return false;
		}
		return true;
	}
	
	/**
	 * Validação de número de placas
	 * @param placa (String) número de placa para validação
	 * @return (boolean) true para número de placa válida e false para inválida
	 */
	public static boolean placaValid(String placa) {
		if (StringUtils.isBlank(placa)) {
			return false;
		}
		// Valida se esta no formato correto
		if (!placa.matches(RegexValidation.PLACA.expression())) {
			return false;
		}
		return true;
	}
	
	/**
	 * Validação de cpf
	 * @param placa (String) número do cpf para validação
	 * @return (boolean) true para número de cpf válido e false para inválido
	 * @see .spring.corp.framework.utilsIdentificationValidation.validaCPF(String)
	 */
	public static boolean cpfValid(String cpf) {
		return IdentificationValidation.cpfInstance.validaCPF(cpf);
	}
	
	/**
	 * Validação de cpf
	 * @param placa (String) número do cpf para validação
	 * @return (boolean) true para número de cpf válido e false para inválido
	 * @see .spring.corp.framework.utilsIdentificationValidation.validaCNPJ(String)
	 */
	public static boolean cnpjValid(String cnpj) {
		return IdentificationValidation.cnpjInstance.validaCNPJ(cnpj);
	}
	
	// TODO: Inserir validador de CEP
	// TODO: Inserir validador de Outros
}