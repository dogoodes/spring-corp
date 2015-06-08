package spring.corp.framework.utils;

import java.util.InputMismatchException;

import spring.corp.framework.exceptions.UserLinkException;
import spring.corp.framework.i18n.ManagerMessage;
import spring.corp.framework.view.ComplexValidation;
import spring.corp.framework.view.RegexValidation;

public class IdentificacaoValidation implements ComplexValidation {

	public static IdentificacaoValidation instance = new IdentificacaoValidation();
	public static IdentificacaoValidation cnpjInstance = new IdentificacaoValidation(TYPE.CNPJ);
	public static IdentificacaoValidation cpfInstance = new IdentificacaoValidation(TYPE.CPF);
	private static enum TYPE {CNPJ, CPF};
	private TYPE type;
	
	public static IdentificacaoValidation getInstance() {
		return instance;
	}
	
	public static IdentificacaoValidation getCNPJInstance() {
		return cnpjInstance;
	}
	
	public static IdentificacaoValidation getCPFInstance() {
		return cpfInstance;
	}
	
	private IdentificacaoValidation(){}
	private IdentificacaoValidation(TYPE type) {
		this.type = type;
	}
	
	@Override
	public void validate(String name, String valor) throws UserLinkException {
		if (valor != null && !valor.equals("")) {
			if (type != null){
				if (type.equals(TYPE.CPF)) {
					validaCPF(name, valor);
				} else if (type.equals(TYPE.CNPJ)) {
					validaCNPJ(name, valor); 
				}
			} else {
				if (valor.length() <= 11) {
					validaCPF(name, valor);
				} else {
					validaCNPJ(name, valor);
				}
			}
		}
	}
	
	public void type(TYPE type) {
		this.type = type;
	}
	
	public boolean validaCPF(String CPF) {
		CPF = StringUtils.normalizeOnlyLettersNumbers(CPF);
		
		// considera-se erro CPF's formados por uma sequencia de numeros iguais
		if (CPF.equals("00000000000") || CPF.equals("11111111111") || CPF.equals("22222222222") || CPF.equals("33333333333") || CPF.equals("44444444444") || CPF.equals("55555555555") || CPF.equals("66666666666") || CPF.equals("77777777777") || CPF.equals("88888888888") || CPF.equals("99999999999") || (CPF.length() != 11)) {
			return (false);
		}

		char dig10, dig11;
		int sm, i, r, num, peso;

		// "try" - protege o codigo para eventuais erros de conversao de tipo (int)
		try {
			// Calculo do 1o. Digito Verificador
			sm = 0;
			peso = 10;
			for (i = 0; i < 9; i++) {
				// converte o i-esimo caractere do CPF em um numero:
				// por exemplo, transforma o caractere '0' no inteiro 0        
				// (48 eh a posicao de '0' na tabela ASCII)        
				num = (int) (CPF.charAt(i) - 48);
				sm = sm + (num * peso);
				peso = peso - 1;
			}

			r = 11 - (sm % 11);
			if ((r == 10) || (r == 11)) {
				dig10 = '0';
			} else {
				dig10 = (char) (r + 48); // converte no respectivo caractere numerico
			}

			// Calculo do 2o. Digito Verificador
			sm = 0;
			peso = 11;
			for (i = 0; i < 10; i++) {
				num = (int) (CPF.charAt(i) - 48);
				sm = sm + (num * peso);
				peso = peso - 1;
			}

			r = 11 - (sm % 11);
			if ((r == 10) || (r == 11)) {
				dig11 = '0';
			} else {
				dig11 = (char) (r + 48);
			}

			// Verifica se os digitos calculados conferem com os digitos informados.
			if ((dig10 == CPF.charAt(9)) && (dig11 == CPF.charAt(10))) {
				return (true);
			} else {
				return (false);
			}
		} catch (InputMismatchException erro) {
			return (false);
		}
	}

	private void validaCPF(String campo, String cpf) {
		boolean isOk = true;

        // valida se o cpf esta no formato correto
        if (!cpf.matches(RegexValidation.Cpf.expression()) && !cpf.matches(RegexValidation.CpfFormatado.expression())) { // Valida a mascara 99999999999 ou 999.999.999-99
        	if (cpf.matches(RegexValidation.OnlyNumbers.expression()) && cpf.length() <= 11) { // Valida se existe somentes numeros
        		int length = cpf.length();
    			int diferenca = 11 - length;
    			
    			for (int i = 0; i < diferenca; i++) {
    				cpf = "0" + cpf;
    			}
        	} else {
        		isOk = false;
        	}
        }
        	
        if (!isOk) {	
        	String message = ManagerMessage.getMessage("framework.utils.invalid.identification.format", "CPF");
            throw new UserLinkException(campo, message);
        }

        // valida se o cnpj eh um cnpj invalido
        if (cpf.equals("00000000000") || cpf.equals("11111111111") || cpf.equals("22222222222") || cpf.equals("33333333333") || cpf.equals("44444444444") || cpf.equals("55555555555") || cpf.equals("66666666666") || cpf.equals("77777777777") || cpf.equals("88888888888") || cpf.equals("99999999999")) {
            isOk = false;
        }

        // Se nao houver erros, continuam os calculos
        if (isOk) {
            int[] digitos = new int[11];
            int[] valoresCalculo1 = new int[] {10, 9, 8, 7, 6, 5, 4, 3, 2};
            int[] valoresCalculo2 = new int[] {11, 10, 9, 8, 7, 6, 5, 4, 3, 2};

            int pos = 0;
            // Passa os dÌgitos a um array
            for (int i = 0 ; i < cpf.length() ; i++) {
                if (Character.isDigit(cpf.charAt(i))) {
                    digitos[pos] = Integer.parseInt(cpf.substring(i, i + 1));
                    pos++;
                }
            }

            int sum = 0;
            // Calculo do primeiro digito de controle
            for (int i = 0 ; i < valoresCalculo1.length ; i++) {
                sum = sum + (digitos[i] * valoresCalculo1[i]);
            }
            int digito1 = sum % 11;
            if (digito1 < 2) {
                digito1 = 0;
            } else {
                digito1 = 11 - digito1;
            }

            sum = 0;
            // Calculo do segundo digito de controle
            for (int i = 0 ; i < valoresCalculo2.length ; i++) {
                sum = sum + (digitos[i] * valoresCalculo2[i]);
            }
            int digito2 = sum % 11;
            if (digito2 < 2) {
                digito2 = 0;
            } else {
                digito2 = 11 - digito2;
            }
            // valida os digitos do cpf com os digitos de controle
            if (digitos[9] != digito1 || digitos[10] != digito2) {
            	isOk = false;
            }
        }
        if (!isOk) {
        	String message = ManagerMessage.getMessage("framework.utils.invalid.identification.format", "CPF");
            throw new UserLinkException(campo, message);
        }
	}
	
	public boolean validaCNPJ(String CNPJ) {
		CNPJ = StringUtils.normalizeOnlyLettersNumbers(CNPJ);
		
		// considera-se erro CNPJ's formados por uma sequencia de numeros iguais
		if (CNPJ.equals("00000000000000") || CNPJ.equals("11111111111111") || CNPJ.equals("22222222222222") || CNPJ.equals("33333333333333") || CNPJ.equals("44444444444444") || CNPJ.equals("55555555555555") || CNPJ.equals("66666666666666") || CNPJ.equals("77777777777777") || CNPJ.equals("88888888888888") || CNPJ.equals("99999999999999") || (CNPJ.length() != 14)) {
			return (false);
		}

		char dig13, dig14;
		int sm, i, r, num, peso;

		// "try" - protege o código para eventuais erros de conversao de tipo (int)
		try {
			// Calculo do 1o. Digito Verificador
			sm = 0;
			peso = 2;
			for (i = 11; i >= 0; i--) {
				// converte o i-ésimo caractere do CNPJ em um número:
				// por exemplo, transforma o caractere '0' no inteiro 0
				// (48 eh a posição de '0' na tabela ASCII)
				num = (int) (CNPJ.charAt(i) - 48);
				sm = sm + (num * peso);
				peso = peso + 1;
				if (peso == 10) {
					peso = 2;
				}
			}

			r = sm % 11;
			if ((r == 0) || (r == 1)) {
				dig13 = '0';
			} else {
				dig13 = (char) ((11 - r) + 48);
			}

			// Calculo do 2o. Digito Verificador
			sm = 0;
			peso = 2;
			for (i = 12; i >= 0; i--) {
				num = (int) (CNPJ.charAt(i) - 48);
				sm = sm + (num * peso);
				peso = peso + 1;
				if (peso == 10) {
					peso = 2;
				}
			}

			r = sm % 11;
			if ((r == 0) || (r == 1)) {
				dig14 = '0';
			} else {
				dig14 = (char) ((11 - r) + 48);
			}

			// Verifica se os dígitos calculados conferem com os dígitos informados.
			if ((dig13 == CNPJ.charAt(12)) && (dig14 == CNPJ.charAt(13))) {
				return (true);
			} else {
				return (false);
			}
		} catch (InputMismatchException erro) {
			return (false);
		}
	}

	private void validaCNPJ(String campo, String cnpj) {
		boolean isOk = false;

        // valida se o cpnj esta num formato correto
		if (cnpj.matches(RegexValidation.Cnpj.expression())) { //Valida se coincedir com a mascara 99999999999999
			isOk = true;
		} else if (cnpj.matches(RegexValidation.CnpjFormatadoComum.expression())) { // Valida se coincedir com a mascara 99.999.999/9999-99
			isOk = true;
		} else if (cnpj.matches(RegexValidation.CnpjFormatadoIncomum.expression())) { // Valida se coincedir com a mascara 99.999.999/9999-9
			cnpj = cnpj.substring(0,16) + "0" + cnpj.substring(16); 
			isOk = true;
		} else if (cnpj.matches(RegexValidation.OnlyNumbers.expression()) && cnpj.length() <= 14) { // Valida se existe somentes numeros
			int length = cnpj.length();
			int diferenca = 14 - length;

			for (int i = 0; i < diferenca; i++) {
				cnpj = "0" + cnpj;
			}
			isOk = true;
		} 
        
        if (!isOk) {
        	String message = ManagerMessage.getMessage("framework.utils.invalid.identification.format", "CNPJ");
            throw new UserLinkException(campo, message);
        }
        
        if (isOk) {
            int[] digitos = new int[14];
            int[] valoresCalculo1 = new int[] {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
            int[] valoresCalculo2 = new int[] {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

            int pos = 0;
            // Passa os digitos a um array
            for (int i = 0 ; i < cnpj.length() ; i++) {
                if (Character.isDigit(cnpj.charAt(i))) {
                    digitos[pos] = Integer.parseInt(cnpj.substring(i, i + 1));
                    pos++;
                }
            }

            int sum = 0;
            // Calculo do primeiro digito de controle
            for (int i = 0 ; i < valoresCalculo1.length ; i++) {
                sum = sum + (digitos[i] * valoresCalculo1[i]);
            }
            int digito1 = sum % 11;
            if (digito1 < 2) {
                digito1 = 0;
            } else {
                digito1 = 11 - digito1;
            }

            sum = 0;
            // Calculo do segundo digito de controle
            for (int i = 0 ; i < valoresCalculo2.length ; i++) {
                sum = sum + (digitos[i] * valoresCalculo2[i]);
            }
            int digito2 = sum % 11;
            if (digito2 < 2) {
                digito2 = 0;
            } else {
                digito2 = 11 - digito2;
            }

            // valida os digitos do cpnj com os digitos de controle
            if (digitos[12] != digito1 || digitos[13] != digito2) {
                isOk = false;
            }
        }
        if (!isOk) {
        	String message = ManagerMessage.getMessage("framework.utils.invalid.identification.format", "CNPJ");
        	throw new UserLinkException(campo, message);
        }
	}
}