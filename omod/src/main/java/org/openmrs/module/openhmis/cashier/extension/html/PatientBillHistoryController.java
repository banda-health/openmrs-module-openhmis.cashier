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

import org.openmrs.Patient;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.cashier.api.IBillService;
import org.openmrs.module.openhmis.cashier.api.impl.BillServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/module/openhmis/cashier/patientBillHistory")
public class PatientBillHistoryController {
	private IBillService billService;
	private PatientService patientService;

	@Autowired
	public PatientBillHistoryController(IBillService billServce, PatientService patientService) {
		this.billService = billServce;
		this.patientService = patientService;
	}

	@RequestMapping(method = RequestMethod.GET)
	public void billHistory(ModelMap model, @RequestParam(value = "patientId", required = true) Integer patientId) {
		Patient patient = null;
		String patientIdentifier = null;

		patient = patientService.getPatient(patientId);

	}
}
