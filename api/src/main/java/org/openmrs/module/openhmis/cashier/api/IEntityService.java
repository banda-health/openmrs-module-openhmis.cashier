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

import org.openmrs.BaseOpenmrsObject;
import org.openmrs.api.APIException;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.openhmis.cashier.api.db.IEntityDao;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface IEntityService<T extends IEntityDao, E extends BaseOpenmrsObject> extends OpenmrsService {
	/**
	 * Set the data access object that the service will use to interact with the database. This is
	 * set by spring in the applicationContext-service.xml file
	 *
	 * @param dao The data access object that the service will use
	 */
	void setDao(T dao);

	/**
	 * Saves the entity to the database, creating a new item or updating an existing one.
	 *
	 * @param entity The entity to be saved to the database
	 * @return The saved entity.
	 * @should throw IllegalArgumentException if the entity is null
	 * @should validate the entity before saving
	 * @should return saved entity
	 * @should update the entity successfully
	 * @should create the entity successfully
	 */
	E save(E entity) throws APIException;

	/**
	 * Completely remove an entity from the database (not reversible).
	 *
	 * @param entity the entity to remove from the database.
	 * @should throw IllegalArgumentException if the entity is null
	 * @should delete the specified entity
	 */
	void purge(E entity) throws APIException;

	/**
	 * Returns all entity records.
	 *
	 * @return All entity records that are in the database.
	 * @should return all entity records
	 * @should return an empty list if there are no entities
	 */
	@Transactional(readOnly = true)
	List<E> getAll() throws APIException;

	/**
	 * Gets the entity with the specified entity id or {@code null} if not found.
	 * @param entityId The primary key of the entity to find.
	 * @return The entity with the specified id or {@code null}.
	 * @throws APIException
	 * @should return the entity with the specified id
	 * @should return null if no entity can be found.
	 */
	@Transactional(readOnly = true)
	E getById(int entityId) throws APIException;

	/**
	 * Gets an entity by uuid.
	 *
	 * @param uuid is the uuid of the desired entity.
	 * @return the entity with the specified uuid.
	 * @should find the entity with the specified uuid
	 * @should return null if no entity is found
	 * @should throw IllegalArgumentException if uuid is null
	 * @should throw IllegalArgumentException if uuid is empty
	 */
	@Transactional(readOnly = true)
	E getByUuid(String uuid) throws APIException;
}

