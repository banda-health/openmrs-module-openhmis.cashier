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
import org.openmrs.BaseOpenmrsMetadata;

public abstract class IMetadataServiceTest<S extends IMetadataService<E>, E extends BaseOpenmrsMetadata>
		extends IEntityServiceTest<S, E> {

	@Override
	protected void assertEntity(E expected, E actual) {
		super.assertEntity(expected, actual);

		Assert.assertEquals(expected.getChangedBy(), actual.getChangedBy());
		Assert.assertEquals(expected.getCreator(), actual.getCreator());
		Assert.assertEquals(expected.getDateChanged(), actual.getDateChanged());
		Assert.assertEquals(expected.getDateCreated(), actual.getDateCreated());
		Assert.assertEquals(expected.getDateRetired(), actual.getDateRetired());
		Assert.assertEquals(expected.getDescription(), actual.getDescription());
		Assert.assertEquals(expected.getName(), actual.getName());
		Assert.assertEquals(expected.getRetired(), actual.getRetired());
		Assert.assertEquals(expected.getRetiredBy(), actual.getRetiredBy());
		Assert.assertEquals(expected.getRetireReason(), actual.getRetireReason());
	}

	/**
	 * @verifies retire the entity successfully
	 * @see IMetadataService#retire(E, String)
	 */
	@Test
	public void retire_shouldRetireTheEntitySuccessfully() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}

	/**
	 * @verifies throw IllegalArgumentException when the entity is null
	 * @see IMetadataService#retire(E, String)
	 */
	@Test
	public void retire_shouldThrowIllegalArgumentExceptionWhenTheEntityIsNull() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}

	/**
	 * @verifies throw IllegalArgumentException when no reason is given
	 * @see IMetadataService#retire(E, String)
	 */
	@Test
	public void retire_shouldThrowIllegalArgumentExceptionWhenNoReasonIsGiven() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}

	/**
	 * @verifies throw IllegalArgumentException if the entity is null
	 * @see IMetadataService#unretire(E)
	 */
	@Test
	public void unretire_shouldThrowIllegalArgumentExceptionIfTheEntityIsNull() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}

	/**
	 * @verifies unretire the entity
	 * @see IMetadataService#unretire(E)
	 */
	@Test
	public void unretire_shouldUnretireTheEntity() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}

	/**
	 * @verifies return all retired entities when retired is set to true
	 * @see IMetadataService#getAll(boolean)
	 */
	@Test
	public void getAll_shouldReturnAllRetiredEntitiesWhenRetiredIsSetToTrue() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}

	/**
	 * @verifies return all unretired entities when retired is set to false
	 * @see IMetadataService#getAll(boolean)
	 */
	@Test
	public void getAll_shouldReturnAllUnretiredEntitiesWhenRetiredIsSetToFalse() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}

	/**
	 * @verifies throw IllegalArgumentException if the name is null
	 * @see IMetadataService#findByName(String, boolean)
	 */
	@Test
	public void findByName_shouldThrowIllegalArgumentExceptionIfTheNameIsNull() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}

	/**
	 * @verifies throw IllegalArgumentException if the name is empty
	 * @see IMetadataService#findByName(String, boolean)
	 */
	@Test
	public void findByName_shouldThrowIllegalArgumentExceptionIfTheNameIsEmpty() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}

	/**
	 * @verifies throw IllegalArgumentException if the name is longer than 255 characters
	 * @see IMetadataService#findByName(String, boolean)
	 */
	@Test
	public void findByName_shouldThrowIllegalArgumentExceptionIfTheNameIsLongerThan255Characters() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}

	/**
	 * @verifies return an empty list if no entities are found
	 * @see IMetadataService#findByName(String, boolean)
	 */
	@Test
	public void findByName_shouldReturnAnEmptyListIfNoEntitiesAreFound() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}

	/**
	 * @verifies not return retired entities unless specified
	 * @see IMetadataService#findByName(String, boolean)
	 */
	@Test
	public void findByName_shouldNotReturnRetiredEntitiesUnlessSpecified() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}

	/**
	 * @verifies return entities that start with the specified name
	 * @see IMetadataService#findByName(String, boolean)
	 */
	@Test
	public void findByName_shouldReturnEntitiesThatStartWithTheSpecifiedName() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}
}

