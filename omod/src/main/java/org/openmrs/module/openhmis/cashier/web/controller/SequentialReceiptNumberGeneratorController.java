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

import org.openmrs.module.openhmis.cashier.api.IReceiptNumberGenerator;
import org.openmrs.module.openhmis.cashier.api.ISequentialReceiptNumberGeneratorService;
import org.openmrs.module.openhmis.cashier.api.ReceiptNumberGeneratorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/module/openhmis/cashier/seqReceiptNumberGenerator")
public class SequentialReceiptNumberGeneratorController {
	ISequentialReceiptNumberGeneratorService service;

	@Autowired
	public SequentialReceiptNumberGeneratorController(ISequentialReceiptNumberGeneratorService service) {
		this.service = service;
	}

	@RequestMapping(method = RequestMethod.GET)
	public void render(ModelMap model) {
		IReceiptNumberGenerator currentGenerator = ReceiptNumberGeneratorFactory.getGenerator();
		IReceiptNumberGenerator[] generators = ReceiptNumberGeneratorFactory.locateGenerators();

		model.addAttribute("currentGenerator", currentGenerator);
		model.addAttribute("generators", generators);
	}
}
