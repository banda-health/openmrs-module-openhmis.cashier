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

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Provider;
import org.openmrs.api.APIException;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.ProviderService;
import org.openmrs.api.context.Context;
import org.openmrs.module.jasperreport.JasperReport;
import org.openmrs.module.jasperreport.JasperReportService;
import org.openmrs.module.openhmis.cashier.ModuleSettings;
import org.openmrs.module.openhmis.cashier.api.ICashPointService;
import org.openmrs.module.openhmis.cashier.api.ITimesheetService;
import org.openmrs.module.openhmis.cashier.api.model.CashPoint;
import org.openmrs.module.openhmis.cashier.api.model.Timesheet;
import org.openmrs.module.openhmis.cashier.web.CashierWebConstants;
import org.openmrs.module.openhmis.cashier.web.propertyeditor.EntityPropertyEditor;
import org.openmrs.module.openhmis.cashier.web.propertyeditor.ProviderPropertyEditor;
import org.openmrs.module.openhmis.commons.api.ProviderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.WebRequest;

/**
 * Controller to manahe the Cashier page.
 */
@Controller
@RequestMapping(value = CashierWebConstants.CASHIER_PAGE)
public class CashierController {
	private static final Log LOG = LogFactory.getLog(CashierController.class);

	private ITimesheetService timesheetService;
	private ICashPointService cashPointService;
	private ProviderService providerService;
	private JasperReportService jasperService;
	private AdministrationService adminService;

	@Autowired
	public CashierController(ITimesheetService timesheetService, ICashPointService cashPointService,
	    ProviderService providerService, AdministrationService adminService) {
		this.timesheetService = timesheetService;
		this.cashPointService = cashPointService;
		this.providerService = providerService;
		this.adminService = adminService;
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(CashPoint.class, new EntityPropertyEditor<CashPoint>(ICashPointService.class));
		binder.registerCustomEditor(Provider.class, new ProviderPropertyEditor());

		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
		dateFormat.setLenient(false);

		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}

	@RequestMapping(method = RequestMethod.GET)
	public void render(@RequestParam(value = "providerId", required = false) Integer providerId,
	        @RequestParam(value = "returnUrl", required = false) String returnUrl, ModelMap modelMap) {
		Provider provider;
		if (providerId != null) {
			provider = providerService.getProvider(providerId);
		} else {
			provider = ProviderUtil.getCurrentProvider(providerService);
		}

		if (provider == null) {
			throw new APIException("ERROR: Could not locate the provider. Please make sure the user is listed as provider "
			        + "(Admin -> Manage providers)");
		}

		String returnTo = returnUrl;
		if (StringUtils.isEmpty(returnTo)) {
			HttpServletRequest req = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
			returnTo = req.getHeader("Referer");

			if (!StringUtils.isEmpty(returnTo)) {
				try {
					URL url = new URL(returnTo);

					returnTo = url.getPath();
					if (StringUtils.startsWith(returnTo, req.getContextPath())) {

						returnTo = returnTo.substring(req.getContextPath().length());
					}
				} catch (MalformedURLException e) {
					LOG.warn("Could not parse referrer url '" + returnTo + "'");
					returnTo = "";
				}
			}
		}

		// Load the current timesheet information
		Timesheet timesheet = timesheetService.getCurrentTimesheet(provider);
		if (timesheet == null) {
			timesheet = new Timesheet();
			timesheet.setCashier(provider);
			timesheet.setClockIn(new Date());
		}

		// load shift report (this must be refactored for the next version)
		loadShiftReport(modelMap);

		addRenderAttributes(modelMap, timesheet, provider, returnTo);
	}

	@RequestMapping(method = RequestMethod.POST)
	public String post(Timesheet timesheet, Errors errors, WebRequest request, ModelMap modelMap) {
		String returnUrl = request.getParameter("returnUrl");

		new TimesheetEntryValidator().validate(timesheet, errors);
		if (errors.hasErrors()) {
			loadShiftReport(modelMap);
			addRenderAttributes(modelMap, timesheet, timesheet.getCashier(), returnUrl);

			return null;
		}

		timesheetService.save(timesheet);

		if (StringUtils.isEmpty(returnUrl)) {
			returnUrl = "redirect:";
		} else {
			returnUrl = "redirect:" + returnUrl;
		}
		return returnUrl;
	}

	@ModelAttribute("cashPoints")
	public List<CashPoint> getCashPoints() {
		return cashPointService.getAll();
	}

	private void loadShiftReport(ModelMap modelMap) {
		if (jasperService == null) {
			jasperService = Context.getService(JasperReportService.class);
		}

		JasperReport shiftReport = null;
		String shiftReportId = adminService.getGlobalProperty(ModuleSettings.CASHIER_SHIFT_REPORT_ID_PROPERTY);
		if (StringUtils.isNotEmpty(shiftReportId)) {
			if (StringUtils.isNumeric(shiftReportId)) {
				shiftReport = jasperService.getJasperReport(Integer.parseInt(shiftReportId));

				if (shiftReport != null) {
					modelMap.addAttribute("shiftReport", shiftReport);
				}
			}
		}
	}

	private void addRenderAttributes(ModelMap modelMap, Timesheet timesheet, Provider cashier, String returnUrl) {
		modelMap.addAttribute("returnUrl", returnUrl);
		modelMap.addAttribute("cashier", cashier);
		modelMap.addAttribute("timesheet", timesheet);
	}
}
