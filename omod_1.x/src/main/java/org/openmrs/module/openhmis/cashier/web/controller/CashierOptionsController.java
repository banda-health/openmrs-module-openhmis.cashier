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
package org.openmrs.module.openhmis.cashier.web.controller;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;
import org.openmrs.api.APIException;
import org.openmrs.api.AdministrationService;
import org.openmrs.module.openhmis.cashier.ModuleSettings;
import org.openmrs.module.openhmis.cashier.api.ICashierOptionsService;
import org.openmrs.module.openhmis.cashier.api.model.CashierOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller to manage the Cashier Options page.
 */
@Controller
@RequestMapping("/module/openhmis/cashier/options")
public class CashierOptionsController {
	private AdministrationService adminService;
	private ICashierOptionsService cashierOptionsService;

	@Autowired
	public CashierOptionsController(AdministrationService adminService, ICashierOptionsService cashierOptionsService) {
		this.adminService = adminService;
		this.cashierOptionsService = cashierOptionsService;
	}

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public CashierOptions options() {
		CashierOptions options = cashierOptionsService.getOptions();

		String roundingModeProperty = adminService.getGlobalProperty(ModuleSettings.ROUNDING_MODE_PROPERTY);
		String roundingItemId = adminService.getGlobalProperty(ModuleSettings.ROUNDING_ITEM_ID);
		if (StringUtils.isNotEmpty(roundingModeProperty)) {
			if (StringUtils.isEmpty(options.getRoundingItemUuid()) && StringUtils.isNotEmpty(roundingItemId)) {
				throw new APIException("Rounding item ID set in options but item not found. Make sure your user has the "
				        + "required rights and the item has the set ID in the database");
			}

			// Check to see if rounding has been enabled and throw exception if it has as a rounding item must be set
			if (StringUtils.isEmpty(roundingItemId) && options.getRoundToNearest() != null
			        && !options.getRoundToNearest().equals(BigDecimal.ZERO)) {
				throw new APIException("Rounding enabled (nearest " + options.getRoundToNearest().toString()
				        + ") but no rounding item ID specified in options.");
			}
		}

		return options;
	}
}
