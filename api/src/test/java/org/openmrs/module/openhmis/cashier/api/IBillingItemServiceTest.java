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

public class IBillingItemServiceTest {
	/**
	 * @verifies throw IllegalArgumentException if the item code is null
	 * @see IBillingItemService#getItemByCode(String)
	 */
	@Test
	public void getItemByCode_shouldThrowIllegalArgumentExceptionIfTheItemCodeIsNull() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}

	/**
	 * @verifies throw IllegalArgumentException if the item code is longer than 255 characters
	 * @see IBillingItemService#getItemByCode(String)
	 */
	@Test
	public void getItemByCode_shouldThrowIllegalArgumentExceptionIfTheItemCodeIsLongerThan255Characters() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}

	/**
	 * @verifies return the item with the specified item code
	 * @see IBillingItemService#getItemByCode(String)
	 */
	@Test
	public void getItemByCode_shouldReturnTheItemWithTheSpecifiedItemCode() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}

	/**
	 * @verifies return null if the item code is not found
	 * @see IBillingItemService#getItemByCode(String)
	 */
	@Test
	public void getItemByCode_shouldReturnNullIfTheItemCodeIsNotFound() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}

	/**
	 * @verifies throw IllegalArgumentException if the department is null
	 * @see IBillingItemService#findItems(org.openmrs.module.openhmis.cashier.api.model.Department, String, boolean)
	 */
	@Test
	public void findItems_shouldThrowIllegalArgumentExceptionIfTheDepartmentIsNull() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}

	/**
	 * @verifies throw IllegalArgumentException if the name is null
	 * @see IBillingItemService#findItems(org.openmrs.module.openhmis.cashier.api.model.Department, String, boolean)
	 */
	@Test
	public void findItems_shouldThrowIllegalArgumentExceptionIfTheNameIsNull() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}

	/**
	 * @verifies throw IllegalArgumentException if the name is empty
	 * @see IBillingItemService#findItems(org.openmrs.module.openhmis.cashier.api.model.Department, String, boolean)
	 */
	@Test
	public void findItems_shouldThrowIllegalArgumentExceptionIfTheNameIsEmpty() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}

	/**
	 * @verifies throw IllegalArgumentException if the name is longer than 255 characters
	 * @see IBillingItemService#findItems(org.openmrs.module.openhmis.cashier.api.model.Department, String, boolean)
	 */
	@Test
	public void findItems_shouldThrowIllegalArgumentExceptionIfTheNameIsLongerThan255Characters() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}

	/**
	 * @verifies return an empty list if no items are found
	 * @see IBillingItemService#findItems(org.openmrs.module.openhmis.cashier.api.model.Department, String, boolean)
	 */
	@Test
	public void findItems_shouldReturnAnEmptyListIfNoItemsAreFound() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}

	/**
	 * @verifies not return retired items unless specified
	 * @see IBillingItemService#findItems(org.openmrs.module.openhmis.cashier.api.model.Department, String, boolean)
	 */
	@Test
	public void findItems_shouldNotReturnRetiredItemsUnlessSpecified() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}

	/**
	 * @verifies return items that start with the specified name
	 * @see IBillingItemService#findItems(org.openmrs.module.openhmis.cashier.api.model.Department, String, boolean)
	 */
	@Test
	public void findItems_shouldReturnItemsThatStartWithTheSpecifiedName() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}

	/**
	 * @verifies return items for only the specified department
	 * @see IBillingItemService#findItems(org.openmrs.module.openhmis.cashier.api.model.Department, String, boolean)
	 */
	@Test
	public void findItems_shouldReturnItemsForOnlyTheSpecifiedDepartment() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}
}
