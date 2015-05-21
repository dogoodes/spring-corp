package spring.corp.framework.hibernate.dao;

import java.util.Collection;

public interface IGenericDAO<T> {

	public void inserir(T orm);
	public T atualizar(T orm);
	public void excluir(T orm);
	public <S> void  alterarRelacao(Collection<S> oldOrm, Collection<S> newOrm);
	public <S> void  alterarRelacao(Collection<S> oldOrm, Collection<S> newOrm, ICustomMerge<S> merge, ICustomPersist<S> persist);
}