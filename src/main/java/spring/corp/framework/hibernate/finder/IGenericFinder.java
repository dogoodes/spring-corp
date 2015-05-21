package spring.corp.framework.hibernate.finder;

import java.util.List;

import spring.corp.framework.exceptions.ObjectNotFoundException;
import spring.corp.framework.exceptions.UserException;
import spring.corp.framework.hibernate.IFetchEager;

public interface IGenericFinder<T> {
	
	@SuppressWarnings("rawtypes")
	public T buscarById(Class classe, Long id, IFetchEager<T>... eagers) throws ObjectNotFoundException;
	@SuppressWarnings("rawtypes")
	public List<T> buscarTodos(Class classe, IFetchEager<T>... eagers);
	@SuppressWarnings("rawtypes")
	public T buscarByAtributo1(Class classe, String atributo1name, Object atributo1value, IFetchEager<T>... eagers) throws ObjectNotFoundException;
	@SuppressWarnings("rawtypes")
	public Object[] buscarByPaginacao(Class classe, int first, int max, String expression, IFetchEager<T>... eagers) throws UserException;
	@SuppressWarnings("rawtypes")
	public Object[] buscarByPaginacao(Class classe, int first, int max, IFetchEager<T>... eagers) throws UserException;
}