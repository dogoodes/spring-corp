package spring.corp.framework.view;

public interface IComponentView<T> {

	/**
	 * Retorna o nome do componente 
	 * @return
	 */
	public String getName();

	/**
	 * Retorna a Label do componente
	 * @return
	 */
	public String getLabel();

	/**
	 * Retorna o valor do Componente
	 * @return
	 */
	public T getValue();

	/**
	 * Retorna o valor do Componente. O parametro silent quando verdadeiro
	 * retorna o valor mesmo que haja uma excecao, caso seja falso a excecao
	 * sera lancada
	 * 
	 * @param silent
	 *            Indica se deve retornar mesmo com erro
	 *            <ul>
	 *            <li>True - Para retornar sem erro</li>
	 *            <li>False - Para retornar a excecao caso exista</li>
	 *            </ul>
	 * @return O Valor do Component
	 * @exception Caso
	 *                silent seja false e exista alguma excecao a mesma sera
	 *                lancada
	 */
	public T getValue(boolean silent);
}