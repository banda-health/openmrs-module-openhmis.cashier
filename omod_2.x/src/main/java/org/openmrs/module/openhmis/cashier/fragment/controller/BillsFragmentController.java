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

import java.util.ArrayList;
import java.util.List;

import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.cashier.api.IBillService;
import org.openmrs.module.openhmis.cashier.api.model.Bill;
import org.openmrs.ui.framework.annotation.FragmentParam;
import org.openmrs.ui.framework.fragment.FragmentModel;

public class BillsFragmentController {
	public void controller(FragmentModel model, @FragmentParam("patientId") Patient patient) {
		IBillService iBillService = Context.getService(IBillService.class);
		List<Bill> bills = iBillService.getBillsByPatient(patient, null);
		List<Bill> billsToReturn = new ArrayList<Bill>();
		Integer numberConfiguredToShow =
		        Integer.parseInt(Context.getAdministrationService().getGlobalProperty(
		            "openhmis.cashier.numberOfBillsFor2xPatientDashboard"));
		Integer numberToShow = numberConfiguredToShow >= bills.size() ? bills.size() : numberConfiguredToShow;

		for (int i = 0; i < numberToShow; i++) {
			billsToReturn.add(bills.get(i));
		}

		model.addAttribute("bills", billsToReturn);
	}
}
