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
import org.openmrs.test.BaseModuleContextSensitiveTest;

public class IBillingItemServiceTest extends BaseModuleContextSensitiveTest {
	/**
	 * @verifies throw IllegalArgumentException if the item is null
	 * @see IBillingItemService#saveItem(org.openmrs.module.openhmis.cashier.api.model.Item)
	 */
	@Test
	public void saveItem_shouldThrowIllegalArgumentExceptionIfTheItemIsNull() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}

	/**
	 * @verifies throw APIException if the item has no name
	 * @see IBillingItemService#saveItem(org.openmrs.module.openhmis.cashier.api.model.Item)
	 */
	@Test
	public void saveItem_shouldThrowAPIExceptionIfTheItemHasNoName() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}

	/**
	 * @verifies throw APIException if the item has no department
	 * @see IBillingItemService#saveItem(org.openmrs.module.openhmis.cashier.api.model.Item)
	 */
	@Test
	public void saveItem_shouldThrowAPIExceptionIfTheItemHasNoDepartment() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}

	/**
	 * @verifies throw APIException if the item name is longer than 255 characters
	 * @see IBillingItemService#saveItem(org.openmrs.module.openhmis.cashier.api.model.Item)
	 */
	@Test
	public void saveItem_shouldThrowAPIExceptionIfTheItemNameIsLongerThan255Characters() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}

	/**
	 * @verifies throw APIException if the item description is longer than 1024 characters
	 * @see IBillingItemService#saveItem(org.openmrs.module.openhmis.cashier.api.model.Item)
	 */
	@Test
	public void saveItem_shouldThrowAPIExceptionIfTheItemDescriptionIsLongerThan1024Characters() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}

	/**
	 * @verifies throw APIException if the item has an item code that is already defined
	 * @see IBillingItemService#saveItem(org.openmrs.module.openhmis.cashier.api.model.Item)
	 */
	@Test
	public void saveItem_shouldThrowAPIExceptionIfTheItemHasAnItemCodeThatIsAlreadyDefined() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}

	/**
	 * @verifies return saved item
	 * @see IBillingItemService#saveItem(org.openmrs.module.openhmis.cashier.api.model.Item)
	 */
	@Test
	public void saveItem_shouldReturnSavedItem() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}

	/**
	 * @verifies update item successfully
	 * @see IBillingItemService#saveItem(org.openmrs.module.openhmis.cashier.api.model.Item)
	 */
	@Test
	public void saveItem_shouldUpdateItemSuccessfully() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}

	/**
	 * @verifies create item successfully
	 * @see IBillingItemService#saveItem(org.openmrs.module.openhmis.cashier.api.model.Item)
	 */
	@Test
	public void saveItem_shouldCreateItemSuccessfully() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}

	/**
	 * @verifies retire the item successfully
	 * @see IBillingItemService#retireLocation(org.openmrs.module.openhmis.cashier.api.model.Item, String)
	 */
	@Test
	public void retireLocation_shouldRetireTheItemSuccessfully() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}

	/**
	 * @verifies throw IllegalArgumentException when the item is null
	 * @see IBillingItemService#retireLocation(org.openmrs.module.openhmis.cashier.api.model.Item, String)
	 */
	@Test
	public void retireLocation_shouldThrowIllegalArgumentExceptionWhenTheItemIsNull() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}

	/**
	 * @verifies throw IllegalArgumentException when no reason is given
	 * @see IBillingItemService#retireLocation(org.openmrs.module.openhmis.cashier.api.model.Item, String)
	 */
	@Test
	public void retireLocation_shouldThrowIllegalArgumentExceptionWhenNoReasonIsGiven() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}

	/**
	 * @verifies throw IllegalArgumentException if the item is null
	 * @see IBillingItemService#unretireLocation(org.openmrs.module.openhmis.cashier.api.model.Item)
	 */
	@Test
	public void unretireLocation_shouldThrowIllegalArgumentExceptionIfTheItemIsNull() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}

	/**
	 * @verifies unretire retired item
	 * @see IBillingItemService#unretireLocation(org.openmrs.module.openhmis.cashier.api.model.Item)
	 */
	@Test
	public void unretireLocation_shouldUnretireRetiredItem() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}

	/**
	 * @verifies throw IllegalArgumentException if the item is null
	 * @see IBillingItemService#purgeLocation(org.openmrs.module.openhmis.cashier.api.model.Item)
	 */
	@Test
	public void purgeLocation_shouldThrowIllegalArgumentExceptionIfTheItemIsNull() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}

	/**
	 * @verifies delete the specified item
	 * @see IBillingItemService#purgeLocation(org.openmrs.module.openhmis.cashier.api.model.Item)
	 */
	@Test
	public void purgeLocation_shouldDeleteTheSpecifiedItem() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}

	/**
	 * @verifies return the item for the specified id
	 * @see IBillingItemService#getItem(int)
	 */
	@Test
	public void getItem_shouldReturnTheItemForTheSpecifiedId() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}

	/**
	 * @verifies return null if the item cannot be found.
	 * @see IBillingItemService#getItem(int)
	 */
	@Test
	public void getItem_shouldReturnNullIfTheItemCannotBeFound() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}

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
	 * @verifies throw IllegalArgumentException if the name is null
	 * @see IBillingItemService#findItems(String, boolean)
	 */
	@Test
	public void findItems_shouldThrowIllegalArgumentExceptionIfTheNameIsNull() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}

	/**
	 * @verifies throw IllegalArgumentException if the name is empty
	 * @see IBillingItemService#findItems(String, boolean)
	 */
	@Test
	public void findItems_shouldThrowIllegalArgumentExceptionIfTheNameIsEmpty() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}

	/**
	 * @verifies throw IllegalArgumentException if the name is longer than 255 characters
	 * @see IBillingItemService#findItems(String, boolean)
	 */
	@Test
	public void findItems_shouldThrowIllegalArgumentExceptionIfTheNameIsLongerThan255Characters() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}

	/**
	 * @verifies return an empty list if no items are found
	 * @see IBillingItemService#findItems(String, boolean)
	 */
	@Test
	public void findItems_shouldReturnAnEmptyListIfNoItemsAreFound() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}

	/**
	 * @verifies not return retired items unless specified
	 * @see IBillingItemService#findItems(String, boolean)
	 */
	@Test
	public void findItems_shouldNotReturnRetiredItemsUnlessSpecified() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}

	/**
	 * @verifies return items that start with the specified name
	 * @see IBillingItemService#findItems(String, boolean)
	 */
	@Test
	public void findItems_shouldReturnItemsThatStartWithTheSpecifiedName() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}

	/**
	 * @verifies return items for all departments
	 * @see IBillingItemService#findItems(String, boolean)
	 */
	@Test
	public void findItems_shouldReturnItemsForAllDepartments() throws Exception {
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
	 * @verifies return items for only the specified department
	 * @see IBillingItemService#findItems(org.openmrs.module.openhmis.cashier.api.model.Department, String, boolean)
	 */
	@Test
	public void findItems_shouldReturnItemsForOnlyTheSpecifiedDepartment() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}
}
