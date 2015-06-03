package com.hcentive.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.dao.InvalidDataAccessApiUsageException;

/**
 * The Class GenericDAOImpl.
 */
public class GenericDAOImpl<T extends Serializable> implements GenericDAO<T> {

	@PersistenceContext
	protected EntityManager entityManager;

	@Override
	public void delete(T t) {
		entityManager.remove(t);
	}

	@Override
	public void delete(Class<T> clas, Serializable id) {
		entityManager.remove(entityManager.getReference(clas, id));
	}

	@Override
	public T create(T t) {
		entityManager.persist(t);
		return t;
	}

	@Override
	public T get(Class<T> clas, Serializable id) {
		return entityManager.find(clas, id);
	}

	@Override
	public T update(T t) {
		return entityManager.merge(t);
	}

	@Override
	public int executeUpdateNamedQuery(final String queryName, final Map<String, Object> params) {
		Query queryObject = entityManager.createNamedQuery(queryName);
		if (params != null) {
			for (String key : params.keySet()) {
				queryObject.setParameter(key, params.get(key));
			}
		}
		return queryObject.executeUpdate();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<T> findByNamedQuery(final String queryName) {
		return entityManager.createNamedQuery(queryName).getResultList();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<T> findByNamedQuery(final String queryName, final String[] paramNames, final Object... paramValues) {
		Query queryObject = entityManager.createNamedQuery(queryName);
		for (int i = 0; i < paramNames.length; i++) {
			queryObject.setParameter(paramNames[i], paramValues[i]);
		}
		return queryObject.getResultList();
	}

	@Override
	public List findListObjectByNamedQuery(String queryName) {
		return entityManager.createNamedQuery(queryName).getResultList();
	}
	
	@Override
	public List findListObjectByNamedQuery(String queryName, String[] paramNames, Object... paramValues) {
		Query queryObject = entityManager.createNamedQuery(queryName);
		for (int i = 0; i < paramNames.length; i++) {
			queryObject.setParameter(paramNames[i], paramValues[i]);
		}
		return queryObject.getResultList();
	}

	@Override
	public List findListObjectByNamedQuery(String queryName, Map<String, Object> params) {
		Query queryObject = entityManager.createNamedQuery(queryName);
		if (params != null) {
			for (String key : params.keySet()) {
				queryObject.setParameter(key, params.get(key));
			}
		}
		return queryObject.getResultList();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<T> findPaginatedByNamedQuery(final String queryName, int startPosition, int maxResult, final String[] paramNames, final Object... paramValues) {
		Query queryObject = entityManager.createNamedQuery(queryName);
		for (int i = 0; i < paramNames.length; i++) {
			queryObject.setParameter(paramNames[i], paramValues[i]);
		}
		queryObject.setFirstResult(startPosition);
		queryObject.setMaxResults(maxResult);
		return queryObject.getResultList();
	}

	@Override
	public Object findObjectByNamedQuery(String queryName, String[] paramNames, Object... paramValues) {
		List<T> results = findByNamedQuery(queryName, paramNames, paramValues);
		if (results == null || results.size() == 0) {
			return null;
		}

		if (results.size() > 1) {
			throw new InvalidDataAccessApiUsageException("Multiple records found in query result where single record is expected");
		}
		return results.get(0);
	}

	@Override
	public Object findObjectByNamedQuery(String queryName, Map<String, Object> params) {
		List<T> results = findByNamedQuery(queryName, params);
		if (results == null || results.size() == 0) {
			return null;
		}

		if (results.size() > 1) {
			throw new InvalidDataAccessApiUsageException("Multiple records found in query result where single record is expected");
		}
		return results.get(0);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<T> findByNativeQuery(final String query, final Object... params) {
		Query queryObject = entityManager.createNativeQuery(query);
		if (params != null) {
			for (int i = 0; i < params.length; i++) {
				queryObject.setParameter(i, params[i]);
			}
		}
		return queryObject.getResultList();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<T> findByNamedQuery(String queryName, Map<String, Object> params, int startPosition, int maxResult) {
		Query queryObject = entityManager.createNamedQuery(queryName);
		if (params != null) {
			for (String key : params.keySet()) {
				queryObject.setParameter(key, params.get(key));
			}
		}
		queryObject.setFirstResult(startPosition);
		queryObject.setMaxResults(maxResult);
		return queryObject.getResultList();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<T> findByNamedQuery(String queryName, Map<String, Object> params) {
		Query queryObject = entityManager.createNamedQuery(queryName);
		if (params != null) {
			for (String key : params.keySet()) {
				queryObject.setParameter(key, params.get(key));
			}
		}
		return queryObject.getResultList();
	}

	@Override
	public Integer deleteByNamedQuery(String queryName, String[] paramNames, Object... paramValues) {
		Query queryObject = entityManager.createNamedQuery(queryName);
		for (int i = 0; i < paramNames.length; i++) {
			queryObject.setParameter(paramNames[i], paramValues[i]);
		}
		return queryObject.executeUpdate();
	}

	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	
	public void setQueryParams(Query queryObj, Map<String, Object> params) {
		Iterator<Entry<String, Object>> iterator = params.entrySet().iterator();
		while(iterator.hasNext()) {
			Entry<String, Object> entries = iterator.next();
			queryObj.setParameter(entries.getKey(), entries.getValue());
		}
	}
	

    @Override

    public Collection<T> updateAll(Collection<T> objectsToBePersisted) {

            Collection<T> savedObjects = new ArrayList<T>();

            for (Iterator<T> iterator = objectsToBePersisted.iterator(); iterator.hasNext();) {

                    T obj = iterator.next();

                    savedObjects.add(update(obj));

            }

            objectsToBePersisted.clear();

            objectsToBePersisted.addAll(savedObjects);

            return objectsToBePersisted;

    }

    

   

    @Override

    public void deleteAll(Collection<T> objectsToDelete) {

            for (Iterator<T> iterator = objectsToDelete.iterator(); iterator.hasNext();) {

                    T obj = iterator.next();

                    delete(obj);

            }

    }
}
