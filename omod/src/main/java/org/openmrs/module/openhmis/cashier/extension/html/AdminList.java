/*
 * The contents of this file are subject to the OpenMRS Public License
 * Version 2.0 (the "License"); you may not use this file except in
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
package org.openmrs.module.openhmis.cashier.extension.html;

import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.module.Extension;
import org.openmrs.module.openhmis.cashier.api.util.CashierPrivilegeConstants;
import org.openmrs.module.web.extension.AdministrationSectionExt;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This class defines the links that will appear on the administration page under the
 * "openhmis.cashier.title" heading. 
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
	
	/**
	 * @see AdministrationSectionExt#getLinks()
	 */
	public Map<String, String> getLinks() {
        User authenticatedUser = Context.getAuthenticatedUser();
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        if(authenticatedUser.hasPrivilege(CashierPrivilegeConstants.MANAGE_METADATA)) {
            map.put("/module/openhmis/cashier/cashierRole.form", "openhmis.cashier.admin.role");
        }

        if((authenticatedUser.hasPrivilege(CashierPrivilegeConstants.VIEW_METADATA)) && (authenticatedUser.hasPrivilege(CashierPrivilegeConstants.MANAGE_METADATA))) {
            map.put("/module/openhmis/cashier/cashPoints.form", "openhmis.cashier.admin.cashPoints");
            map.put("/module/openhmis/cashier/paymentModes.form", "openhmis.cashier.admin.paymentModes");
        }

		if(authenticatedUser.hasPrivilege(CashierPrivilegeConstants.MANAGE_BILLS)) {
            map.put("/module/openhmis/cashier/admin/receiptNumberGenerator.form", "openhmis.cashier.admin.receiptNumberGenerator");
        }

		return map;
	}
	
}
