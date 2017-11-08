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

import org.apache.commons.lang.StringUtils;
import org.openmrs.api.context.Context;
import org.openmrs.module.jasperreport.JasperReport;
import org.openmrs.module.jasperreport.JasperReportService;
import org.openmrs.module.jasperreport.ReportParameter;
import org.openmrs.module.jasperreport.ReportsControllerBase;
import org.openmrs.module.openhmis.cashier.ModuleSettings;
import org.openmrs.module.openhmis.cashier.api.model.CashierSettings;
import org.openmrs.module.openhmis.cashier.web.CashierWebConstants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller to manage the Jasper Reports page.
 */
@Controller
@RequestMapping(value = CashierWebConstants.JASPER_REPORT_PAGE)
public class JasperReportController extends ReportsControllerBase {
	@Override
	public String parse(int reportId, WebRequest request, HttpServletResponse response) throws IOException {
		CashierSettings settings = ModuleSettings.loadSettings();
		if (settings.getDefaultShiftReportId() != null && reportId == settings.getDefaultShiftReportId()) {
			return renderShiftReport(reportId, request, response);
		} else if (settings.getDepartmentCollectionsReportId() != null
		        && reportId == settings.getDepartmentCollectionsReportId()) {
			return renderDateRangeReport(reportId, "Department Collections", request, response);
		} else if (settings.getDepartmentRevenueReportId() != null
		        && reportId == settings.getDepartmentRevenueReportId()) {
			return renderDateRangeReport(reportId, "Department Revenue", request, response);
		} else if (settings.getDailyShiftSummaryReportId() != null
		        && reportId == settings.getDailyShiftSummaryReportId()) {
			return renderDateRangeReport(reportId, "Daily Shift Summary", request, response);
		} else if (settings.getShiftSummaryReportId() != null
		        && reportId == settings.getShiftSummaryReportId()) {
			return renderDateRangeReport(reportId, "Shift Summary", request, response);
		}

		return null;
	}

	private String renderShiftReport(int reportId, WebRequest request, HttpServletResponse response) throws IOException {
		int timesheetId;
		String temp = request.getParameter("timesheetId");
		if (!StringUtils.isEmpty(temp) && StringUtils.isNumeric(temp)) {
			timesheetId = Integer.parseInt(temp);
		} else {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "The timesheet id ('" + temp
			        + "') must be defined and be numeric.");
			return null;
		}

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("timesheetId", timesheetId);

		return renderReport(reportId, params, "Cashier Shift Report - " + temp, response);
	}

	private String renderDateRangeReport(int reportId, String reportName, WebRequest request,
	        HttpServletResponse response) throws IOException {
		HashMap<String, Object> params = new HashMap<String, Object>();
		if (parseDateRange(reportId, request, response, params)) {
			String format = request.getParameter("format");
			if (StringUtils.isEmpty(format)) {
				format = "pdf";
			}

			return renderReport(reportId, params, reportName, response, format);
		} else {
			return null;
		}
	}

	private Boolean parseDateRange(int reportId, WebRequest request, HttpServletResponse response,
	        Map<String, Object> params) throws IOException {
		Date beginDate = null, endDate = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		String temp = request.getParameter("beginDate");
		if (!StringUtils.isEmpty(temp)) {
			try {
				beginDate = dateFormat.parse(temp);
			} catch (Exception ex) {
				// Whatevs... dealing with stupid checked exceptions
			}
		}

		temp = request.getParameter("endDate");
		if (!StringUtils.isEmpty(temp)) {
			try {
				endDate = dateFormat.parse(temp);
			} catch (Exception ex) {
				// Whatevs... dealing with stupid checked exceptions
			}
		}

		if (beginDate == null || endDate == null) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "The begin and end dates must be defined.");

			return false;
		}

		// Get the report definition and check to see if the parameter is named beginDate or startDate
		String beginParameterName = "beginDate";
		JasperReportService reportService = Context.getService(JasperReportService.class);
		JasperReport report = reportService.getJasperReport(reportId);
		for (ReportParameter parameter : report.getParameters()) {
			if (StringUtils.equalsIgnoreCase(parameter.getName(), "startDate")) {
				beginParameterName = parameter.getName();
				break;
			}
		}

		params.put(beginParameterName, beginDate);
		params.put("endDate", endDate);

		return true;
	}
}
