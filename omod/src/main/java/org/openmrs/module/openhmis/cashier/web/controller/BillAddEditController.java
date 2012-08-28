package org.openmrs.module.openhmis.cashier.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/module/openhmis.cashier/bill.form")
public class BillAddEditController {
	
	@RequestMapping(method = RequestMethod.GET)
	public void bill(ModelMap model) {
		
	}

}
