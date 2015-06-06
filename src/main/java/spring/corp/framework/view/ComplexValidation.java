package spring.corp.framework.view;

import spring.corp.framework.exceptions.UserLinkException;

/**
 * Interface usada para validar os dados da tela, esta interface pode ser usada como closure ou ter uma implementacao caso seja necessario 
 * validacao com dependencia de campos. A ideia eh usar apenas UserLinkException para subir as excecoes.
 */
public interface ComplexValidation {

	/**
	 * Metodo responsavel pela validacao do campo
	 * @param name (String) nome do campo para ser incluido no UserLinkException
	 * @param value (String) valor do campo digitado pelo usuario
	 * @throws UserLinkException Caso a validacao nao seja satisfeita
	 */
	public void validate(String name, String value) throws UserLinkException;
}