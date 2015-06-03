
package com.hcentive.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * The Interface GenericDAO.
 */
public interface GenericDAO<T extends Serializable> {

	/**
	 * Insert T into the database.
	 * 
	 * @param t
	 *        the persistable Object which needs to be created
	 */
	public T create(T t);

	/**
	 * Updates T.
	 * 
	 * @param t
	 *        the persistable object which needs to be updated
	 * @return the updated object
	 */
	public T update(T t);

    /**
     * Updates Collection of T.
     * 
     * @param collection
     *            of t the persistable objects which needs to be updated
     * @return the collection of updated objects
     */

    public Collection<T> updateAll(Collection<T> objectsToBePersisted);
    
	/**
	 * Gets the object by primary key.
	 * 
	 * @param clas
	 *        the class of persistable object
	 * @param primaryKeyId
	 *        the primary key id
	 * @return the t
	 */
	public T get(Class<T> clas, Serializable primaryKeyId);

	/**
	 * Deletes the object by primary key and class.
	 * 
	 * @param clas
	 *        the class
	 * @param id
	 *        the id
	 */
	public void delete(Class<T> clas, Serializable id);

	/**
	 * Find by named query.
	 * 
	 * @param queryName
	 *        the query name
	 * @return the list of persistable
	 */
	public  List<T> findByNamedQuery(String queryName);

	/**
	 * Find by named query and named params.
	 * 
	 * @param queryName
	 *        - the query with named params
	 * @param paramNames
	 *        - the param names as mentioned in query.
	 * @param paramValues
	 *        - the param values, should be in same order as the order of paramNames.
	 * @return the list< t>
	 */
	public List<T> findByNamedQuery(final String queryName, final String[] paramNames, final Object... paramValues);

	public List<T> findByNamedQuery(final String queryName, final Map<String, Object> params);
	
	/**
	 * 
	 */
	public List findListObjectByNamedQuery(String queryName);
	
	public List findListObjectByNamedQuery(final String queryName, final String[] paramNames, final Object... paramValues);

	public List findListObjectByNamedQuery(final String queryName, final Map<String, Object> params);

	public Object findObjectByNamedQuery(String queryName, String[] paramNames, Object... paramValues);

	public Object findObjectByNamedQuery(String queryName, Map<String, Object> params);

	/**
	 * Execute update by named query and params.
	 * 
	 * @param queryName
	 *        - the query with named params
	 * @param paramNames
	 *        - the param names as mentioned in query.
	 * @param paramValues
	 *        - the param values, should be in same order as the order of paramNames.
	 * @return the int
	 */
	public int executeUpdateNamedQuery(final String queryName, final Map<String, Object> params);

	/**
	 * Returns the list of object given a SQL native query and params.
	 * 
	 * @deprecated
	 */
	@Deprecated
	public List<T> findByNativeQuery(final String query, final Object... args);

	public Integer deleteByNamedQuery(String queryName, String[] paramNames, Object... paramValues);

	public List<T> findPaginatedByNamedQuery(final String queryName, int startPosition, int maxResult, final String[] paramNames, final Object... paramValues);

	public EntityManager getEntityManager();

	public void delete(T t);

    public void deleteAll(Collection<T> objectsToDelete);
    
	/**
	 * Find by named query and named params.
	 * 
	 * @param queryName
	 *        - the query with named params
	 * @param params
	 *        - the param name , param value pair.
	 * @param startPosition
	 * 		  - Set the position of the first result to retrieve.
	 * @param maxResult
	 *        - Set the maximum number of results to retrieve.
	 * @return the list< t>
	 */
	List<T> findByNamedQuery(String queryName, Map<String, Object> params,
			int startPosition, int maxResult);
	
	void setQueryParams(Query queryObj, Map<String, Object> params);
}
