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
package org.openmrs.module.openhmis.cashier.web;

public class CashierWebConstants {
	public static final String MODULE_ROOT = "/module/openhmis/cashier/";

	public static final String RECEIPT_NUMBER_GENERATOR_CONFIGURATION_PAGE = MODULE_ROOT + "admin/receiptNumberGenerator";
	public static final String SEQ_RECEIPT_NUMBER_GENERATOR_CONFIGURATION_PAGE = MODULE_ROOT + "admin/seqReceiptNumberGenerator";

	public static final String CASHIER_PAGE = MODULE_ROOT + "cashier";
	public static final String CASHIER_SHIFT_REPORT_ID_PROPERTY = "openhmis.cashier.defaultShiftReportId";
	public static final String JASPER_REPORT_PAGE = MODULE_ROOT + "jasperReport";
	public static final String CASHIER_ROLE_PAGE = MODULE_ROOT + "cashierRole";

	public static final String BILL_PAGE = MODULE_ROOT + "bill";
	public static final String TIMESHEET_REQUIRED_PROPERTY = "openhmis.cashier.timesheetRequired";

	public static final String RECEIPT = MODULE_ROOT + "receipt";
	public static final String RECEIPT_REPORT_ID_PROPERTY = "openhmis.cashier.defaultReceiptReportId";
	public static final String RECEIPT_REPORT_NAME_PROPERTY = "openhmis.cashier.defaultReceiptReportName";
	
	public static final String ROUNDING_MODE_PROPERTY = "openhmis.cashier.roundingMode";
	public static final String ROUND_TO_NEAREST_PROPERTY = "openhmis.cashier.roundToNearest";
	public static final String ROUNDING_ITEM_ID = "openhmis.cashier.roundingItemId";
	public static final String ROUNDING_DEPT_ID = "openhmis.cashier.roundingDeptId";

	public static final String REPORT_DOWNLOAD_URL = "/moduleServlet/jasperreport/jreportDownload";

	public static String formUrl(String page) {
		return page.endsWith(".form") ? page : page + ".form";
	}

	public static String redirectUrl(String page) {
		return "redirect:" + formUrl(page);
	}
}
