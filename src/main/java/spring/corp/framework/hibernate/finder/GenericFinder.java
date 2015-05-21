package spring.corp.framework.hibernate.finder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import spring.corp.framework.exceptions.CriticalUserException;
import spring.corp.framework.exceptions.ObjectNotFoundException;
import spring.corp.framework.exceptions.UserException;
import spring.corp.framework.hibernate.FetchEagerUtils;
import spring.corp.framework.hibernate.IFetchEager;
import spring.corp.framework.hibernate.dao.GenericDAO;
import spring.corp.framework.i18n.GerenciadorMensagem;

public abstract class GenericFinder<T> implements IGenericFinder<T> {

	private EntityManager em;
	
	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
	
	public EntityManager getEntityManager() {
		return em;
	}

	private static final Map<String, String[]> expressions = new HashMap<String, String[]>();
	static {
		expressions.put("Nome:", new String[] {"nome", " and t.nome like :nome"});
		expressions.put("Codigo:", new String[] {"codigo", " and t.codigo like :codigo"});
		expressions.put("Identificacao:", new String[] {"identificacao", " and t.identificacao like :identificacao"});
		expressions.put("Numero:", new String[] {"numero", " and t.numero like :numero"});
		expressions.put("Descricao:", new String[] {"descricao", " and t.descricao like :descricao"});
	}
	
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public T buscarById(Class classe, Long id, IFetchEager<T>... eagers) throws ObjectNotFoundException {
		Query query = getEntityManager().createQuery("select t from " + classe.getName() + " where t.id = :id");
		query.setParameter("id", id);
		T t = (T) query.getSingleResult();
		if (t == null) {
			String mensagem = GerenciadorMensagem.getMessage("t.finder.t.nao.encontrada");
			throw new ObjectNotFoundException(GenericDAO.class, mensagem);
		}
		FetchEagerUtils.runFetch(t, eagers);
		return t;
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<T> buscarTodos(Class classe, IFetchEager<T>... eagers) {
		Query query = getEntityManager().createQuery("select t from " + classe.getName() + " t");
		List<T> t = (List<T>) query.getResultList();
		FetchEagerUtils.runFetch(t, eagers);
		return t;
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public T buscarByAtributo1(Class classe, String atributo1name, Object atributo1value, IFetchEager<T>... eagers) throws ObjectNotFoundException {
		try {
			Query query = getEntityManager().createQuery("select t from " + classe.getName() + " where t." + atributo1name + " = :" + atributo1name);
			query.setParameter(atributo1name, atributo1value);
			T t = (T) query.getSingleResult();
			FetchEagerUtils.runFetch(t, eagers);
			return t;
		} catch (EntityNotFoundException e) {
			String message = GerenciadorMensagem.getMessage(("t.finder.t.nao.encontrada.pelo." + atributo1value), atributo1value);
			throw new ObjectNotFoundException(GenericDAO.class, message);
		} catch (NoResultException e) {
			String message = GerenciadorMensagem.getMessage(("t.finder.t.nao.encontrada.pelo." + atributo1value), atributo1value);
			throw new ObjectNotFoundException(GenericDAO.class, message);
		} catch (NonUniqueResultException e) {
			String message = GerenciadorMensagem.getMessage(("t.finder.t.nao.encontrada.pelo." + atributo1value), atributo1value);
			throw new CriticalUserException(GenericDAO.class, message);
		}
	}
	
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object[] buscarByPaginacao(Class classe, int first, int max, IFetchEager<T>... eagers) throws UserException {
		Query count = getEntityManager().createQuery("select count(t) from " + classe.getName());
		Number numberOfRows = (Number) count.getSingleResult();

		Query query = getEntityManager().createQuery("select t from " + classe.getName());
		query.setFirstResult(first);
		query.setMaxResults(max);
		List<T> t = (List<T>) query.getResultList();
		FetchEagerUtils.runFetch(t, eagers);
		return new Object[] {numberOfRows, t};
	}
	
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object[] buscarByPaginacao(Class classe, int first, int max, String expression, IFetchEager<T>... eagers) throws UserException {
		Query count = QLExpression.applyExpression(getEntityManager(), expressions, ("select count(t) from " + classe.getName()), expression);
		Number numberOfRows = (Number) count.getSingleResult();

		Query query = QLExpression.applyExpression(getEntityManager(), expressions, ("select t from " + classe.getName()), expression);
		query.setFirstResult(first);
		query.setMaxResults(max);
		List<T> t = (List<T>) query.getResultList();
		FetchEagerUtils.runFetch(t, eagers);
		return new Object[] {numberOfRows, t};
	}
}