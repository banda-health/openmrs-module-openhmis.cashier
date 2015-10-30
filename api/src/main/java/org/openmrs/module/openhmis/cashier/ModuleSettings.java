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
package org.openmrs.module.openhmis.cashier;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.jasperreport.JasperReport;
import org.openmrs.module.jasperreport.JasperReportService;
import org.openmrs.module.openhmis.cashier.api.model.CashierSettings;

/**
 * Helper class to load and save the inventory module global settings.
 */
public class ModuleSettings {
	public static final String RECEIPT_REPORT_ID_PROPERTY = "openhmis.cashier.defaultReceiptReportId";
	public static final String CASHIER_SHIFT_REPORT_ID_PROPERTY = "openhmis.cashier.defaultShiftReportId";
	public static final String TIMESHEET_REQUIRED_PROPERTY = "openhmis.cashier.timesheetRequired";
	public static final String ROUNDING_MODE_PROPERTY = "openhmis.cashier.roundingMode";
	public static final String ROUND_TO_NEAREST_PROPERTY = "openhmis.cashier.roundToNearest";
	public static final String ROUNDING_ITEM_ID = "openhmis.cashier.roundingItemId";
	public static final String ROUNDING_DEPT_ID = "openhmis.cashier.roundingDeptId";
	public static final String SYSTEM_RECEIPT_NUMBER_GENERATOR = "openhmis.cashier.systemReceiptNumberGenerator";
	public static final String ADJUSTMENT_REASEON_FIELD = "openhmis.cashier.adjustmentReasonField";
	public static final String ALLOW_BILL_ADJUSTMENT = "openhmis.cashier.allowBillAdjustments";
	public static final String AUTOFILL_PAYMENT_AMOUNT = "openhmis.cashier.autofillPaymentAmount";
	public static final String PATIENT_DASHBOARD_2_BILL_COUNT =
	        "openhmis.cashier.patientDashboard2BillCount";
	private static final Integer DEFAULT_PATIENT_DASHBOARD_2_BILL_COUNT = 10;//avoids '10' is a magic number.

	protected ModuleSettings() {}

	public static Integer getReceiptReportId() {
		AdministrationService administrationService = Context.getAdministrationService();
		String property = administrationService.getGlobalProperty(RECEIPT_REPORT_ID_PROPERTY);

		return Integer.parseInt(property);
	}

	public static JasperReport getReceiptReport() {
		JasperReport report = null;

		Integer reportId = getReceiptReportId();
		if (reportId != null) {
			JasperReportService reportService = Context.getService(JasperReportService.class);
			report = reportService.getJasperReport(reportId);
		}

		return report;
	}

	public static CashierSettings loadSettings() {
		CashierSettings cashierSettings = new CashierSettings();
		AdministrationService administrationService = Context.getAdministrationService();

		String property = administrationService.getGlobalProperty(ADJUSTMENT_REASEON_FIELD);
		if (!StringUtils.isEmpty(property)) {
			cashierSettings.setAdjustmentReasonField(Boolean.parseBoolean(property));
		} else {
			cashierSettings.setAdjustmentReasonField(Boolean.FALSE);
		}

		property = administrationService.getGlobalProperty(ALLOW_BILL_ADJUSTMENT);
		if (!StringUtils.isEmpty(property)) {
			cashierSettings.setAllowBillAdjustment(Boolean.parseBoolean(property));
		} else {
			cashierSettings.setAllowBillAdjustment(Boolean.FALSE);
		}

		property = administrationService.getGlobalProperty(AUTOFILL_PAYMENT_AMOUNT);
		if (!StringUtils.isEmpty(property)) {
			cashierSettings.setAutoFillPaymentAmount(Boolean.parseBoolean(property));
		} else {
			cashierSettings.setAutoFillPaymentAmount(Boolean.FALSE);
		}

		property = administrationService.getGlobalProperty(CASHIER_SHIFT_REPORT_ID_PROPERTY);
		if (!StringUtils.isEmpty(property)) {
			cashierSettings.setDefaultShitReportId(Integer.parseInt(property));
		}

		property = administrationService.getGlobalProperty(ROUND_TO_NEAREST_PROPERTY);
		if (!StringUtils.isEmpty(property)) {
			cashierSettings.setCashierRoundingToNearest(Integer.parseInt(property));
		}

		property = administrationService.getGlobalProperty(RECEIPT_REPORT_ID_PROPERTY);
		if (!StringUtils.isEmpty(property)) {
			cashierSettings.setDefaultReceiptReportId(Integer.parseInt(property));
		}

		property = administrationService.getGlobalProperty(ROUNDING_MODE_PROPERTY);
		if (!StringUtils.isEmpty(property)) {
			cashierSettings.setCashierRoundingMode(property);
		}

		property = administrationService.getGlobalProperty(TIMESHEET_REQUIRED_PROPERTY);
		if (!StringUtils.isEmpty(property)) {
			cashierSettings.setCashierTimesheetRequired(Boolean.parseBoolean(property));
		}

		property = administrationService.getGlobalProperty(PATIENT_DASHBOARD_2_BILL_COUNT);
		if (!StringUtils.isEmpty(property)) {
			cashierSettings.setPatientDashboard2BillCount(Integer.parseInt(property));
		} else {
			cashierSettings.setPatientDashboard2BillCount(DEFAULT_PATIENT_DASHBOARD_2_BILL_COUNT);
		}

		return cashierSettings;
	}

	public static void saveSettings(CashierSettings cashierSettings) {
		if (cashierSettings == null) {
			throw new IllegalArgumentException("The settings to save must be defined.");
		}

		AdministrationService adminService = Context.getAdministrationService();
		Boolean adjustmentReasonField = cashierSettings.getAdjustmentReasonField();
		if (Boolean.TRUE.equals(adjustmentReasonField)) {
			adminService.setGlobalProperty(ADJUSTMENT_REASEON_FIELD, Boolean.TRUE.toString());
		} else {
			adminService.setGlobalProperty(ADJUSTMENT_REASEON_FIELD, Boolean.FALSE.toString());
		}

		Boolean allowBillAdjustment = cashierSettings.getAllowBillAdjustment();
		if (Boolean.TRUE.equals(allowBillAdjustment)) {
			adminService.setGlobalProperty(ALLOW_BILL_ADJUSTMENT, Boolean.TRUE.toString());
		} else {
			adminService.setGlobalProperty(ALLOW_BILL_ADJUSTMENT, Boolean.FALSE.toString());
		}

		Boolean autofillPaymentAmount = cashierSettings.getAutoFillPaymentAmount();
		if (Boolean.TRUE.equals(autofillPaymentAmount)) {
			adminService.setGlobalProperty(AUTOFILL_PAYMENT_AMOUNT, Boolean.TRUE.toString());
		} else {
			adminService.setGlobalProperty(AUTOFILL_PAYMENT_AMOUNT, Boolean.FALSE.toString());
		}

		Integer shiftReportId = cashierSettings.getDefaultShitReportId();
		if (shiftReportId != null) {
			adminService.setGlobalProperty(CASHIER_SHIFT_REPORT_ID_PROPERTY, shiftReportId.toString());
		} else {
			adminService.setGlobalProperty(CASHIER_SHIFT_REPORT_ID_PROPERTY, "");
		}

		Integer roundToNearest = cashierSettings.getCashierRoundingToNearest();
		if (roundToNearest != null) {
			adminService.setGlobalProperty(ROUND_TO_NEAREST_PROPERTY, roundToNearest.toString());
		} else {
			adminService.setGlobalProperty(ROUND_TO_NEAREST_PROPERTY, "");
		}

		Integer receiptReport = cashierSettings.getDefaultReceiptReportId();
		if (receiptReport != null) {
			adminService.setGlobalProperty(RECEIPT_REPORT_ID_PROPERTY, receiptReport.toString());
		} else {
			adminService.setGlobalProperty(RECEIPT_REPORT_ID_PROPERTY, "");
		}

		String roundingMode = cashierSettings.getCashierRoundingMode();
		if (roundingMode != null) {
			adminService.setGlobalProperty(ROUNDING_MODE_PROPERTY, roundingMode);
		} else {
			adminService.setGlobalProperty(ROUNDING_MODE_PROPERTY, "");
		}

		Boolean timesheetRequiredProperty = cashierSettings.getCashierTimesheetRequired();
		if (Boolean.TRUE.equals(timesheetRequiredProperty)) {
			adminService.setGlobalProperty(TIMESHEET_REQUIRED_PROPERTY, Boolean.TRUE.toString());
		} else {
			adminService.setGlobalProperty(TIMESHEET_REQUIRED_PROPERTY, Boolean.FALSE.toString());
		}

	}
}
