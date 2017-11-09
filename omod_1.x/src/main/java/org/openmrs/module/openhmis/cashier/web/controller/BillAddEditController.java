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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.api.APIException;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.cashier.ModuleSettings;
import org.openmrs.module.openhmis.cashier.api.IBillService;
import org.openmrs.module.openhmis.cashier.api.ICashierOptionsService;
import org.openmrs.module.openhmis.cashier.api.model.Bill;
import org.openmrs.module.openhmis.cashier.api.model.CashierOptions;
import org.openmrs.module.openhmis.cashier.api.model.Timesheet;
import org.openmrs.module.openhmis.cashier.api.util.PrivilegeConstants;
import org.openmrs.module.openhmis.cashier.api.util.TimesheetUtil;
import org.openmrs.module.openhmis.cashier.web.CashierWebConstants;
import org.openmrs.module.openhmis.commons.api.util.UrlUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Set;

/**
 * Controller to manage the Bill page.
 */
@Controller
@RequestMapping(value = CashierWebConstants.BILL_PAGE)
public class BillAddEditController {

	private static final Log LOG = LogFactory.getLog(BillAddEditController.class);

	private AdministrationService adminService;
	private ICashierOptionsService cashOptService;

	@Autowired
	public BillAddEditController(AdministrationService adminService, ICashierOptionsService cashOptService) {
		this.adminService = adminService;
		this.cashOptService = cashOptService;
	}

	@RequestMapping(method = RequestMethod.GET)
	public String bill(ModelMap model, @RequestParam(value = "billUuid", required = false) String billUuid,
	        @RequestParam(value = "patientUuid", required = false) String patientUuid, HttpServletRequest request) {
		Timesheet timesheet = null;
		try {
			timesheet = TimesheetUtil.getCurrentTimesheet();
		} catch (Exception e) {
			LOG.error("Error retrieving provider for current user. ", e);
			timesheet = null;
			return "redirect:/login.htm";

		}

		if (timesheet == null && TimesheetUtil.isTimesheetRequired()) {
			return buildRedirectUrl(request);
		}

		model.addAttribute("timesheet", timesheet);
		model.addAttribute("user", Context.getAuthenticatedUser());
		model.addAttribute("url", buildUrlModelAttribute(request));

		boolean showAdjustmentReasonField = Boolean.parseBoolean(adminService.getGlobalProperty(
		        ModuleSettings.ADJUSTMENT_REASEON_FIELD));
		model.addAttribute("showAdjustmentReasonField", showAdjustmentReasonField);

		boolean allowBillAdjustment = Boolean.parseBoolean(adminService.getGlobalProperty(
		        ModuleSettings.ALLOW_BILL_ADJUSTMENT));
		model.addAttribute("allowBillAdjustment", allowBillAdjustment);

		boolean autofillPaymentAmount = Boolean.parseBoolean(adminService.getGlobalProperty(
		        ModuleSettings.AUTOFILL_PAYMENT_AMOUNT));
		model.addAttribute("autofillPaymentAmount", autofillPaymentAmount);

		CashierOptions options = cashOptService.getOptions();
		String roundingItemUuid = options.getRoundingItemUuid();
		model.addAttribute("roundingItemUuid", roundingItemUuid);

		if (billUuid != null) {
			handleExistingBill(model, billUuid);
		} else {
			addPatientAttributes(model, patientUuid);
			model.addAttribute("showPrint", true);
			model.addAttribute("cashPoint", timesheet != null ? timesheet.getCashPoint() : null);
		}

		return CashierWebConstants.BILL_PAGE;
	}

	private void handleExistingBill(ModelMap model, String billUuid) {
		Bill bill = getBillFromService(billUuid);
		if (bill != null) {
			Patient patient = bill.getPatient();
			addBillAttributes(model, bill, patient);
		}
	}

	private void addPatientAttributes(ModelMap model, String patientUuid) {
		if (patientUuid != null) {
			Patient patient = getPatientFromService(patientUuid);

			String patientIdentifier = null;
			if (patient != null) {
				patientIdentifier = getPreferedPatientIdentifier(patient);
			}
			model.addAttribute("patient", patient);
			model.addAttribute("patientIdentifier", patientIdentifier);
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
		Patient patient;
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
		model.addAttribute("adjustmentReason", bill.getAdjustmentReason());
		if (!bill.isReceiptPrinted()
		        || (bill.isReceiptPrinted() && Context.hasPrivilege(PrivilegeConstants.REPRINT_RECEIPT))) {
			model.addAttribute("showPrint", true);
		}
	}

	private Bill getBillFromService(String billUuid) {
		IBillService service = Context.getService(IBillService.class);
		Bill bill;

		try {
			bill = service.getByUuid(billUuid);
		} catch (APIException e) {
			LOG.error("Error when trying to get bill with ID <" + billUuid + ">", e);
			throw new APIException("Error when trying to get bill with ID <" + billUuid + ">");
		}

		return bill;
	}

	private String buildUrlModelAttribute(HttpServletRequest request) {
		return UrlUtil.formUrl(CashierWebConstants.BILL_PAGE) + ((request.getQueryString() != null)
		        ? "?" + request.getQueryString() : "");
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
