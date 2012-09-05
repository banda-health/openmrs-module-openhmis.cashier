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
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.openmrs.BaseOpenmrsMetadata;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.cashier.api.IMetadataAuthorizationPrivileges;
import org.openmrs.module.openhmis.cashier.api.IMetadataService;

import java.util.Date;
import java.util.List;

/**
 * The base type for metadata entity services.
 * @param <E> THe entity type.
 */
public abstract class BaseMetadataServiceImpl<E extends BaseOpenmrsMetadata>
		extends BaseEntityServiceImpl<E, IMetadataAuthorizationPrivileges> implements IMetadataService<E> {

	@Override
	public E retire(E entity, String reason) throws APIException {
		IMetadataAuthorizationPrivileges privileges = getPrivileges();
		if (privileges != null && !StringUtils.isEmpty(privileges.getRetirePrivilege())) {
			Context.requirePrivilege(privileges.getRetirePrivilege());
		}

		if (entity == null) {
			throw new NullPointerException("The entity to retire cannot be null.");
		}
		if (StringUtils.isEmpty(reason)) {
			throw new IllegalArgumentException("The reason to retire must be defined.");
		}

		entity.setRetired(true);
		entity.setRetireReason(reason);
		entity.setRetiredBy(Context.getAuthenticatedUser());
		entity.setDateRetired(new Date());

		return save(entity);
	}

	@Override
	public E unretire(E entity) throws APIException {
		IMetadataAuthorizationPrivileges privileges = getPrivileges();
		if (privileges != null && !StringUtils.isEmpty(privileges.getRetirePrivilege())) {
			Context.requirePrivilege(privileges.getRetirePrivilege());
		}

		if (entity == null) {
			throw new NullPointerException("The entity to unretire cannot be null.");
		}

		entity.setRetired(false);
		entity.setRetireReason(null);
		entity.setRetiredBy(null);

		return save(entity);
	}

	@Override
	public List<E> getAll(boolean retired) throws APIException {
		IMetadataAuthorizationPrivileges privileges = getPrivileges();
		if (privileges != null && !StringUtils.isEmpty(privileges.getGetPrivilege())) {
			Context.requirePrivilege(privileges.getGetPrivilege());
		}

		Criteria criteria = dao.createCriteria(getEntityClass());
		criteria.add(Restrictions.eq("retired", retired));

		return dao.select(getEntityClass(), criteria);
	}

	@Override
	public List<E> findByName(String nameFragment, boolean includeRetired) throws APIException {
		IMetadataAuthorizationPrivileges privileges = getPrivileges();
		if (privileges != null && !StringUtils.isEmpty(privileges.getGetPrivilege())) {
			Context.requirePrivilege(privileges.getGetPrivilege());
		}

		if (StringUtils.isEmpty(nameFragment)) {
			throw new IllegalArgumentException("The name fragment must be defined.");
		}
		if (nameFragment.length() > 255) {
			throw new IllegalArgumentException("the name fragment must be less than 256 characters long.");
		}

		Criteria criteria = dao.createCriteria(getEntityClass());
		criteria.add(Restrictions.ilike("name", nameFragment, MatchMode.START));

		if (!includeRetired) {
			criteria.add(Restrictions.eq("retired", false));
		}

		return dao.select(getEntityClass(), criteria);
	}
}
