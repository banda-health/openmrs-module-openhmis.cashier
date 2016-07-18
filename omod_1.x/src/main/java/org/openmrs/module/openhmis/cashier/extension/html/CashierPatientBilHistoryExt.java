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

import org.openmrs.module.openhmis.cashier.api.util.PrivilegeConstants;
import org.openmrs.module.web.extension.PatientDashboardTabExt;

/**
 * Link extension class to add a patient's bill history on to the patient dashboard
 */
public class CashierPatientBilHistoryExt extends PatientDashboardTabExt {
	@Override
	public String getTabName() {
		return "openhmis.cashier.patient.bill.history";
	}

	@Override
	public String getTabId() {
		return "openhmis.cashier.bill.history";
	}

	@Override
	public String getRequiredPrivilege() {
		return PrivilegeConstants.VIEW_BILLS;
	}

	@Override
	public String getPortletUrl() {
		return "patientBillHistory";
	}
}
