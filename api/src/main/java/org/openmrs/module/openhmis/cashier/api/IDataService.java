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

package org.openmrs.module.openhmis.cashier.api;

import org.openmrs.BaseOpenmrsData;
import org.openmrs.api.APIException;
import org.openmrs.module.openhmis.cashier.api.db.IEntityDao;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface IDataService<T extends IEntityDao, E extends BaseOpenmrsData> extends IEntityService<T, E> {
	/**
	 * Voiding an entity essentially removes it from circulation.
	 *
	 * @param entity The entity object to void.
	 * @param reason The reason for voiding.
	 * @should void the entity
	 * @should throw IllegalArgumentException with null reason parameter
	 * @should throw IllegalArgumentException with null entity
	 */
	public E voidEncounter(E entity, String reason);

	/**
	 * Unvoid the entity record.
	 *
	 * @param entity The entity to be revived.
	 * @should unvoid the entity
	 * @should throw IllegalArgumentException with null entity
	 */
	public E unvoidEncounter(E entity) throws APIException;
}
