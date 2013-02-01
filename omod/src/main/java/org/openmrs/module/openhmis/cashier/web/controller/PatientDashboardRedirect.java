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
package org.openmrs.module.openhmis.cashier.web.controller;

import org.openmrs.Patient;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/module/openhmis/cashier/patientDashboard")
public class PatientDashboardRedirect {
	@RequestMapping(method = RequestMethod.GET)
	public String items(@RequestParam(value = "patientUuid", required = true) String patientUuid) {
		PatientService service = Context.getPatientService();
		Patient patient = service.getPatientByUuid(patientUuid);
		return "redirect:/patientDashboard.form?patientId=" + patient.getId();
	}
}
