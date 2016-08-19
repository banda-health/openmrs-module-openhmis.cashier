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
package org.openmrs.module.openhmis.cashier.web.filter;

import java.io.IOException;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Provider;
import org.openmrs.User;
import org.openmrs.api.ProviderService;
import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.cashier.api.ITimesheetService;
import org.openmrs.module.openhmis.cashier.api.model.Timesheet;
import org.openmrs.module.openhmis.cashier.api.util.PrivilegeConstants;
import org.openmrs.module.openhmis.commons.api.ProviderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Performs the neccessary filters when a cashier logs out. Implements {@link Filter}
 */
@Component
public class CashierLogoutFilter implements Filter {
	private static final Log LOG = LogFactory.getLog(CashierLogoutFilter.class);
	private static final String PROVIDER_ERROR_LOG_MESSAGE = "Could not locate the Provider";
	private static final Object TIMESHEET_ERROR_LOG_MESSAGE = "Could not locate Timesheet";

	@Autowired
	private ProviderService providerService;
	@Autowired
	private ITimesheetService timesheetService;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
	        ServletException {
		LOG.debug("doCashierLogoutFilter");
		clockOutCashier();
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void destroy() {

	}

	private void clockOutCashier() {
		if (!userIsCashier()) {
			return;
		}

		Provider provider = ProviderUtil.getCurrentProvider(providerService);
		if (provider == null) {
			LOG.error(PROVIDER_ERROR_LOG_MESSAGE);
			return;
		}

		Timesheet timesheet = timesheetService.getCurrentTimesheet(provider);
		if (timesheet == null) {
			LOG.error(TIMESHEET_ERROR_LOG_MESSAGE);
			return;
		}

		if (cashierIsClockedIn(timesheet)) {
			timesheet.setClockOut(new Date());
			timesheetService.save(timesheet);
		}
	}

	private boolean userIsCashier() {
		boolean result = false;
		User authenticatedUser = Context.getAuthenticatedUser();
		if (authenticatedUser != null) {
			result = authenticatedUser.hasPrivilege(PrivilegeConstants.MANAGE_TIMESHEETS);
		}

		return result;
	}

	private boolean cashierIsClockedIn(Timesheet timesheet) {
		return timesheet != null && timesheet.getClockIn() != null;
	}

}
