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

import org.junit.Assert;
import org.junit.Test;
import org.openmrs.BaseOpenmrsObject;
import org.openmrs.module.openhmis.cashier.api.db.hibernate.IGenericHibernateDAO;

public abstract class IEntityServiceTest<T extends IGenericHibernateDAO<E>, E extends BaseOpenmrsObject> {
	/**
	 * @verifies throw IllegalArgumentException if the entity is null
	 * @see IEntityService#save(E)
	 */
	@Test
	public void save_shouldThrowIllegalArgumentExceptionIfTheEntityIsNull() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}

	/**
	 * @verifies validate the entity before saving
	 * @see IEntityService#save(E)
	 */
	@Test
	public void save_shouldValidateTheEntityBeforeSaving() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}

	/**
	 * @verifies return saved entity
	 * @see IEntityService#save(E)
	 */
	@Test
	public void save_shouldReturnSavedEntity() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}

	/**
	 * @verifies update the entity successfully
	 * @see IEntityService#save(E)
	 */
	@Test
	public void save_shouldUpdateTheEntitySuccessfully() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}

	/**
	 * @verifies create the entity successfully
	 * @see IEntityService#save(E)
	 */
	@Test
	public void save_shouldCreateTheEntitySuccessfully() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}

	/**
	 * @verifies throw IllegalArgumentException if the entity is null
	 * @see IEntityService#purge(E)
	 */
	@Test
	public void purge_shouldThrowIllegalArgumentExceptionIfTheEntityIsNull() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}

	/**
	 * @verifies delete the specified entity
	 * @see IEntityService#purge(E)
	 */
	@Test
	public void purge_shouldDeleteTheSpecifiedEntity() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}

	/**
	 * @verifies return all entity records
	 * @see IEntityService#getAll()
	 */
	@Test
	public void getAll_shouldReturnAllEntityRecords() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}

	/**
	 * @verifies return an empty list if there are no entities
	 * @see IEntityService#getAll()
	 */
	@Test
	public void getAll_shouldReturnAnEmptyListIfThereAreNoEntities() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}

	/**
	 * @verifies return the entity with the specified id
	 * @see IEntityService#getById(int)
	 */
	@Test
	public void getById_shouldReturnTheEntityWithTheSpecifiedId() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}

	/**
	 * @verifies return null if no entity can be found.
	 * @see IEntityService#getById(int)
	 */
	@Test
	public void getById_shouldReturnNullIfNoEntityCanBeFound() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}

	/**
	 * @verifies find the entity with the specified uuid
	 * @see IEntityService#getByUuid(String)
	 */
	@Test
	public void getByUuid_shouldFindTheEntityWithTheSpecifiedUuid() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}

	/**
	 * @verifies return null if no entity is found
	 * @see IEntityService#getByUuid(String)
	 */
	@Test
	public void getByUuid_shouldReturnNullIfNoEntityIsFound() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}

	/**
	 * @verifies throw IllegalArgumentException if uuid is null
	 * @see IEntityService#getByUuid(String)
	 */
	@Test
	public void getByUuid_shouldThrowIllegalArgumentExceptionIfUuidIsNull() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}

	/**
	 * @verifies throw IllegalArgumentException if uuid is empty
	 * @see IEntityService#getByUuid(String)
	 */
	@Test
	public void getByUuid_shouldThrowIllegalArgumentExceptionIfUuidIsEmpty() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}
}
