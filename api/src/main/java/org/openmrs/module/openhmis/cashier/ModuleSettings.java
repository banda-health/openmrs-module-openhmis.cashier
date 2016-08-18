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
import org.apache.commons.lang3.math.NumberUtils;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.jasperreport.JasperReport;
import org.openmrs.module.jasperreport.JasperReportService;
import org.openmrs.module.openhmis.cashier.api.model.CashierSettings;
import org.openmrs.module.openhmis.commons.api.f.Action1;

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
	private static final Integer DEFAULT_PATIENT_DASHBOARD_2_BILL_COUNT = 4;
	public static final String DEPARTMENT_COLLECTIONS_REPORT_ID_PROPERTY = "openhmis.cashier.reports.departmentCollections";
	public static final String DEPARTMENT_REVENUE_REPORT_ID_PROPERTY = "openhmis.cashier.reports.departmentRevenue";
	public static final String SHIFT_SUMMARY_REPORT_ID_PROPERTY = "openhmis.cashier.reports.shiftSummary";
	public static final String DAILY_SHIFT_SUMMARY_REPORT_ID_PROPERTY = "openhmis.cashier.reports.dailyShiftSummary";
	public static final String PAYMENTS_BY_PAYMENT_MODE_REPORT_ID_PROPERTY =
	        "openhmis.cashier.reports.paymentsByPaymentMode";

	private static AdministrationService administrationService;

	static {
		administrationService = Context.getAdministrationService();
	}

	protected ModuleSettings() {}

	public static Integer getReceiptReportId() {
		return getIntProperty(RECEIPT_REPORT_ID_PROPERTY);
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
		final CashierSettings cashierSettings = new CashierSettings();

		getBoolProperty(ADJUSTMENT_REASEON_FIELD, Boolean.FALSE, new Action1<Boolean>() {
			@Override
			public void apply(Boolean parameter) {
				cashierSettings.setAdjustmentReasonField(parameter);
			}
		});

		getBoolProperty(ALLOW_BILL_ADJUSTMENT, Boolean.FALSE, new Action1<Boolean>() {
			@Override
			public void apply(Boolean parameter) {
				cashierSettings.setAllowBillAdjustment(parameter);
			}
		});

		getBoolProperty(AUTOFILL_PAYMENT_AMOUNT, Boolean.FALSE, new Action1<Boolean>() {
			@Override
			public void apply(Boolean parameter) {
				cashierSettings.setAutoFillPaymentAmount(parameter);
			}
		});

		getIntProperty(ROUND_TO_NEAREST_PROPERTY, new Action1<Integer>() {
			@Override
			public void apply(Integer parameter) {
				cashierSettings.setCashierRoundingToNearest(parameter);
			}
		});

		getIntProperty(CASHIER_SHIFT_REPORT_ID_PROPERTY, new Action1<Integer>() {
			@Override
			public void apply(Integer parameter) {
				cashierSettings.setDefaultShiftReportId(parameter);
			}
		});

		getIntProperty(RECEIPT_REPORT_ID_PROPERTY, new Action1<Integer>() {
			@Override
			public void apply(Integer parameter) {
				cashierSettings.setDefaultReceiptReportId(parameter);
			}
		});

		String property = administrationService.getGlobalProperty(ROUNDING_MODE_PROPERTY);
		if (!StringUtils.isEmpty(property)) {
			cashierSettings.setCashierRoundingMode(property);
		}

		getBoolProperty(TIMESHEET_REQUIRED_PROPERTY, new Action1<Boolean>() {
			@Override
			public void apply(Boolean parameter) {
				cashierSettings.setCashierTimesheetRequired(parameter);
			}
		});

		getIntProperty(PATIENT_DASHBOARD_2_BILL_COUNT, DEFAULT_PATIENT_DASHBOARD_2_BILL_COUNT, new Action1<Integer>() {
			@Override
			public void apply(Integer parameter) {
				cashierSettings.setPatientDashboard2BillCount(parameter);
			}
		});

		getIntProperty(DEPARTMENT_COLLECTIONS_REPORT_ID_PROPERTY, new Action1<Integer>() {
			@Override
			public void apply(Integer parameter) {
				cashierSettings.setDepartmentCollectionsReportId(parameter);
			}
		});

		getIntProperty(DEPARTMENT_REVENUE_REPORT_ID_PROPERTY, new Action1<Integer>() {
			@Override
			public void apply(Integer parameter) {
				cashierSettings.setDepartmentRevenueReportId(parameter);
			}
		});

		getIntProperty(SHIFT_SUMMARY_REPORT_ID_PROPERTY, new Action1<Integer>() {
			@Override
			public void apply(Integer parameter) {
				cashierSettings.setShiftSummaryReportId(parameter);
			}
		});

		getIntProperty(DAILY_SHIFT_SUMMARY_REPORT_ID_PROPERTY, new Action1<Integer>() {
			@Override
			public void apply(Integer parameter) {
				cashierSettings.setDailyShiftSummaryReportId(parameter);
			}
		});

		getIntProperty(PAYMENTS_BY_PAYMENT_MODE_REPORT_ID_PROPERTY, new Action1<Integer>() {
			@Override
			public void apply(Integer parameter) {
				cashierSettings.setPaymentsByPaymentModeReportId(parameter);
			}
		});

		return cashierSettings;
	}

	public static void saveSettings(CashierSettings cashierSettings) {
		if (cashierSettings == null) {
			throw new IllegalArgumentException("The settings to save must be defined.");
		}

		setBoolProperty(ADJUSTMENT_REASEON_FIELD, cashierSettings.getAdjustmentReasonField());
		setBoolProperty(ALLOW_BILL_ADJUSTMENT, cashierSettings.getAllowBillAdjustment());
		setBoolProperty(AUTOFILL_PAYMENT_AMOUNT, cashierSettings.getAutoFillPaymentAmount());
		setIntProperty(CASHIER_SHIFT_REPORT_ID_PROPERTY, cashierSettings.getDefaultShiftReportId());
		setIntProperty(ROUND_TO_NEAREST_PROPERTY, cashierSettings.getCashierRoundingToNearest());
		setIntProperty(RECEIPT_REPORT_ID_PROPERTY, cashierSettings.getDefaultReceiptReportId());
		setStringProperty(ROUNDING_MODE_PROPERTY, cashierSettings.getCashierRoundingMode());
		setBoolProperty(TIMESHEET_REQUIRED_PROPERTY, cashierSettings.getCashierTimesheetRequired());
		setIntProperty(PATIENT_DASHBOARD_2_BILL_COUNT, cashierSettings.getPatientDashboard2BillCount());
		setIntProperty(DEPARTMENT_COLLECTIONS_REPORT_ID_PROPERTY, cashierSettings.getDepartmentCollectionsReportId());
		setIntProperty(DEPARTMENT_REVENUE_REPORT_ID_PROPERTY, cashierSettings.getDepartmentRevenueReportId());
		setIntProperty(SHIFT_SUMMARY_REPORT_ID_PROPERTY, cashierSettings.getShiftSummaryReportId());
		setIntProperty(DAILY_SHIFT_SUMMARY_REPORT_ID_PROPERTY, cashierSettings.getDailyShiftSummaryReportId());
		setIntProperty(PAYMENTS_BY_PAYMENT_MODE_REPORT_ID_PROPERTY, cashierSettings.getPaymentsByPaymentModeReportId());
	}

	// TODO: These functions should be moved to a commons-level base class for module settings classes
	private static Boolean getBoolProperty(String propertyName) {
		Boolean result = null;
		String property = administrationService.getGlobalProperty(propertyName);
		if (!StringUtils.isEmpty(property)) {
			result = Boolean.parseBoolean(property);
		}

		return result;
	}

	private static void getBoolProperty(String propertyName, Action1<Boolean> action) {
		getBoolProperty(propertyName, null, action);
	}

	private static void getBoolProperty(String propertyName, Boolean defaultValue, Action1<Boolean> action) {
		String property = administrationService.getGlobalProperty(propertyName);
		if (!StringUtils.isEmpty(property)) {
			action.apply(Boolean.parseBoolean(property));
		} else if (defaultValue != null) {
			action.apply(defaultValue);
		}
	}

	private static void setBoolProperty(String propertyName, Boolean value) {
		if (Boolean.TRUE.equals(value)) {
			administrationService.setGlobalProperty(propertyName, Boolean.TRUE.toString());
		} else {
			administrationService.setGlobalProperty(propertyName, Boolean.FALSE.toString());
		}
	}

	private static Integer getIntProperty(String propertyName) {
		Integer result = null;
		String property = administrationService.getGlobalProperty(propertyName);
		if (!StringUtils.isEmpty(property) && NumberUtils.isNumber(property)) {
			result = Integer.parseInt(property);
		}

		return result;
	}

	private static void getIntProperty(String propertyName, Action1<Integer> action) {
		getIntProperty(propertyName, null, action);
	}

	private static void getIntProperty(String propertyName, Integer defaultValue, Action1<Integer> action) {
		String property = administrationService.getGlobalProperty(propertyName);
		if (!StringUtils.isEmpty(property) && NumberUtils.isNumber(property)) {
			action.apply(Integer.parseInt(property));
		} else if (defaultValue != null) {
			action.apply(defaultValue);
		}
	}

	private static void setIntProperty(String propertyName, Integer value) {
		if (value != null) {
			administrationService.setGlobalProperty(propertyName, value.toString());
		} else {
			administrationService.setGlobalProperty(propertyName, "");
		}
	}

	private static void setStringProperty(String propertyName, String value) {
		if (value != null) {
			administrationService.setGlobalProperty(propertyName, value);
		} else {
			administrationService.setGlobalProperty(propertyName, "");
		}
	}
}
