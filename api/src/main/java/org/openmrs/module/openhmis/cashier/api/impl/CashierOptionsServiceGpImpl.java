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
package org.openmrs.module.openhmis.cashier.api.impl;

import org.apache.commons.lang.StringUtils;
import org.openmrs.api.APIException;
import org.openmrs.api.AdministrationService;
import org.openmrs.module.openhmis.cashier.api.ICashierOptionsService;
import org.openmrs.module.openhmis.cashier.api.model.CashierOptions;
import org.openmrs.module.openhmis.cashier.web.CashierWebConstants;
import org.openmrs.module.openhmis.inventory.api.IItemDataService;
import org.openmrs.module.openhmis.inventory.api.model.Item;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

/**
 * Service to load CashierOptions from global options
 * @author daniel
 *
 */
public class CashierOptionsServiceGpImpl implements ICashierOptionsService {

	private AdministrationService adminService;
	private IItemDataService itemService;

	@Autowired
	public CashierOptionsServiceGpImpl(AdministrationService adminService, IItemDataService itemService) {
		this.adminService = adminService;
		this.itemService = itemService;
	}

	/**
	 * Loads the cashier options from the database.
	 * @return The {@link CashierOptions}
	 * @should throw APIException if rounding is set but rounding item is not
	 * @should throw APIException if rounding is set but rounding item cannot be found
	 * @should not throw exception if numeric options are null
	 * @should default to false if timesheet required is not specified
	 * @should load cashier options from the database
	 */
	public CashierOptions getOptions() {
		CashierOptions options = new CashierOptions();

		setDefaultReceiptReportId(options);

		String temp = adminService.getGlobalProperty(CashierWebConstants.ROUNDING_MODE_PROPERTY);
		if (StringUtils.isNotEmpty(temp)) {
			try {
				options.setRoundingMode(CashierOptions.RoundingMode.valueOf(temp));

				temp = adminService.getGlobalProperty(CashierWebConstants.ROUND_TO_NEAREST_PROPERTY);
				if (StringUtils.isNotEmpty(temp)) {
					options.setRoundToNearest(new BigDecimal(temp));

					temp = adminService.getGlobalProperty(CashierWebConstants.ROUNDING_ITEM_ID);
					if (StringUtils.isNotEmpty(temp)) {
						try {
							Integer itemId = Integer.parseInt(temp);
							Item roundingItem = itemService.getById(itemId);

							options.setRoundingItemUuid(roundingItem.getUuid());
						} catch (Exception ex) {
							throw new APIException("Rounding item ID set in options but item not found. Maybe your user doesn't have the required rights or the item is not existent in the database.", ex);
						}
					} else {
						// Check to see if rounding has been enabled and throw exception if it has as a rounding item must be set
						if (options.getRoundToNearest() != null && !options.getRoundToNearest().equals(BigDecimal.ZERO)) {
							throw new APIException("Rounding enabled (nearest " + options.getRoundToNearest().toPlainString() +
									") but no rounding item ID specified in options.");
						}
					}
				}
			} catch (IllegalArgumentException iae) {
				/* Use default if option is not set */
			} catch (NullPointerException e) {
				/* Use default if option is not set */
			}
		}

		setRoundingOptions(options);
		setTimesheetOptions(options);

		return options;
	}

	private void setDefaultReceiptReportId(CashierOptions options) {
		String receiptReportIdProperty = adminService.getGlobalProperty(CashierWebConstants.RECEIPT_REPORT_ID_PROPERTY);
		if (StringUtils.isNotEmpty(receiptReportIdProperty)) {
			try {
				options.setDefaultReceiptReportId(Integer.parseInt(receiptReportIdProperty));
			} catch (NumberFormatException e) {
				/* Leave unset; must be handled, e.g. in ReceiptController */
			}
		}
	}

	private void setRoundingOptions(CashierOptions options) {
		if (options.getRoundingItemUuid() == null || options.getRoundingItemUuid().isEmpty()) {
			options.setRoundingMode(CashierOptions.RoundingMode.MID);
			options.setRoundToNearest(BigDecimal.ZERO);
		}
	}

	private void setTimesheetOptions(CashierOptions options) {
		String timesheetRequiredProperty = adminService.getGlobalProperty(CashierWebConstants.TIMESHEET_REQUIRED_PROPERTY);
		if (StringUtils.isNotBlank(timesheetRequiredProperty)) {
			try {
				options.setTimesheetRequired(Boolean.parseBoolean(timesheetRequiredProperty));
			} catch (Exception ex) {
				options.setTimesheetRequired(false);
			}
		} else {
			options.setTimesheetRequired(false);
		}
	}
}
