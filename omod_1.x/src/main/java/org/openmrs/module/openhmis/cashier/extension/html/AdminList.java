/*
 * The contents of this file are subject to the OpenMRS Public License
 * Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See
 * the License for the specific language governing rights and
 * limitations under the License.
 *
 * Copyright (C) OpenHMIS.  All Rights Reserved.
 */
package org.openmrs.module.openhmis.cashier.extension.html;

import java.util.LinkedHashMap;
import java.util.Map;

import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.module.Extension;
import org.openmrs.module.openhmis.cashier.api.util.PrivilegeConstants;
import org.openmrs.module.openhmis.cashier.web.CashierWebConstants;
import org.openmrs.module.openhmis.cashier.web.PrivilegeWebConstants;
import org.openmrs.module.web.extension.AdministrationSectionExt;

/**
 * This class defines the links that will appear on the administration page under the "openhmis.cashier.title" heading.
 */
public class AdminList extends AdministrationSectionExt {

	/**
	 * @see AdministrationSectionExt#getMediaType()
	 */
	public Extension.MEDIA_TYPE getMediaType() {
		return Extension.MEDIA_TYPE.html;
	}

	/**
	 * @see AdministrationSectionExt#getTitle()
	 */
	public String getTitle() {
		return "openhmis.cashier.title";
	}

	@Override
	public String getRequiredPrivilege() {
		return PrivilegeConstants.MANAGE_BILLS;
	}

	/**
	 * @see AdministrationSectionExt#getLinks()
	 */
	public Map<String, String> getLinks() {
		User authenticatedUser = Context.getAuthenticatedUser();
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		if (authenticatedUser.hasPrivilege(PrivilegeConstants.MANAGE_METADATA)) {
			map.put(CashierWebConstants.CASHIER_ROLE_PAGE, "openhmis.cashier.admin.role");
			map.put(CashierWebConstants.CASH_POINTS_PAGE, "openhmis.cashier.admin.cashPoints");
			map.put(CashierWebConstants.PAYMENT_MODES_PAGE, "openhmis.cashier.admin.paymentModes");
		}

		if (authenticatedUser.hasPrivilege(PrivilegeConstants.MANAGE_BILLS)) {
			map.put(CashierWebConstants.RECEIPT_NUMBER_GENERATOR_PAGE, "openhmis.cashier.admin.receiptNumberGenerator");
		}

		if (authenticatedUser.hasPrivilege(PrivilegeWebConstants.SETTING_PAGE_PRIVILEGE)) {
			map.put(CashierWebConstants.CASHIER_SETTINGS_PAGE, "openhmis.cashier.admin.cashierSettings");
		}

		return map;
	}

}
