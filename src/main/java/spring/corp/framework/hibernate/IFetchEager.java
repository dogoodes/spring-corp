package spring.corp.framework.hibernate;

public interface IFetchEager<T> {

	public void fetch(T orm);
}