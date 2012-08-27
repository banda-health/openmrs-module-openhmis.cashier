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

import org.openmrs.annotation.Authorized;
import org.openmrs.api.APIException;
import org.openmrs.module.openhmis.cashier.api.db.IBillingItemDAO;
import org.openmrs.module.openhmis.cashier.api.model.Department;
import org.openmrs.module.openhmis.cashier.api.model.Item;
import org.openmrs.module.openhmis.cashier.api.util.CashierPrivilegeConstants;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface IBillingItemService extends IDaoService<IBillingItemDAO> {
	/**
	 * Saves the {@link Item} to the database, creating a new item or updating an existing one.
	 *
	 * @param item The {@link Item} to be saved to the database
	 * @return The saved {@link Item}.
	 * @should throw APIException if the item has no name
	 * @should throw APIException if the item has no department
	 * @should throw APIException if the item name is longer than 255 characters
	 * @should throw APIException if the item description is longer than 1024 characters
	 * @should throw APIException if the item has an item code that is already defined
	 * @should return saved item
	 * @should update item successfully
	 * @should create item successfully
	 */
	@Authorized( {CashierPrivilegeConstants.MANAGE_ITEMS})
	Item saveItem(Item item) throws APIException;

	/**
	 * Gets the {@link Item} with the specified {@code itemId} or {@code null} if not found.
	 * @param itemId The primary key of the item to find.
	 * @return The {@link Item} with the specified id or {@code null}.
	 * @throws APIException
	 * @should return the item for the specified id
	 * @should return null if the item cannot be found.
	 */
	@Transactional(readOnly = true)
	@Authorized( {CashierPrivilegeConstants.VIEW_ITEMS})
	Item getItem(int itemId) throws APIException;

	/**
	 * Gets the {@link Item} with the specified code or {@code null} if not found.
	 * @param itemCode The item code to find.
	 * @return The {@link Item} or with the specified item code or {@code null}.
	 * @throws APIException
	 */
	@Transactional(readOnly =  true)
	@Authorized( {CashierPrivilegeConstants.VIEW_ITEMS})
	Item getItemByCode(String itemCode) throws APIException;

	@Transactional(readOnly = true)
	@Authorized( {CashierPrivilegeConstants.VIEW_ITEMS})
	List<Item> findItems(String name, boolean includeRetired) throws APIException;

	@Transactional(readOnly = true)
	@Authorized( {CashierPrivilegeConstants.VIEW_ITEMS})
	List<Item> findItems(Department department, String name, boolean includeRetired) throws APIException;
}

