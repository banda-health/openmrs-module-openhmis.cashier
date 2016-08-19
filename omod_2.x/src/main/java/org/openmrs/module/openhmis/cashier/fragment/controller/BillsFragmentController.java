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
package org.openmrs.module.openhmis.cashier.fragment.controller;

import java.util.List;

import org.openmrs.Patient;
import org.openmrs.module.openhmis.cashier.ModuleSettings;
import org.openmrs.module.openhmis.cashier.api.IBillService;
import org.openmrs.module.openhmis.cashier.api.model.Bill;
import org.openmrs.module.openhmis.commons.api.PagingInfo;
import org.openmrs.ui.framework.annotation.FragmentParam;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.fragment.FragmentModel;

/**
 * Spring OpenMRS 2.x Controller for Bills (bills.gsp) page
 */
public class BillsFragmentController {

	public void controller(FragmentModel model, @FragmentParam("patientId") Patient patient,
	        @SpringBean("cashierBillService") IBillService billService) {
		Integer numberConfiguredToShow = ModuleSettings.loadSettings().getPatientDashboard2BillCount();
		List<Bill> bills = billService.getBillsByPatient(patient, new PagingInfo(1, numberConfiguredToShow));

		model.addAttribute("bills", bills);
	}
}
