package org.openmrs.module.openhmis.cashier.web.controller;

import org.openmrs.module.openhmis.backboneforms.web.BackboneWebConstants;
import org.openmrs.module.openhmis.cashier.web.CashierWebConstants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by dubdabasoduba on 16/04/15.
 */
@Controller @RequestMapping(CashierWebConstants.MESSAGEPROPERTIES_JS_URI) public class MessageRenderController {

	@RequestMapping(method = RequestMethod.GET) public ModelAndView MessageRenderController(HttpServletRequest request) {
		Locale locale = RequestContextUtils.getLocale(request);
		ResourceBundle resourceBundle = ResourceBundle.getBundle("messages", locale);
		return new ModelAndView(CashierWebConstants.MESSAGE_PAGE, "keys", resourceBundle.getKeys());
	}
}
