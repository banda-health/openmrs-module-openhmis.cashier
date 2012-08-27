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
	 * @should throw IllegalArgumentException if the item is null
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
	 * Retires the specified {@link Item}. This effectively removes the item from circulation or use.
	 *
	 * @param item {@link Item} to be retired
	 * @param reason   is the reason why the item is being retired
	 * @should retire the item successfully
	 * @should throw IllegalArgumentException when the item is null
	 * @should throw IllegalArgumentException when no reason is given
	 */
	@Authorized( { CashierPrivilegeConstants.MANAGE_ITEMS })
	Item retireLocation(Item item, String reason) throws APIException;

	/**
	 * Unretire the specified {@link Item}. This restores a previously retired item back into circulation and use.
	 *
	 * @param item The {@link Item} to unretire
	 * @return the newly unretired location
	 * @throws APIException
	 * @should throw IllegalArgumentException if the item is null
	 * @should unretire retired item
	 */
	@Authorized( { CashierPrivilegeConstants.MANAGE_ITEMS })
	Item unretireLocation(Item item) throws APIException;

	/**
	 * Completely remove an {@link Item} from the database (not reversible).
	 *
	 * @param item the {@link Item} to remove from the database.
	 * @should throw IllegalArgumentException if the item is null
	 * @should delete the specified item
	 */
	@Authorized( { CashierPrivilegeConstants.PURGE_ITEMS })
	void purgeLocation(Item item) throws APIException;

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
	 * @should throw IllegalArgumentException if the item code is null
	 * @should throw IllegalArgumentException if the item code is longer than 255 characters
	 * @should return the item with the specified item code
	 * @should return null if the item code is not found
	 */
	@Transactional(readOnly =  true)
	@Authorized( {CashierPrivilegeConstants.VIEW_ITEMS})
	Item getItemByCode(String itemCode) throws APIException;

	/**
	 * Finds all items that start with the specified name.
	 * @param name The item name fragment
	 * @param includeRetired Whether retired item should be included in the results
	 * @return All items that start with the specified name.
	 * @throws APIException
	 * @should throw IllegalArgumentException if the name is null
	 * @should throw IllegalArgumentException if the name is empty
	 * @should throw IllegalArgumentException if the name is longer than 255 characters
	 * @should return an empty list if no items are found
	 * @should not return retired items unless specified
	 * @should return items that start with the specified name
	 * @should return items for all departments
	 */
	@Transactional(readOnly = true)
	@Authorized( {CashierPrivilegeConstants.VIEW_ITEMS})
	List<Item> findItems(String name, boolean includeRetired) throws APIException;

	/**
	 * Finds all items in the specified {@link Department} that start with the specified name.
	 * @param department The department to search within
	 * @param name The item name fragment
	 * @param includeRetired Whether retired item should be included in the results
	 * @return All items in the specified {@link Department} that start with the specified name.
	 * @throws APIException
	 * @should throw IllegalArgumentException if the department is null
	 * @should throw IllegalArgumentException if the name is null
	 * @should throw IllegalArgumentException if the name is empty
	 * @should throw IllegalArgumentException if the name is longer than 255 characters
	 * @should return an empty list if no items are found
	 * @should not return retired items unless specified
	 * @should return items that start with the specified name
	 * @should return items for only the specified department
	 */
	@Transactional(readOnly = true)
	@Authorized( {CashierPrivilegeConstants.VIEW_ITEMS})
	List<Item> findItems(Department department, String name, boolean includeRetired) throws APIException;
}

