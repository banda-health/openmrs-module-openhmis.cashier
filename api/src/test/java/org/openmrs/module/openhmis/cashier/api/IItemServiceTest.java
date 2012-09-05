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
import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.cashier.api.model.Item;
import org.openmrs.module.openhmis.cashier.api.model.ItemCode;
import org.openmrs.module.openhmis.cashier.api.model.ItemPrice;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Set;

public class IItemServiceTest extends IMetadataServiceTest<IItemService, Item> {
	IDepartmentService departmentService;

	public static final String ITEM_DATASET = "org/openmrs/module/openhmis/cashier/api/include/ItemTest.xml";

	@Override
	public void before() throws Exception {
		super.before();

		departmentService = Context.getService(IDepartmentService.class);

		executeDataSet(IDepartmentServiceTest.DEPARTMENT_DATASET);
		executeDataSet(ITEM_DATASET);
	}

	@Override
	protected IItemService createService() {
		return Context.getService(IItemService.class);
	}

	@Override
	protected int getTestEntityCount() {
		return 1;
	}

	@Override
	protected Item createEntity(boolean valid) {
		Item item = new Item();
		item.setDepartment(departmentService.getById(0));
		item.setCreator(Context.getAuthenticatedUser());

		if (valid) {
			item.setName("Test Name");
		}

		item.setDescription("Test Description");

		item.addCode("one", "Test Code 010");
		item.addCode("two", "Test Code 011");

		ItemPrice price = item.addPrice("default", BigDecimal.valueOf(100));
		item.addPrice("second", BigDecimal.valueOf(200));
		item.setDefaultPrice(price);

		return item;
	}

	@Override
	protected void updateEntityFields(Item item) {
		item.setDepartment(departmentService.getById(1));
		item.setDescription(item.getDescription() + " Updated");
		item.setName(item.getName() + " Updated");

		Set<ItemCode> codes = item.getCodes();
		if (codes.size() > 0) {
			// Update an existing code
			Iterator<ItemCode> iterator = codes.iterator();
			ItemCode code = iterator.next();
			code.setName(code.getName() + " Updated");
			code.setCode(code.getCode() + " Updated");

			if (codes.size() > 1) {
				// Delete an existing code
				code = iterator.next();

				item.removeCode(code);
			}
		}

		// Add a new code
		item.addCode("three", "Test Code 012");

		Set<ItemPrice> prices = item.getPrices();
		if (prices.size() > 0) {
			// Update n existing price
			Iterator<ItemPrice> iterator = prices.iterator();
			ItemPrice price = iterator.next();
			price.setName(price.getName() + " Updated");
			price.setPrice(price.getPrice().multiply(BigDecimal.valueOf(10)));

			if (prices.size() > 1) {
				// Delete an existing price
				price = iterator.next();

				item.removePrice(price);
			}
		}

		// Add a new price
		ItemPrice price = item.addPrice("third", BigDecimal.valueOf(3));

		item.setDefaultPrice(price);
	}

	@Override
	protected void assertEntity(Item expected, Item actual) {
		super.assertEntity(expected, actual);

		Assert.assertNotNull(expected.getDepartment());
		Assert.assertNotNull(actual.getDepartment());
		Assert.assertEquals(expected.getDepartment().getId(), actual.getDepartment().getId());

		Assert.assertEquals(expected.getCodes().size(), actual.getCodes().size());
		ItemCode[] expectedCodes = new ItemCode[expected.getCodes().size()];
		expected.getCodes().toArray(expectedCodes);
		ItemCode[] actualCodes = new ItemCode[actual.getCodes().size()];
		actual.getCodes().toArray(actualCodes);
		for (int i = 0; i < expected.getCodes().size(); i++) {
			Assert.assertEquals(expectedCodes[i].getId(), actualCodes[i].getId());
			Assert.assertEquals(expectedCodes[i].getName(), actualCodes[i].getName());
			Assert.assertEquals(expectedCodes[i].getCode(), actualCodes[i].getCode());
		}

		Assert.assertEquals(expected.getPrices().size(), actual.getPrices().size());
		ItemPrice[] expectedPrices = new ItemPrice[expected.getPrices().size()];
		expected.getPrices().toArray(expectedPrices);
		ItemPrice[] actualPrices = new ItemPrice[actual.getPrices().size()];
		actual.getPrices().toArray(actualPrices);
		for (int i = 0; i < expected.getPrices().size(); i++) {
			Assert.assertEquals(expectedPrices[i].getId(), actualPrices[i].getId());
			Assert.assertEquals(expectedPrices[i].getName(), actualPrices[i].getName());
			Assert.assertEquals(expectedPrices[i].getPrice(), actualPrices[i].getPrice());
		}
	}

	/**
	 * @verifies throw IllegalArgumentException if the item code is null
	 * @see IItemService#getItemByCode(String)
	 */
	@Test
	public void getItemByCode_shouldThrowIllegalArgumentExceptionIfTheItemCodeIsNull() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}

	/**
	 * @verifies throw IllegalArgumentException if the item code is longer than 255 characters
	 * @see IItemService#getItemByCode(String)
	 */
	@Test
	public void getItemByCode_shouldThrowIllegalArgumentExceptionIfTheItemCodeIsLongerThan255Characters() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}

	/**
	 * @verifies return the item with the specified item code
	 * @see IItemService#getItemByCode(String)
	 */
	@Test
	public void getItemByCode_shouldReturnTheItemWithTheSpecifiedItemCode() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}

	/**
	 * @verifies return null if the item code is not found
	 * @see IItemService#getItemByCode(String)
	 */
	@Test
	public void getItemByCode_shouldReturnNullIfTheItemCodeIsNotFound() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}

	/**
	 * @verifies throw IllegalArgumentException if the department is null
	 * @see IItemService#findItems(org.openmrs.module.openhmis.cashier.api.model.Department, String, boolean)
	 */
	@Test
	public void findItems_shouldThrowIllegalArgumentExceptionIfTheDepartmentIsNull() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}

	/**
	 * @verifies throw IllegalArgumentException if the name is null
	 * @see IItemService#findItems(org.openmrs.module.openhmis.cashier.api.model.Department, String, boolean)
	 */
	@Test
	public void findItems_shouldThrowIllegalArgumentExceptionIfTheNameIsNull() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}

	/**
	 * @verifies throw IllegalArgumentException if the name is empty
	 * @see IItemService#findItems(org.openmrs.module.openhmis.cashier.api.model.Department, String, boolean)
	 */
	@Test
	public void findItems_shouldThrowIllegalArgumentExceptionIfTheNameIsEmpty() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}

	/**
	 * @verifies throw IllegalArgumentException if the name is longer than 255 characters
	 * @see IItemService#findItems(org.openmrs.module.openhmis.cashier.api.model.Department, String, boolean)
	 */
	@Test
	public void findItems_shouldThrowIllegalArgumentExceptionIfTheNameIsLongerThan255Characters() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}

	/**
	 * @verifies return an empty list if no items are found
	 * @see IItemService#findItems(org.openmrs.module.openhmis.cashier.api.model.Department, String, boolean)
	 */
	@Test
	public void findItems_shouldReturnAnEmptyListIfNoItemsAreFound() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}

	/**
	 * @verifies not return retired items unless specified
	 * @see IItemService#findItems(org.openmrs.module.openhmis.cashier.api.model.Department, String, boolean)
	 */
	@Test
	public void findItems_shouldNotReturnRetiredItemsUnlessSpecified() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}

	/**
	 * @verifies return items that start with the specified name
	 * @see IItemService#findItems(org.openmrs.module.openhmis.cashier.api.model.Department, String, boolean)
	 */
	@Test
	public void findItems_shouldReturnItemsThatStartWithTheSpecifiedName() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}

	/**
	 * @verifies return items for only the specified department
	 * @see IItemService#findItems(org.openmrs.module.openhmis.cashier.api.model.Department, String, boolean)
	 */
	@Test
	public void findItems_shouldReturnItemsForOnlyTheSpecifiedDepartment() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}
}
