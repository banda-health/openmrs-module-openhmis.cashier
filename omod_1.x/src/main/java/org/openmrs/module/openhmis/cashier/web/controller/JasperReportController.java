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
import org.openmrs.module.jasperreport.ReportsControllerBase;
import org.openmrs.module.openhmis.cashier.web.CashierWebConstants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

/**
 * Controller to manage the Jasper Reports page.
 */
@Controller
@RequestMapping(value = CashierWebConstants.JASPER_REPORT_PAGE)
public class JasperReportController extends ReportsControllerBase {
	@Override
	public String parse(int reportId, WebRequest request, HttpServletResponse response) throws IOException {
		int timesheetId;
		String temp = request.getParameter("timesheetId");
		if (!StringUtils.isEmpty(temp) && StringUtils.isNumeric(temp)) {
			timesheetId = Integer.parseInt(temp);
		} else {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "The timesheet id ('" + temp + "') must be "
			        + "defined and be numeric.");
			return null;
		}

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("timesheetId", timesheetId);

		return renderReport(reportId, params, "Cashier Shift Report - " + temp, response);
	}
}
