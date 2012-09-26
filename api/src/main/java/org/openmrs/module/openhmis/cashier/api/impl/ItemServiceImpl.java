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

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.openmrs.annotation.Authorized;
import org.openmrs.api.APIException;
import org.openmrs.module.openhmis.cashier.api.IItemService;
import org.openmrs.module.openhmis.cashier.api.IMetadataAuthorizationPrivileges;
import org.openmrs.module.openhmis.cashier.api.model.Department;
import org.openmrs.module.openhmis.cashier.api.model.Item;
import org.openmrs.module.openhmis.cashier.api.util.CashierPrivilegeConstants;
import org.openmrs.module.openhmis.cashier.api.util.PagingInfo;

import java.util.List;

public class ItemServiceImpl
		extends BaseMetadataServiceImpl<Item>
		implements IItemService, IMetadataAuthorizationPrivileges {
	public static final int NAME_FIELD_LENGTH = 255;
	public static final int DESC_FIELD_SIZE = 1024;

	/**
	 * Validates the entity.
	 * @param entity The {@link Item} to be validated
	 * @return
	 * @throws APIException
	 * @should throw APIException if the item has no name
	 * @should throw APIException if the item has no department
	 * @should throw APIException if the item name is longer than 255 characters
	 * @should throw APIException if the item description is longer than 1024 characters
	 * @should throw APIException if the item has an item code that is already defined
	 */
	@Override
	protected void validate(Item entity) throws APIException {
		return;
	}

	@Override
	@Authorized( { CashierPrivilegeConstants.VIEW_ITEMS } )
	public Item getItemByCode(String itemCode) throws APIException {
		if (StringUtils.isEmpty(itemCode)) {
			throw new IllegalArgumentException("The item code must be defined.");
		}
		if (itemCode.length() > 255) {
			throw new IllegalArgumentException("The item code must be less than 256 characters.");
		}

		Criteria criteria = dao.createCriteria(getEntityClass());
		criteria.createAlias("codes", "c")
				.add(Restrictions.ilike("c.code", itemCode));

		return dao.selectSingle(getEntityClass(), criteria);
	}

	@Override
	@Authorized( { CashierPrivilegeConstants.VIEW_ITEMS } )
	public List<Item> findItems(Department department, String name, boolean includeRetired) throws APIException {
		return findItems(department, name, includeRetired, null);
	}
	
	@Override
	@Authorized( { CashierPrivilegeConstants.VIEW_ITEMS } )
	public List<Item> findItems(Department department, String name, boolean includeRetired, PagingInfo pagingInfo) throws APIException {
		if (department == null) {
			throw new NullPointerException("The department must be defined");
		}
		if (StringUtils.isEmpty(name)) {
			throw new IllegalArgumentException("The item code must be defined.");
		}
		if (name.length() > 255) {
			throw new IllegalArgumentException("The item code must be less than 256 characters.");
		}

		Criteria criteria = dao.createCriteria(getEntityClass());
		criteria.add(Restrictions.eq("department", department))
				.add(Restrictions.ilike("name", name, MatchMode.START));

		if (!includeRetired) {
			criteria.add(Restrictions.eq("retired", false));
		}

		loadPagingTotal(pagingInfo, criteria);
		return dao.select(getEntityClass(), criteria);
	}

	@Override
	protected IMetadataAuthorizationPrivileges getPrivileges() {
		return this;
	}

	@Override
	public String getSavePrivilege() {
		return CashierPrivilegeConstants.MANAGE_ITEMS;
	}

	@Override
	public String getPurgePrivilege() {
		return CashierPrivilegeConstants.PURGE_ITEMS;
	}

	@Override
	public String getGetPrivilege() {
		return CashierPrivilegeConstants.VIEW_ITEMS;
	}

	@Override
	public String getRetirePrivilege() {
		return CashierPrivilegeConstants.MANAGE_ITEMS;
	}
}
