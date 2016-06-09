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
 *
 */

package org.openmrs.module.openhmis.cashier.web.controller;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.api.AdministrationService;
import org.openmrs.module.openhmis.cashier.ModuleSettings;
import org.openmrs.module.openhmis.cashier.web.CashierWebConstants;
import org.openmrs.module.webservices.rest.SimpleObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Type of a RestController to check up setting values in the Cashier Module Settings.
 */
@Controller(value = "moduleSettings")
@RequestMapping(CashierWebConstants.MODULE_SETTINGS_PAGE)
public class ModuleSettingsController {

	private AdministrationService adminService;

	@Autowired
	public ModuleSettingsController(AdministrationService adminService) {
		this.adminService = adminService;
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET)
	public SimpleObject get(@RequestParam("setting") String setting) {
		SimpleObject results = new SimpleObject();
		if (StringUtils.isNotEmpty(setting)) {
			if (StringUtils.equalsIgnoreCase(setting, ModuleSettings.CASHIER_SHIFT_REPORT_ID_PROPERTY)) {
				results.put("results", adminService.getGlobalProperty(ModuleSettings.CASHIER_SHIFT_REPORT_ID_PROPERTY));
			}
			// TODO: check other settings
		}
		return results;
	}
}