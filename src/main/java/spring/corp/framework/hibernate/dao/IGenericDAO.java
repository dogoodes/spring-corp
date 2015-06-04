package spring.corp.framework.hibernate.dao;

import java.util.Collection;

public interface IGenericDAO<T> {

	public void insert(T orm);
	public T update(T orm);
	public void remove(T orm);
	public <S> void  update(Collection<S> oldOrm, Collection<S> newOrm);
	public <S> void  update(Collection<S> oldOrm, Collection<S> newOrm, ICustomMerge<S> merge, ICustomPersist<S> persist);
}