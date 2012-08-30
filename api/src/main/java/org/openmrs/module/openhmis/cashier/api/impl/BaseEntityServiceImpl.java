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

package org.openmrs.module.openhmis.cashier.api.impl;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.openmrs.BaseOpenmrsObject;
import org.openmrs.api.APIException;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.openhmis.cashier.api.IEntityService;
import org.openmrs.module.openhmis.cashier.api.db.hibernate.IGenericHibernateDAO;

import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * The base type for entity services which provides the core implementation for the common {@link BaseOpenmrsObject} operations.
 * @param <T> The entity data access object type.
 * @param <E> The entity type.
 */
public abstract class BaseEntityServiceImpl<T extends IGenericHibernateDAO<E>, E extends BaseOpenmrsObject>
		extends BaseOpenmrsService implements IEntityService<T, E> {
	protected T dao;
	private Class entityClass = null;

	/**
	 * Validates the specified entity, throwing an exception in the validation fails.
	 * @param entity The entity to validate.
	 * @should not throw an exception for valid objects
	 * @should throw IllegalArgumentException with a null entity
	 * @should throw an exception for invalid objects
	 */
	protected abstract void validate(E entity) throws APIException;

	/**
	 * @param dao the dao to set
	 */
	public void setDao(T dao) {
		this.dao = dao;
	}

	/**
	 * @return the dao
	 */
	public T getDao() {
		return dao;
	}

	@Override
	public E save(E entity) throws APIException {
		if (entity == null) {
			throw new IllegalArgumentException("The entity to save cannot be null.");
		}

		validate(entity);

		return dao.save(entity);
	}

	@Override
	public void purge(E entity) throws APIException {
		if (entity == null) {
			throw new IllegalArgumentException("The entity to purge cannot be null.");
		}

		dao.delete(entity);
	}

	@Override
	public List<E> getAll() throws APIException {
		return dao.select();
	}

	@Override
	public E getById(int entityId) throws APIException {
		return dao.selectSingle(entityId);
	}

	@Override
	public E getByUuid(String uuid) throws APIException {
		if (StringUtils.isEmpty(uuid)) {
			throw new IllegalArgumentException("The UUID must be defined.");
		}

		Criteria criteria = dao.createCriteria();
		criteria.add(Restrictions.eq("uuid", uuid));

		return dao.selectSingle(criteria);
	}

	protected Class getEntityClass() {
		if (entityClass == null) {
			ParameterizedType parameterizedType = (ParameterizedType)getClass().getGenericSuperclass();

			entityClass = (Class) parameterizedType.getActualTypeArguments()[1];
		}

		return entityClass;
	}
}

