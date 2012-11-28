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

import org.openmrs.module.openhmis.cashier.api.ISequentialReceiptNumberGeneratorService;
import org.openmrs.module.openhmis.cashier.api.ReceiptNumberGeneratorFactory;
import org.openmrs.module.openhmis.cashier.api.SequentialReceiptNumberGenerator;
import org.openmrs.module.openhmis.cashier.api.model.SequentialReceiptNumberGeneratorModel;
import org.openmrs.module.openhmis.cashier.web.CashierWebConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequestMapping(value = CashierWebConstants.SEQ_RECEIPT_NUMBER_GENERATOR_CONFIGURATION_PAGE)
public class SequentialReceiptNumberGeneratorController {
	ISequentialReceiptNumberGeneratorService service;

	@Autowired
	public SequentialReceiptNumberGeneratorController(ISequentialReceiptNumberGeneratorService service) {
		this.service = service;
	}

	@RequestMapping(method = RequestMethod.GET)
	public void render(ModelMap modelMap) {
		SequentialReceiptNumberGeneratorModel model = service.getOnly();

		modelMap.addAttribute("generator", model);
	}

	@RequestMapping(method = RequestMethod.POST)
	public String post(@ModelAttribute("generator") SequentialReceiptNumberGeneratorModel generator,
	                   WebRequest request) {
		if (generator.getSeparator().equals("<space>")) {
			generator.setSeparator(" ");
		}

		// The check digit checkbox value is only bound if checked
		if (request.getParameter("includeCheckDigit") == null) {
			generator.setIncludeCheckDigit(false);
		}

		// Save the generator settings
		service.save(generator);

		// Set the system generator
		ReceiptNumberGeneratorFactory.setGenerator(new SequentialReceiptNumberGenerator());

		return CashierWebConstants.redirectUrl("/" + CashierWebConstants.RECEIPT_NUMBER_GENERATOR_CONFIGURATION_PAGE);
	}
}
