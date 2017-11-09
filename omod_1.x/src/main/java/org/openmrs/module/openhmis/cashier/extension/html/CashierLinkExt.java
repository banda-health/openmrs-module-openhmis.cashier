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
package org.openmrs.module.openhmis.cashier.extension.html;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Provider;
import org.openmrs.api.ProviderService;
import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.cashier.api.ITimesheetService;
import org.openmrs.module.openhmis.cashier.api.model.Timesheet;
import org.openmrs.module.openhmis.cashier.api.util.PrivilegeConstants;
import org.openmrs.module.openhmis.cashier.web.CashierWebConstants;
import org.openmrs.module.openhmis.commons.api.ProviderUtil;
import org.openmrs.module.openhmis.commons.api.util.UrlUtil;
import org.openmrs.module.web.extension.LinkExt;

/**
 * Link extension class to add a cashier link to OpenMRS.
 */
public class CashierLinkExt extends LinkExt {
	private static final Log LOG = LogFactory.getLog(CashierLinkExt.class);

	private ITimesheetService timesheetService;
	private ProviderService providerService;

	private Timesheet currentTimesheet;
	private boolean isProviderUser = false;

	@Override
	public void initialize(Map<String, String> parameterMap) {
		super.initialize(parameterMap);

		isProviderUser = false;
		currentTimesheet = null;

		if (Context.getAuthenticatedUser() != null
		        && Context.getAuthenticatedUser().hasPrivilege(org.openmrs.util.PrivilegeConstants.VIEW_PROVIDERS)) {
			try {
				this.timesheetService = Context.getService(ITimesheetService.class);
				this.providerService = Context.getProviderService();

				isProviderUser = false;
				if (Context.isAuthenticated()) {
					Provider provider = ProviderUtil.getCurrentProvider(providerService);
					if (provider != null) {
						isProviderUser = true;
						try {
							currentTimesheet = timesheetService.getCurrentTimesheet(provider);
						} catch (Exception e) {
							currentTimesheet = null;
						}
					}
				}
			} catch (Exception ex) {
				LOG.error("An error occurred while attempting to load the cashier extension point", ex);

				isProviderUser = false;
				currentTimesheet = null;
			}
		}
	}

	@Override
	public String getLabel() {
		if (isProviderUser) {
			return "openhmis.cashier.page";
		}

		return null;
	}

	public String getPortletUrl() {
		return null;
	}

	@Override
	public String getUrl() {
		return UrlUtil.formUrl(CashierWebConstants.CASHIER_PAGE);
	}

	@Override
	public String getRequiredPrivilege() {
		return PrivilegeConstants.MANAGE_BILLS;
	}
}
