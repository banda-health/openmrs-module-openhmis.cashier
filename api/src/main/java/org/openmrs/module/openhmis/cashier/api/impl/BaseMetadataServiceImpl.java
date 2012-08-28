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

import org.openmrs.BaseOpenmrsMetadata;
import org.openmrs.api.APIException;
import org.openmrs.module.openhmis.cashier.api.IMetadataService;
import org.openmrs.module.openhmis.cashier.api.db.IEntityDao;

import java.util.List;

public abstract class BaseMetadataServiceImpl<T extends IEntityDao, E extends BaseOpenmrsMetadata>
		extends BaseEntityServiceImpl<T, E> implements IMetadataService<T, E> {

	@Override
	public E retire(E entity, String reason) throws APIException {
		return null;
	}

	@Override
	public E unretire(E entity) throws APIException {
		return null;
	}

	@Override
	public List<E> getAll(boolean retired) throws APIException {
		return null;
	}

	@Override
	public List<E> findByName(String nameFragment, boolean includeRetired) throws APIException {
		return null;
	}
}
