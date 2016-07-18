/*
 * The contents of this file are subject to the OpenMRS Public License
 * Version 2.0 (the "License"); you may not use this file except in
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

import org.openmrs.module.openhmis.backboneforms.web.controller.BackboneMessageRenderController;
import org.openmrs.module.openhmis.cashier.web.CashierWebConstants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Enumeration;
import java.util.Vector;

/**
 * Controller to manage the Message Render page.
 */
@Controller
@RequestMapping(CashierWebConstants.MESSAGE_PROPERTIES_JS_URI)
public class CashierMessageRenderController {

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView render(HttpServletRequest request) {
		// object to store keys from cashier and backboneforms
		Vector<String> keys = new Vector<String>();

		// locate and retrieve cashier messages
		Locale locale = RequestContextUtils.getLocale(request);
		ResourceBundle resourceBundle = ResourceBundle.getBundle("messages", locale);

		// store cashier message keys in the vector object
		keys.addAll(resourceBundle.keySet());

		// retrieve backboneforms messages
		BackboneMessageRenderController backboneController = new BackboneMessageRenderController();
		ModelAndView modelAndView = backboneController.render(request);

		// store backboneforms message keys in the vector object
		for (Map.Entry<String, Object> messageKeys : modelAndView.getModel().entrySet()) {
			Enumeration<String> messageKey = (Enumeration<String>)messageKeys.getValue();
			while (messageKey.hasMoreElements()) {
				String key = messageKey.nextElement();
				if (!keys.contains(key))
					keys.add(key);
			}
		}

		return new ModelAndView(CashierWebConstants.MESSAGE_PAGE, "keys", keys.elements());
	}
}
