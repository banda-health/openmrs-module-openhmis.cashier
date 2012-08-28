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
import org.openmrs.module.openhmis.cashier.api.IMetadataService;
import org.openmrs.module.openhmis.cashier.api.db.hibernate.IGenericHibernateDAO;

import java.util.List;

/**
 * The base type for metadata entity services.
 * @param <T> The entity data access object type.
 * @param <E> THe entity type.
 */
public abstract class BaseMetadataServiceImpl<T extends IGenericHibernateDAO<E>, E extends BaseOpenmrsMetadata>
		extends BaseEntityServiceImpl<T, E> implements IMetadataService<T, E> {

	@Override
	public E retire(E entity, String reason) throws APIException {
		if (entity == null) {
			throw new IllegalArgumentException("The entity to retire cannot be null.");
		}
		if (StringUtils.isEmpty(reason)) {
			throw new IllegalArgumentException("The reason to retire must be defined.");
		}

		entity.setRetired(true);
		entity.setRetireReason(reason);

		return save(entity);
	}

	@Override
	public E unretire(E entity) throws APIException {
		if (entity == null) {
			throw new IllegalArgumentException("The entity to unretire cannot be null.");
		}

		entity.setRetired(false);
		entity.setRetireReason("");

		return save(entity);
	}

	@Override
	public List<E> getAll(boolean retired) throws APIException {
		Criteria criteria = dao.createCriteria();
		criteria.add(Restrictions.eq("retired", retired));

		return dao.select(criteria);
	}

	@Override
	public List<E> findByName(String nameFragment, boolean includeRetired) throws APIException {
		if (StringUtils.isEmpty(nameFragment)) {
			throw new IllegalArgumentException("The name fragment must be defined.");
		}

		Criteria criteria = dao.createCriteria();
		criteria.add(Restrictions.ilike("name", nameFragment, MatchMode.START));

		if (!includeRetired) {
			criteria.add(Restrictions.eq("retired", false));
		}

		return dao.select(criteria);
	}
}
