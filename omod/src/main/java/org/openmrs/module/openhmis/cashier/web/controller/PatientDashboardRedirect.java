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
