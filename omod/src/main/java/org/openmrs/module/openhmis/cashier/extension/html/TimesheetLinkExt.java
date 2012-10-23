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

import org.openmrs.Provider;
import org.openmrs.api.ProviderService;
import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.cashier.api.ITimesheetService;
import org.openmrs.module.openhmis.cashier.api.model.Timesheet;
import org.openmrs.module.openhmis.cashier.api.util.CashierPrivilegeConstants;
import org.openmrs.module.openhmis.cashier.api.util.ProviderHelper;
import org.openmrs.module.openhmis.cashier.web.CashierWebConstants;
import org.openmrs.module.web.extension.LinkExt;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class TimesheetLinkExt extends LinkExt {
	private ITimesheetService timesheetService;
	private ProviderService providerService;

	private Timesheet currentTimesheet;
	private boolean isProviderUser = false;

	@Override
	public void initialize(Map<String, String> parameterMap) {
		super.initialize(parameterMap);

		this.timesheetService = Context.getService(ITimesheetService.class);
		this.providerService = Context.getProviderService();

		isProviderUser = false;
		if (Context.isAuthenticated()) {
			Provider provider = ProviderHelper.getCurrentProvider(providerService);
			if (provider != null) {
				isProviderUser = true;
				try {
					currentTimesheet = timesheetService.getCurrentTimesheet(provider);
				} catch (Exception e) {
					currentTimesheet = null;
				}
			}
		}

	}

	@Override
	public String getLabel() {
		if (isProviderUser) {
			if (currentTimesheet == null) {
				return "openhmis.cashier.timesheet.clockIn";
			} else {
				return "openhmis.cashier.timesheet.clockOut";
			}
		}

		return null;
	}

	public String getPortletUrl() {
		return null;
	}

	@Override
	public String getUrl() {
		String url = CashierWebConstants.formUrl(CashierWebConstants.TIMESHEET_ENTRY_PAGE);

		HttpServletRequest req = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		if (req != null) {
			url += "?returnUrl=" + req.getAttribute("javax.servlet.forward.servlet_path");
		}

		return url;
	}

	@Override
	public String getRequiredPrivilege() {
		return CashierPrivilegeConstants.MANAGE_BILLS;
	}
}
