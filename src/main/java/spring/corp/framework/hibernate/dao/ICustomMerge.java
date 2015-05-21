package spring.corp.framework.hibernate.dao;

public interface ICustomMerge<T> {

	public void merge(T orm);
}