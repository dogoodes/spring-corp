package spring.corp.framework.hibernate.finder;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import spring.corp.framework.log.ManagerLog;
import spring.corp.framework.utils.StringUtils;

public final class QLExpression {

	public static Query applyExpression(EntityManager em, Map<String, String[]> expressions, String originalQuery, String expressao) {
		Query query = null;
		if (isBlankExpression(expressao)) {
			query = em.createQuery(originalQuery);
		} else {
			int corte = expressao.indexOf(":");
			String realExpressao = expressao.substring(0, corte+1);
			String valor  = expressao.substring(corte+1);
			String[] jpqlExpression = expressions.get(realExpressao);
			String jpqlQuery = originalQuery + jpqlExpression[1];
			query = em.createQuery(jpqlQuery);
			boolean temCondicaoLike = jpqlQuery.contains(" like ");
			if (temCondicaoLike) {
				query.setParameter(jpqlExpression[0], "%" + valor.trim() + "%");
			} else {
				query.setParameter(jpqlExpression[0], valor.trim());
			}
		}
		return query;
	}
	
	public static Query applyExpression(EntityManager em, String originalQuery, Map<String, Object[]> filters, String orderBy) {
		StringBuilder queryBuilder = new StringBuilder(originalQuery);
		if (filters != null && !filters.isEmpty()) {
			int ins = 0;
			for (String filter : filters.keySet()) {
				if (filter.startsWith("having") || filter.startsWith("group by")) {
					//function usadas na query, por isso nao sao vistas como condicao da busca.
					//Estas duas condicoes devem ser insediras primeiro no filter para chegar nesse laco por ultimo.
					queryBuilder.append(" " + filter);
				} else {
					queryBuilder.append(" and ");
					if (filter.startsWith("IN")){
						int inPos = filter.indexOf(")");
						String in = filter.substring(3, inPos+1);
						in = ", IN(t." + in + " r" + ins + " ";
						filter = "r" + ins + "." + filter.substring(inPos + 1);
						ins++;
						int wherePos = queryBuilder.indexOf("where");
						String qry = queryBuilder.substring(0, wherePos);
						queryBuilder = new StringBuilder(qry + in + queryBuilder.substring(wherePos));
						queryBuilder.append(filter);
					}else if (filter.startsWith("(") || filter.startsWith("?")){
						//por enquanto nada, mas temos casos onde a query precisa de or por exemplo ((x == true) or (y == true)) nesses casos
						//nao queremos um t. antes do parentes, em contrapartida espera-se que o cliente da API use t.x e t.y
						queryBuilder.append(filter);
					}else{
						queryBuilder.append("t.");
						queryBuilder.append(filter);
					}
				}	
			}
		}
		if (orderBy != null) {
			if (orderBy.startsWith("(")) {
				//por enquanto nada, mas temos casos onde a query precisa de outra variavel na query nesses casos
				//nao queremos um t. antes do parentes
				queryBuilder.append(" order by " + orderBy);
			} else {
				queryBuilder.append(" order by t." + orderBy);
			}
		}
		
		Query query = em.createQuery(queryBuilder.toString());
		if (filters != null && !filters.isEmpty()) {
			int i = 1;
			for (Object[] parameters : filters.values()) {
				for (int x = 0, s = parameters.length ; x < s ; x++) {
					query.setParameter(i, parameters[x]);
					i++;
				}
			}
		}
		ManagerLog.debug(QLExpression.class, query.toString());
		return query;
	}
	
	/**
	 * Recebe um map, sendo que para cada posicao do objeto existe um array (Object[]) com a realString da query e um outro map com filters
	 * @param em
	 * @param dadosFilters Map de Object[] por Integer
	 * 		@param Integer posição do Object[]
	 * 		@param Object[] array de String e Map<String, Object[]>
	 * 			@param Word real query da busca, mapeada nas claseses SQL.java
	 * 			@param Map<String, Object[]> filters de condições da busca
	 * @param filterPai Map de Object[] por String, e o filters do select pai
	 * @param orderBy
	 * @return javax.persistence.Query 
	 */
	@SuppressWarnings("unchecked")
	public static Query applyExpression(EntityManager em, Map<Integer, Object[]> dadosFilters , Map<String, Object[]> filterPai, String orderBy) {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("Select * from(");
		for (int x = 0, y = dadosFilters.size() ; x < y ; x++) {
			Object[] dado = dadosFilters.get(x);
			String queryString = (String) dado[0];
			queryString = "(" + queryString;
			StringBuilder select = new StringBuilder(queryString);
			Map<String, Object[]> filters = new HashMap<String, Object[]>();
			filters = (Map<String, Object[]>) dado[1];
			
			if (filters != null && !filters.isEmpty()) {
				int ins = 0;
				for (String filter : filters.keySet()) {
					if (filter.startsWith("having") || filter.startsWith("group by")) {
						//function usadas na query, por isso nao sao vistas como condicao da busca.
						//Estas duas condicoes devem ser insediras primeiro no filter para chegar nesse
						//laco por ultimo.
						select.append(" " + filter);
					} else {
						select.append(" and ");
						if (filter.startsWith("IN")) {
							int inPos = filter.indexOf(")");
							String in = filter.substring(3, inPos+1);
							in = ", IN(t." + in + " r" + ins + " ";
							filter = "r" + ins + "." + filter.substring(inPos + 1);
							ins++;
							int wherePos = select.indexOf("where");
							String qry = select.substring(0, wherePos);
							select = new StringBuilder(qry + in + select.substring(wherePos));
							select.append(filter);
						} else {
							select.append(filter);
						}
					}	
				}
			}
			select.append(")");
			if (dadosFilters.size() != (x+1)) {
				select.append(" union ");
			}
			queryBuilder.append(select.toString());
		}
		
		if (orderBy != null) {
			queryBuilder.append(" order by " + orderBy);
		}
	
		queryBuilder.append(") as selectPai");
		if (filterPai != null && !filterPai.isEmpty()) {
			queryBuilder.append(" where ");
			int ins = 0;
			int cont = 0;
			for (String filter : filterPai.keySet()) {
				if (filter.startsWith("having") || filter.startsWith("group by")) {
					//function usadas na query, por isso nao sao vistas como condicao da busca.
					//Estas duas condicoes devem ser insediras primeiro no filter para chegar nesse laco por ultimo.
					queryBuilder.append(" " + filter);
				} else {
					if (cont > 0) {
						queryBuilder.append(" and ");
					}
					if (filter.startsWith("IN")) {
						int inPos = filter.indexOf(")");
						String in = filter.substring(3, inPos+1);
						in = ", IN(" + in + " r" + ins + " ";
						filter = "r" + ins + "." + filter.substring(inPos + 1);
						ins++;
						int wherePos = queryBuilder.indexOf("where");
						String qry = queryBuilder.substring(0, wherePos);
						queryBuilder = new StringBuilder(qry + in + queryBuilder.substring(wherePos));
						queryBuilder.append(filter);
					} else {
						queryBuilder.append(filter);
					}
					cont++;
				}	
			}
		}
		
		Query query = em.createNativeQuery(queryBuilder.toString());
		int continuo = 1;
		for (int x = 0, y = dadosFilters.size() ; x < y ; x++) {
			Object[] dado = dadosFilters.get(x);
			Map<String, Object[]> filters = new HashMap<String, Object[]>();
			filters = (Map<String, Object[]>) dado[1];
			if (filters != null && !filters.isEmpty()) {
				for (Object[] parameters : filters.values()) {
					for (int l = 0, s = parameters.length ; l < s ; l++) {
						query.setParameter(continuo, parameters[l]);
						continuo++;
					}
				}
			}
		}
		if (filterPai != null && !filterPai.isEmpty()) {
			for (Object[] parameters : filterPai.values()) {
				for (int x = 0, s = parameters.length ; x < s ; x++) {
					query.setParameter(continuo, parameters[x]);
					continuo++;
				}
			}
		}
		
		ManagerLog.debug(QLExpression.class, query.toString());
		return query;
	}
	
	private static boolean isBlankExpression(String expressao){
		boolean isBlank = false;
		if (!StringUtils.isBlank(expressao)) {
			int corte = expressao.indexOf(":");
			if (corte < 0) {
				isBlank = true;
			} else {
				String valor  = expressao.substring(corte + 1);
				if (StringUtils.isBlank(valor.trim())) {
					isBlank = true;
				}
			}
		} else {
			isBlank = true;
		}
		return isBlank;
	}	
}