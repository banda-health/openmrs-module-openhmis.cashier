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
package org.openmrs.module.openhmis.cashier.extension.html;

import org.openmrs.module.Extension;
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
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		map.put("/module/openhmis/cashier/role.form", "openhmis.cashier.admin.role");
		map.put("/module/openhmis/cashier/departments.form", "openhmis.cashier.admin.departments");
		map.put("/module/openhmis/cashier/cashPoints.form", "openhmis.cashier.admin.cashPoints");
		map.put("/module/openhmis/cashier/paymentModes.form", "openhmis.cashier.admin.paymentModes");
		map.put("/module/openhmis/cashier/items.form", "openhmis.cashier.admin.items");
		map.put("/module/openhmis/cashier/admin/receiptNumberGenerator.form", "openhmis.cashier.admin.receiptNumberGenerator");
		return map;
	}
	
}
