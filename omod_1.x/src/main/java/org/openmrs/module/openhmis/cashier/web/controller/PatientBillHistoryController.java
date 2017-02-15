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
package org.openmrs.module.openhmis.cashier.web.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.directwebremoting.util.Logger;
import org.openmrs.Location;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.cashier.ModuleSettings;
import org.openmrs.module.openhmis.cashier.api.IBillService;
import org.openmrs.module.openhmis.cashier.api.model.Bill;
import org.openmrs.util.OpenmrsConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller to manage the page to display patient's bills history.
 */
@Controller
@RequestMapping(value = "/module/openhmis/cashier/portlets/patientBillHistory")
public class PatientBillHistoryController {
	private static final Logger LOG = Logger.getLogger(PatientBillHistoryController.class);
	private IBillService billService;

	@Autowired
	public PatientBillHistoryController(IBillService billServce, PatientService patientService) {
		this.billService = billServce;
	}

	@RequestMapping(method = RequestMethod.GET)
	public void billHistory(ModelMap model, @RequestParam(value = "patientId", required = true) int patientId) {
		LOG.warn("In bill history controller");
		if (ModuleSettings.areItemsRestrictedByLocation()) {
			String location = Context.getAuthenticatedUser()
			        .getUserProperty(OpenmrsConstants.USER_PROPERTY_DEFAULT_LOCATION);
			Location locationTemp = Context.getLocationService().getLocation(Integer.parseInt(location));
			List<Bill> bills = billService.getBillsByPatientIdAndLocation(patientId, locationTemp, null);
			model.addAttribute("bills", bills);
		} else {
			List<Bill> bills = billService.getBillsByPatientId(patientId, null);
			model.addAttribute("bills", bills);
		}
	}
}
