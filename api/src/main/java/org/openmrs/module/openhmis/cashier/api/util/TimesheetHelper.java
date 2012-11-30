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
