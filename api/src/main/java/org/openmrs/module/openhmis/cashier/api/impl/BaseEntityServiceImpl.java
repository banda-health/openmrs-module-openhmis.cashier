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
import org.openmrs.api.context.Context;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.openhmis.cashier.api.IEntityAuthorizationPrivileges;
import org.openmrs.module.openhmis.cashier.api.IEntityService;
import org.openmrs.module.openhmis.cashier.api.db.hibernate.IGenericHibernateDAO;

import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * The base type for entity services. Provides the core implementation for the common {@link BaseOpenmrsObject} operations.
 * @param <E> The entity type.
 */
public abstract class BaseEntityServiceImpl<E extends BaseOpenmrsObject, P extends IEntityAuthorizationPrivileges>
		extends BaseOpenmrsService implements IEntityService<E> {
	protected IGenericHibernateDAO dao;
	private Class entityClass = null;

	/**
	 * Gets the privileges to use for this service or {@code null} if none are needed.
	 * @return The service privileges.
	 */
	protected abstract P getPrivileges();

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
	public void setDao(IGenericHibernateDAO dao) {
		this.dao = dao;
	}

	/**
	 * @return the dao
	 */
	public IGenericHibernateDAO getDao() {
		return dao;
	}

	@Override
	public E save(E entity) throws APIException {
		P privileges = getPrivileges();
		if (privileges != null && !StringUtils.isEmpty(privileges.getSavePrivilege())) {
			Context.requirePrivilege(privileges.getSavePrivilege());
		}

		if (entity == null) {
			throw new IllegalArgumentException("The entity to save cannot be null.");
		}

		validate(entity);

		return dao.save(entity);
	}

	@Override
	public void purge(E entity) throws APIException {
		P privileges = getPrivileges();
		if (privileges != null && !StringUtils.isEmpty(privileges.getPurgePrivilege())) {
			Context.requirePrivilege(privileges.getPurgePrivilege());
		}

		if (entity == null) {
			throw new IllegalArgumentException("The entity to purge cannot be null.");
		}

		dao.delete(entity);
	}

	@Override
	public List<E> getAll() throws APIException {
		P privileges = getPrivileges();
		if (privileges != null && !StringUtils.isEmpty(privileges.getGetPrivilege())) {
			Context.requirePrivilege(privileges.getGetPrivilege());
		}

		return dao.select(getEntityClass());
	}

	@Override
	public E getById(int entityId) throws APIException {
		P privileges = getPrivileges();
		if (privileges != null && !StringUtils.isEmpty(privileges.getGetPrivilege())) {
			Context.requirePrivilege(privileges.getGetPrivilege());
		}

		return dao.selectSingle(getEntityClass(), entityId);
	}

	@Override
	public E getByUuid(String uuid) throws APIException {
		P privileges = getPrivileges();
		if (privileges != null && !StringUtils.isEmpty(privileges.getGetPrivilege())) {
			Context.requirePrivilege(privileges.getGetPrivilege());
		}

		if (StringUtils.isEmpty(uuid)) {
			throw new IllegalArgumentException("The UUID must be defined.");
		}

		Criteria criteria = dao.createCriteria(getEntityClass());
		criteria.add(Restrictions.eq("uuid", uuid));

		return dao.selectSingle(getEntityClass(), criteria);
	}

	@SuppressWarnings("unchecked")
	protected Class<E> getEntityClass() {
		if (entityClass == null) {
			ParameterizedType parameterizedType = (ParameterizedType)getClass().getGenericSuperclass();

			entityClass = (Class) parameterizedType.getActualTypeArguments()[0];
		}

		return entityClass;
	}
}

