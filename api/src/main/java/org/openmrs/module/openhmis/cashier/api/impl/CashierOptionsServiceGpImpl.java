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
package org.openmrs.module.openhmis.cashier.api.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.AdministrationService;
import org.openmrs.module.openhmis.cashier.ModuleSettings;
import org.openmrs.module.openhmis.cashier.api.ICashierOptionsService;
import org.openmrs.module.openhmis.cashier.api.model.CashierOptions;
import org.openmrs.module.openhmis.inventory.api.IItemDataService;
import org.openmrs.module.openhmis.inventory.api.model.Item;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Service to load CashierOptions from global options
 * @author daniel
 */
public class CashierOptionsServiceGpImpl implements ICashierOptionsService {
	private static final Log LOG = LogFactory.getLog(CashierOptionsServiceGpImpl.class);

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
		setRoundingOptions(options);
		if (StringUtils.isEmpty(options.getRoundingItemUuid())) {
			setRoundingOptionsForEmptyUuid(options);
		}
		setTimesheetOptions(options);

		return options;
	}

	private void setRoundingOptions(CashierOptions options) {
		String roundingModeProperty = adminService.getGlobalProperty(ModuleSettings.ROUNDING_MODE_PROPERTY);
		if (StringUtils.isNotEmpty(roundingModeProperty)) {
			try {
				options.setRoundingMode(CashierOptions.RoundingMode.valueOf(roundingModeProperty));

				String roundToNearestProperty = adminService.getGlobalProperty(ModuleSettings.ROUND_TO_NEAREST_PROPERTY);
				if (StringUtils.isNotEmpty(roundToNearestProperty)) {
					options.setRoundToNearest(new Integer(roundToNearestProperty));

					String roundingItemId = adminService.getGlobalProperty(ModuleSettings.ROUNDING_ITEM_ID);
					if (StringUtils.isNotEmpty(roundingItemId)) {
						Item roundingItem = null;
						try {
							Integer itemId = Integer.parseInt(roundingItemId);
							roundingItem = itemService.getById(itemId);
						} catch (Exception e) {
							LOG.error("Did not find rounding item by ID with ID <" + roundingItemId + ">", e);
						}
						if (roundingItem != null) {
							options.setRoundingItemUuid(roundingItem.getUuid());
						} else {
							LOG.error("Rounding item is NULL. Check your ID");
						}
					}
				}
			} catch (IllegalArgumentException iae) {
				/* Use default if option is not set */
				LOG.error("IllegalArgumentException occured", iae);
			} catch (NullPointerException e) {
				/* Use default if option is not set */
				LOG.error("NullPointerException occured", e);
			}
		}
	}

	private void setDefaultReceiptReportId(CashierOptions options) {
		String receiptReportIdProperty = adminService.getGlobalProperty(ModuleSettings.RECEIPT_REPORT_ID_PROPERTY);
		if (StringUtils.isNotEmpty(receiptReportIdProperty)) {
			try {
				options.setDefaultReceiptReportId(Integer.parseInt(receiptReportIdProperty));
			} catch (NumberFormatException e) {
				/* Leave unset; must be handled, e.g. in ReceiptController */
				LOG.error("Error parsing ReceiptReportId <" + receiptReportIdProperty + ">", e);
			}
		}
	}

	private void setRoundingOptionsForEmptyUuid(CashierOptions options) {
		options.setRoundingMode(CashierOptions.RoundingMode.MID);
		options.setRoundToNearest(0);
	}

	private void setTimesheetOptions(CashierOptions options) {
		String timesheetRequiredProperty = adminService.getGlobalProperty(ModuleSettings.TIMESHEET_REQUIRED_PROPERTY);
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
