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

import org.openmrs.api.APIException;
import org.openmrs.module.openhmis.cashier.api.IBillingItemService;
import org.openmrs.module.openhmis.cashier.api.db.IBillingItemDAO;
import org.openmrs.module.openhmis.cashier.api.model.Department;
import org.openmrs.module.openhmis.cashier.api.model.Item;

import java.util.List;

public class BillingItemServiceImpl extends BaseDaoServiceImpl<IBillingItemDAO> implements IBillingItemService {
	@Override
	public Item saveItem(Item item) throws APIException {
		return null;
	}

	@Override
	public Item retireLocation(Item item, String reason) throws APIException {
		return null;
	}

	@Override
	public Item unretireLocation(Item item) throws APIException {
		return null;
	}

	@Override
	public void purgeLocation(Item item) throws APIException {
	}

	@Override
	public Item getItem(int itemId) throws APIException {
		return null;
	}

	@Override
	public Item getItemByCode(String itemCode) throws APIException {
		return null;
	}

	@Override
	public List<Item> findItems(String name, boolean includeRetired) throws APIException {
		return null;
	}

	@Override
	public List<Item> findItems(Department department, String name, boolean includeRetired) throws APIException {
		return null;
	}
}
