package org.openmrs.module.openhmis.cashier.api.db.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseHibernateDAO<T> {

	SessionFactory sessionFactory;
	private final Class<T> type;
	
	@Autowired
	public BaseHibernateDAO(Class<T> type, SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
		this.type = type; 
	}
	
	/**
	 * @should get all objects of type T from the database.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<T> getAll() {
		Session session = sessionFactory.getCurrentSession();

		try {
			Criteria search = session.createCriteria(type);
			return search.list();
		} catch (Exception ex) {
			throw new RuntimeException("An exception occurred while attempting to retrieve all " + type.getSimpleName() + " objects.", ex);
		}
	}

	public Integer add(T object) {
		Session session = sessionFactory.getCurrentSession();
		Integer listId = null;

		try {
			listId = (Integer) session.save(object);
		} catch (Exception ex) {
			throw new RuntimeException("An exception occurred while attempting to add a " + type.getSimpleName() + " object.", ex);
		}
		return listId;
	}

	public void remove(String keyName, String keyValue) {
		Session session = sessionFactory.getCurrentSession();

		try {
			// Find the lists with the specified key (should only be one)
			Criteria search = session.createCriteria(type)
					.add(Restrictions.eq(keyName, keyValue));

			// Delete the lists
			for (Object list : search.list()) {
				session.delete(list);
			}
			session.flush();
		} catch (Exception ex) {
			throw new RuntimeException("An exception occurred while attempting to remove a " + type.getSimpleName() + " object.", ex);
		}		
	}
}
