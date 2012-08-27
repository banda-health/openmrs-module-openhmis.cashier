/*
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */

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
