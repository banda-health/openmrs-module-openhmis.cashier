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

import org.openmrs.BaseOpenmrsObject;
import org.openmrs.api.APIException;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.openhmis.cashier.api.IEntityService;
import org.openmrs.module.openhmis.cashier.api.db.IEntityDao;

import java.util.List;

public abstract class BaseEntityServiceImpl<T extends IEntityDao, E extends BaseOpenmrsObject>
		extends BaseOpenmrsService implements IEntityService<T, E> {
	private T dao;

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
		return null;
	}

	@Override
	public void purge(E entity) throws APIException {
	}

	@Override
	public List<E> getAll() throws APIException {
		return null;
	}

	@Override
	public E getById(int entityId) throws APIException {
		return null;
	}

	@Override
	public E getByUuid(String uuid) throws APIException {
		return null;
	}
}

