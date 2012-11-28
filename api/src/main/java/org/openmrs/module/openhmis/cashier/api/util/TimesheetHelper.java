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
package org.openmrs.module.openhmis.cashier.api.util;

import org.openmrs.Provider;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.ProviderService;
import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.cashier.api.ITimesheetService;
import org.openmrs.module.openhmis.cashier.api.model.Timesheet;
import org.openmrs.module.openhmis.cashier.web.CashierWebConstants;

public class TimesheetHelper {
	public static Timesheet getCurrentTimesheet() throws TimesheetRequiredException {
		Provider provider = null;
		Timesheet timesheet = null;
		ProviderService providerService = Context.getProviderService();
		try {
			provider = ProviderHelper.getCurrentProvider(providerService);
		} catch (Exception e) {
			throw new RuntimeException("Error retrieving provider for current user.", e);
		}
		
		ITimesheetService tsService = Context.getService(ITimesheetService.class);
		try {
			timesheet = tsService.getCurrentTimesheet(provider);
		} catch (Exception e) {
			if (TimesheetHelper.isTimesheetRequired())
				throw new TimesheetRequiredException();
			else
				return null;
		}
		if (TimesheetHelper.isTimesheetRequired() && timesheet == null)
			throw new TimesheetRequiredException();
		return timesheet;
	}
	
	public static boolean isTimesheetRequired() {
		AdministrationService adminService = Context.getAdministrationService();
		boolean timesheetRequired;
		try {
			timesheetRequired = Boolean.parseBoolean(adminService.getGlobalProperty(CashierWebConstants.TIMESHEET_REQUIRED_PROPERTY));
		} catch (Exception e2) {
			timesheetRequired = false;
		}
		return timesheetRequired;
	}
}
