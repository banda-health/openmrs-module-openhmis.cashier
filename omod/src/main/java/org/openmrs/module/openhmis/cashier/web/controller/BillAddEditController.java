/*
 * The contents of this file are subject to the OpenMRS Public License
 * Version 2.0 (the "License"); you may not use this file except in
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

import java.io.UnsupportedEncodingException;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.api.APIException;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.cashier.api.IBillService;
import org.openmrs.module.openhmis.cashier.api.model.Bill;
import org.openmrs.module.openhmis.cashier.api.model.Timesheet;
import org.openmrs.module.openhmis.cashier.api.util.CashierPrivilegeConstants;
import org.openmrs.module.openhmis.cashier.api.util.TimesheetHelper;
import org.openmrs.module.openhmis.cashier.web.CashierWebConstants;
import org.openmrs.module.openhmis.commons.api.util.UrlUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;

@Controller
@RequestMapping(value = CashierWebConstants.BILL_PAGE)
public class BillAddEditController {

    private static final Log LOG = LogFactory.getLog(BillAddEditController.class);

	@RequestMapping(method = RequestMethod.GET)
	public String bill(ModelMap model,
			@RequestParam(value = "billUuid", required = false) String billUuid,
			@RequestParam(value = "patientUuid", required = false) String patientUuid,
			HttpServletRequest request) {

	    Timesheet timesheet = null;
		try {
			timesheet = TimesheetHelper.getCurrentTimesheet();
		} catch (Exception e) {
			LOG.error("An exception occured: ", e);
			timesheet = null;
		}

        if (timesheet == null && TimesheetHelper.isTimesheetRequired()) {
           return buildRedirectUrl(request);
        }

		model.addAttribute("timesheet", timesheet);
		model.addAttribute("user", Context.getAuthenticatedUser());
		model.addAttribute("url", buildUrlModelAttribute(request));

		if (billUuid != null) {
			Bill bill = getBillFromService(billUuid);
			if (bill != null) {
			    Patient patient = bill.getPatient();
			    addBillAttributes(model, bill, patient);
			}
			return CashierWebConstants.BILL_PAGE;
		} else {
			if (patientUuid != null) {
				Patient patient = getPatientFromService(patientUuid);

				String patientIdentifier = null;
				if (patient != null) {
				    patientIdentifier = getPreferedPatientIdentifier(patient);
				}
				model.addAttribute("patient", patient);
				model.addAttribute("patientIdentifier", patientIdentifier);
			}
			model.addAttribute("showPrint", true);
			model.addAttribute("cashPoint", timesheet != null ? timesheet.getCashPoint() : null);
			return CashierWebConstants.BILL_PAGE;
		}
	}

    private String getPreferedPatientIdentifier(Patient patient) {
        String patientIdentifier = null;
        Set<PatientIdentifier> identifiers = patient.getIdentifiers();
        for (PatientIdentifier id : identifiers) {
            if (id.getPreferred()) {
                patientIdentifier = id.getIdentifier();
            }
        }
        return patientIdentifier;
    }

    private Patient getPatientFromService(String patientUuid) {
        PatientService service = Context.getPatientService();
        Patient patient = null;
        try {
            patient = service.getPatientByUuid(patientUuid);
        } catch (APIException e) {
            LOG.error("Error when trying to get Patient with ID <" + patientUuid + ">", e);
            throw new APIException("Error when trying to get Patient with ID <" + patientUuid + ">");
        }
        return patient;
    }

    private void addBillAttributes(ModelMap model, Bill bill, Patient patient) {
        model.addAttribute("bill", bill);
        model.addAttribute("billAdjusted", bill.getBillAdjusted());
        model.addAttribute("adjustedBy", bill.getAdjustedBy());
        model.addAttribute("patient", patient);
        model.addAttribute("cashPoint", bill.getCashPoint());
        model.addAttribute("adjustmentReason",bill.getAdjustmentReason());
        if (!bill.isReceiptPrinted() || (bill.isReceiptPrinted() && Context.hasPrivilege(CashierPrivilegeConstants.REPRINT_RECEIPT))) {
            model.addAttribute("showPrint", true);
        }
    }

    private Bill getBillFromService(String billUuid) {
        IBillService service = Context.getService(IBillService.class);
        Bill bill = null;
        try {
            bill = service.getByUuid(billUuid);
        } catch (APIException e) {
            LOG.error("Error when trying to get bill with ID <" + billUuid + ">", e);
            throw new APIException("Error when trying to get bill with ID <" + billUuid + ">");
        }
        return bill;
    }

    private String buildUrlModelAttribute(HttpServletRequest request) {
        return UrlUtil.formUrl(CashierWebConstants.BILL_PAGE)
                + ( (request.getQueryString() != null) ? "?" + request.getQueryString() : "");
    }

    private String buildRedirectUrl(HttpServletRequest request) {
        String redirectUrl = "redirect:" + UrlUtil.formUrl(CashierWebConstants.CASHIER_PAGE);
        String returnUrlParam = "?returnUrl=" + UrlUtil.formUrl(CashierWebConstants.BILL_PAGE);
        String requestQueryParam = "";
        if (request.getQueryString() != null) {
            requestQueryParam = encodeRequestQuery(request);
        }
        return redirectUrl + returnUrlParam + requestQueryParam;
    }

    private String encodeRequestQuery(HttpServletRequest request) {
        String requestQueryParam = "";
        try {
            requestQueryParam = UriUtils.encodeQuery("?" + request.getQueryString(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            LOG.error("UnsupportedEncodingException occured when trying to encode request query", e);
        }
        return requestQueryParam;
    }
}