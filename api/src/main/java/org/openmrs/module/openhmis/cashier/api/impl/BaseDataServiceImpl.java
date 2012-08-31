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
import org.openmrs.BaseOpenmrsData;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.cashier.api.IDataAuthorizationPrivileges;
import org.openmrs.module.openhmis.cashier.api.IDataService;
import org.openmrs.module.openhmis.cashier.api.db.hibernate.IGenericHibernateDAO;

/**
 * The base type for data entity services.
 * @param <T> The entity data access object type.
 * @param <E> The entity type.
 */
public abstract class BaseDataServiceImpl<T extends IGenericHibernateDAO<E>, E extends BaseOpenmrsData>
		extends BaseEntityServiceImpl<T, E, IDataAuthorizationPrivileges> implements IDataService<T, E> {

	@Override
	public E voidEncounter(E entity, String reason) {
		IDataAuthorizationPrivileges privileges = getPrivileges();
		if (privileges != null && !StringUtils.isEmpty(privileges.getVoidPrivilege())) {
			Context.requirePrivilege(privileges.getVoidPrivilege());
		}

		if (entity == null) {
			throw new IllegalArgumentException("The entity to void cannot be null.");
		}
		if (StringUtils.isEmpty(reason)) {
			throw new IllegalArgumentException("The reason to void must be defined.");
		}

		entity.setVoided(true);
		entity.setVoidReason(reason);

		return save(entity);
	}

	@Override
	public E unvoidEncounter(E entity) throws APIException {
		IDataAuthorizationPrivileges privileges = getPrivileges();
		if (privileges != null && !StringUtils.isEmpty(privileges.getVoidPrivilege())) {
			Context.requirePrivilege(privileges.getVoidPrivilege());
		}

		if (entity == null) {
			throw new IllegalArgumentException("The entity to unvoid cannot be null.");
		}

		entity.setVoided(true);
		entity.setVoidReason(null);

		return save(entity);
	}
}
