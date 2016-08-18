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
package org.openmrs.module.openhmis.cashier.page.controller.reports;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.annotation.OpenmrsProfile;
import org.openmrs.api.context.Context;
import org.openmrs.module.jasperreport.JasperReport;
import org.openmrs.module.jasperreport.JasperReportService;
import org.openmrs.module.openhmis.cashier.ModuleSettings;
import org.openmrs.module.openhmis.cashier.api.model.CashierSettings;
import org.openmrs.module.openhmis.cashier.web.CashierWebConstants;
import org.openmrs.module.openhmis.commons.api.exception.ReportNotFoundException;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.page.PageModel;
import org.openmrs.ui.framework.page.PageRequest;
import org.springframework.stereotype.Controller;

/**
 * Controller for the cashier management landing page.
 */
@Controller
@OpenmrsProfile(modules = { "uiframework:*.*" })
public class ReportsPageController {
	private static final Log LOG = LogFactory.getLog(ReportsPageController.class);

	private JasperReportService reportService;
	private List<JasperReport> reports;

	public void get(PageModel model) throws IOException {
		reportService = Context.getService(JasperReportService.class);
		reports = new ArrayList<JasperReport>();

		CashierSettings settings = ModuleSettings.loadSettings();

		// Add reports to page model
		addReportAttribute(model, settings.getDepartmentCollectionsReportId(), "departmentCollectionsReport");
		addReportAttribute(model, settings.getDepartmentRevenueReportId(), "departmentRevenueReport");
		addReportAttribute(model, settings.getShiftSummaryReportId(), "shiftSummaryReport");
		addReportAttribute(model, settings.getDailyShiftSummaryReportId(), "dailyShiftSummaryReport");
		addReportAttribute(model, settings.getPaymentsByPaymentModeReportId(), "paymentsByPaymentModeReport");

		model.addAttribute("reports", reports);
		model.addAttribute("reportUrl", CashierWebConstants.JASPER_REPORT_PAGE);
	}

	private void addReportAttribute(PageModel model, Integer reportId, String reportName) {
		if (reportId != null) {
			try {
				JasperReport report = reportService.getJasperReport(reportId);

				model.addAttribute(reportName, report);
				reports.add(report);
			} catch (NullPointerException e) {
				LOG.error("The jasper report with ID '" + reportId + "' could not be found", e);
				throw new ReportNotFoundException(
				        "The report could not be found. Check configuration under Inventory Settings");
			}
		}
	}
}
