package org.openmrs.module.openhmis.cashier.web.controller;

import java.util.Set;

import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/module/openhmis/cashier/bill.form")
public class BillAddEditController {
	
	@RequestMapping(method = RequestMethod.GET)
	public void bill(ModelMap model, @RequestParam(value = "patientId", required = false) Integer patientId) {
		Patient patient = null;
		String patientIdentifier = null;
		if (patientId != null) {
			PatientService service = Context.getPatientService();
			patient = service.getPatient(patientId);
			Set<PatientIdentifier> identifiers = patient.getIdentifiers();
			for (PatientIdentifier id : identifiers)
				if (id.getPreferred()) patientIdentifier = id.getIdentifier();
		}
		model.addAttribute("patient", patient);
		model.addAttribute("patientIdentifier", patientIdentifier);
	}

}
