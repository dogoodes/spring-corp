package spring.corp.framework.hibernate.dao;

import java.lang.reflect.ParameterizedType;
import java.util.Collection;

import javax.persistence.EntityManager;

public abstract class GenericDAO<T> implements IGenericDAO<T> {

	@SuppressWarnings("unused")
	private final Class<T> persistentClass;
	
	@SuppressWarnings("unchecked")
	public GenericDAO() {
		this.persistentClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}
	
	@Override
	public void insert(T orm) {
		getEntityManager().persist(orm);
	}

	@Override
	public T update(T orm) {
		return (T)getEntityManager().merge(orm);
	}

	@Override
	public void remove(T orm) {
		getEntityManager().remove(orm);	
	}
	
	@Override
	public <S> void  update(Collection<S> oldOrm, Collection<S> newOrm) {
		for (S newItem : newOrm) {
			if (oldOrm.contains(newItem)) {
				getEntityManager().merge(newItem);
			} else {
				getEntityManager().persist(newItem);
			}
			oldOrm.remove(newItem);
		}
		for (S oldItem : oldOrm) {
			getEntityManager().remove(oldItem);
		}
	}
	
	@Override
	public <S> void  update(Collection<S> oldOrm, Collection<S> newOrm, ICustomMerge<S> customMerge, ICustomPersist<S> customPersist) {
		for (S newItem : newOrm) {
			if (oldOrm.contains(newItem)) {
				if (customMerge == null) {
					getEntityManager().merge(newItem);
				} else {
					customMerge.merge(newItem);
				}
			} else {
				if (customPersist == null) {
					getEntityManager().persist(newItem);
				} else {
					customPersist.persist(newItem);
				}
			}
			oldOrm.remove(newItem);
		}
		for (S oldItem : oldOrm) {
			getEntityManager().remove(oldItem);
		}
	}
	
	protected abstract EntityManager getEntityManager();
}