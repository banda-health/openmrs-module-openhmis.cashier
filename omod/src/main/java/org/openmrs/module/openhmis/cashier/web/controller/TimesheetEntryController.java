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
package org.openmrs.module.openhmis.cashier.web.controller;

import org.apache.commons.lang.StringUtils;
import org.openmrs.Provider;
import org.openmrs.api.APIException;
import org.openmrs.api.ProviderService;
import org.openmrs.module.openhmis.cashier.api.ICashPointService;
import org.openmrs.module.openhmis.cashier.api.ITimesheetService;
import org.openmrs.module.openhmis.cashier.api.model.CashPoint;
import org.openmrs.module.openhmis.cashier.api.model.Timesheet;
import org.openmrs.module.openhmis.cashier.api.util.ProviderHelper;
import org.openmrs.module.openhmis.cashier.web.CashierWebConstants;
import org.openmrs.module.openhmis.cashier.web.propertyeditor.EntityPropertyEditor;
import org.openmrs.module.openhmis.cashier.web.propertyeditor.ProviderPropertyEditor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = CashierWebConstants.TIMESHEET_ENTRY_PAGE)
public class TimesheetEntryController {
	private ITimesheetService timesheetService;
	private ICashPointService cashPointService;
	private ProviderService providerService;

	@Autowired
	public TimesheetEntryController(ITimesheetService timesheetService,
	                                ICashPointService cashPointService,
	                                ProviderService providerService) {
		this.timesheetService = timesheetService;
		this.cashPointService = cashPointService;
		this.providerService = providerService;
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(CashPoint.class, new EntityPropertyEditor<CashPoint>(ICashPointService.class));
		binder.registerCustomEditor(Provider.class, new ProviderPropertyEditor());
	}

	@RequestMapping(method = RequestMethod.GET)
	public void render(@RequestParam(value = "providerId", required = false) Integer providerId,
	                   @RequestParam(value = "returnUrl", required = false) String returnUrl,
	                   ModelMap modelMap) {
		Provider provider;
		if (providerId != null) {
			provider = providerService.getProvider(providerId);
		} else {
			provider = ProviderHelper.getCurrentProvider(providerService);
		}

		if (provider == null) {
			throw new APIException("Could not locate the provider.");
		}

		Timesheet timesheet = timesheetService.getCurrentTimesheet(provider);
		if (timesheet == null) {
			timesheet = new Timesheet();
			timesheet.setCashier(provider);
			timesheet.setClockIn(new Date());
		}

		addRenderAttributes(modelMap, timesheet, provider, returnUrl);
	}

	@RequestMapping(method = RequestMethod.POST)
	public String post(Timesheet timesheet, Errors errors, WebRequest request, ModelMap modelMap) {
		String returnUrl = request.getParameter("returnUrl");

		new TimesheetEntryValidator().validate(timesheet, errors);
		if (errors.hasErrors()) {
			addRenderAttributes(modelMap, timesheet, timesheet.getCashier(), returnUrl);

			return null;
		}

		timesheetService.save(timesheet);

		if (StringUtils.isEmpty(returnUrl)) {
			returnUrl = "redirect:/";
		} else {
			returnUrl = "redirect:/" + returnUrl;
		}
		return returnUrl;
	}

	@ModelAttribute("cashPoints")
	public List<CashPoint> getCashPoints() {
		return cashPointService.getAll();
	}

	private void addRenderAttributes(ModelMap modelMap, Timesheet timesheet, Provider cashier, String returnUrl) {
		modelMap.addAttribute("returnUrl", returnUrl);
		modelMap.addAttribute("cashier", cashier);
		modelMap.addAttribute("timesheet", timesheet);
	}
}

