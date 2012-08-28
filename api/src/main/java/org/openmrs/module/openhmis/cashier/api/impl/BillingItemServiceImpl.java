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

package org.openmrs.module.openhmis.cashier.api.impl;

import org.openmrs.annotation.Authorized;
import org.openmrs.api.APIException;
import org.openmrs.module.openhmis.cashier.api.IBillingItemService;
import org.openmrs.module.openhmis.cashier.api.db.IBillingItemDAO;
import org.openmrs.module.openhmis.cashier.api.model.Department;
import org.openmrs.module.openhmis.cashier.api.model.Item;
import org.openmrs.module.openhmis.cashier.api.util.CashierPrivilegeConstants;

import java.util.List;

public class BillingItemServiceImpl extends BaseMetadataServiceImpl<IBillingItemDAO, Item> implements IBillingItemService {
	/**
	 *
	 * @param item The {@link Item} to be saved to the database
	 * @return
	 * @throws APIException
	 * @should throw APIException if the item has no name
	 * @should throw APIException if the item has no department
	 * @should throw APIException if the item name is longer than 255 characters
	 * @should throw APIException if the item description is longer than 1024 characters
	 * @should throw APIException if the item has an item code that is already defined
	 */
	@Override
	@Authorized( {CashierPrivilegeConstants.MANAGE_ITEMS})
	public Item save(Item item) throws APIException {
		return null;
	}

	@Override
	public Item getItemByCode(String itemCode) throws APIException {
		return null;
	}

	/**
	 *
	 * @param name
	 * @param includeRetired Whether retired item should be included in the results.
	 * @return
	 * @should return items for all departments
	 */
	@Override
	public List<Item> findByName(String name, boolean includeRetired) {
		return null;
	}

	@Override
	public List<Item> findItems(Department department, String name, boolean includeRetired) throws APIException {
		return null;
	}
}
