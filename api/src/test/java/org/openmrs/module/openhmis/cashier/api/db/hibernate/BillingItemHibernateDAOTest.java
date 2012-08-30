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

package org.openmrs.module.openhmis.cashier.api.db.hibernate;

import org.apache.commons.lang.NotImplementedException;
import org.openmrs.module.openhmis.cashier.api.model.Item;

public class BillingItemHibernateDAOTest extends IGenericHibernateDAOTest<Item> {
	protected DepartmentHibernateDAO departmentDao;

	@Override
	public void before() {
		super.before();

		departmentDao = new DepartmentHibernateDAO(sessionFactory);
	}

	@Override
	protected IGenericHibernateDAO<Item> newDao() {
		return new BillingItemHibernateDAO(sessionFactory);
	}

	@Override
	protected Item newEntity() {
		throw new NotImplementedException();
	}

	@Override
	public void save_shouldInsertANewItemIntoTheDatabase() throws Exception {
		// Create new entity

		// Save entity to database

		// Get newly created entity from db

		// Validate entity fields
	}

	@Override
	public void save_shouldUpdateAnExistingItemInTheDatabase() throws Exception {
		// Get known entity from database

		// Update fields

		// Save entity

		// Get newly updated entity from db

		// Validate entity fields
	}

	@Override
	public void save_shouldReturnANewItemWithTheGeneratedId() throws Exception {
		// Create new entity

		// Save entity

		// Check that id is not null
	}

	@Override
	public void delete_shouldNotThrowAnExceptionIfTheItemIsNotInTheDatabase() throws Exception {
		// Create new entity

		// Delete entity from database
	}

	@Override
	public void selectSingle_shouldReturnTheEntityWithTheSpecifiedId() throws Exception {
		// Get entity with known id

		// Check that entity is not null
	}

	@Override
	public void selectSingle_shouldReturnTheEntityThatMeetsTheCriteria() throws Exception {
		// Get entity with known field

		// Check that entity is not null
	}

	@Override
	public void selectSingle_shouldReturnNullIfNoEntityCanBeFound() throws Exception {
		// Get entity with known undefined field

		// Check that entity is null
	}

	@Override
	public void selectSingle_shouldReturnTheFirstEntityIfMultipleEntitiesAreFound() throws Exception {
		// Get entities with known fields by known order

		// Check that first entity is returned
	}

	@Override
	public void select_shouldReturnAListOfAllTheEntities() throws Exception {
		// Get list of entities

		// Assert known list size
	}

	@Override
	public void select_shouldReturnAListOfAllEntitiesThatMeetTheCriteria() throws Exception {
		// Get list of entities by known field

		// Assert known list size and entities
	}

	@Override
	public void select_shouldReturnAnEmptyListWhenNoEntitiesAreFound() throws Exception {
		// Get list of entities with invalid field

		// Assert list not null
		// Assert list size is zero
	}
}

