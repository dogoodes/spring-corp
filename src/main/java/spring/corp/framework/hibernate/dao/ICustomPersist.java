package spring.corp.framework.hibernate.dao;

public interface ICustomPersist<T> {

	public void persist(T orm);
}