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

import org.openmrs.module.openhmis.cashier.api.ISequentialReceiptNumberGeneratorService;
import org.openmrs.module.openhmis.cashier.web.CashierWebConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Contorller to manage the Sequential Receipt Number Generation
 */
@Controller
@RequestMapping(value = SequentialReceiptNumberGeneratorController.SEQ_RECEIPT_NUMBER_GENERATOR_URL)
public class SequentialReceiptNumberGeneratorController extends AbstractSequentialReceiptNumberGenerator {

	public static final String SEQ_RECEIPT_NUMBER_GENERATOR_URL = CashierWebConstants.SEQ_RECEIPT_NUMBER_GENERATOR_PAGE;

	private ISequentialReceiptNumberGeneratorService service;

	@Autowired
	public SequentialReceiptNumberGeneratorController(ISequentialReceiptNumberGeneratorService service) {
		this.service = service;
	}

	@Override
	public ISequentialReceiptNumberGeneratorService getService() {
		return this.service;
	}

	@Override
	public String getReceiptNumberGeneratorUrl() {
		return SEQ_RECEIPT_NUMBER_GENERATOR_URL;
	}
}
