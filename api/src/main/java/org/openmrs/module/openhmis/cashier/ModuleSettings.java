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

import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.jasperreport.JasperReport;
import org.openmrs.module.jasperreport.JasperReportService;

public class ModuleSettings {
	public static final String RECEIPT_REPORT_ID_PROPERTY = "openhmis.cashier.defaultReceiptReportId";
	public static final String CASHIER_SHIFT_REPORT_ID_PROPERTY = "openhmis.cashier.defaultShiftReportId";
	public static final String TIMESHEET_REQUIRED_PROPERTY = "openhmis.cashier.timesheetRequired";
	public static final String ROUNDING_MODE_PROPERTY = "openhmis.cashier.roundingMode";
	public static final String ROUND_TO_NEAREST_PROPERTY = "openhmis.cashier.roundToNearest";
	public static final String ROUNDING_ITEM_ID = "openhmis.cashier.roundingItemId";
	public static final String ROUNDING_DEPT_ID = "openhmis.cashier.roundingDeptId";
	public static final String SYSTEM_RECEIPT_NUMBER_GENERATOR = "openhmis.cashier.systemReceiptNumberGenerator";
	
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
}
