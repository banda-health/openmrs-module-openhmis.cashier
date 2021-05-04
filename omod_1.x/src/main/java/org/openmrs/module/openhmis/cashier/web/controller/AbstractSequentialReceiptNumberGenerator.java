package org.openmrs.module.openhmis.cashier.web.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.openmrs.module.openhmis.cashier.ModuleSettings;
import org.openmrs.module.openhmis.cashier.api.ISequentialReceiptNumberGeneratorService;
import org.openmrs.module.openhmis.cashier.api.ReceiptNumberGeneratorFactory;
import org.openmrs.module.openhmis.cashier.api.SequentialReceiptNumberGenerator;
import org.openmrs.module.openhmis.cashier.api.model.SequentialReceiptNumberGeneratorModel;
import org.openmrs.module.openhmis.commons.api.util.UrlUtil;
import org.openmrs.module.openhmis.commons.web.controller.HeaderController;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

/**
 * Abstract sequential receipt number generator functionality
 */
public abstract class AbstractSequentialReceiptNumberGenerator {

	public abstract ISequentialReceiptNumberGeneratorService getService();

	public abstract String getReceiptNumberGeneratorUrl();

	@RequestMapping(method = RequestMethod.GET)
	public void render(ModelMap modelMap, HttpServletRequest request) throws IOException {
		SequentialReceiptNumberGeneratorModel model = getService().getOnly();

		modelMap.addAttribute("generator", model);

		modelMap.addAttribute("settings", ModuleSettings.loadSettings());

		HeaderController.render(modelMap, request);
	}

	@RequestMapping(method = RequestMethod.POST)
	public String post(@ModelAttribute("generator") SequentialReceiptNumberGeneratorModel generator, WebRequest request) {
		if (generator.getSeparator().equals("<space>")) {
			generator.setSeparator(" ");
		}

		// The check digit checkbox value is only bound if checked
		if (request.getParameter("includeCheckDigit") == null) {
			generator.setIncludeCheckDigit(false);
		}

		// Save the generator settings
		getService().save(generator);

		// Set the system generator
		ReceiptNumberGeneratorFactory.setGenerator(new SequentialReceiptNumberGenerator());
		return UrlUtil.redirectUrl(getReceiptNumberGeneratorUrl());
	}
}
