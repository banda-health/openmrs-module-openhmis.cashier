package org.openmrs.module.openhmis.cashier.web.controller;

import org.openmrs.module.openhmis.cashier.api.model.CashierOptions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/module/openhmis/cashier/options")
public class CashierOptionsController {
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public CashierOptions options() {
		CashierOptions options = new CashierOptions();
		options.loadFromGlobalOptions();
		return options;
	}
}
