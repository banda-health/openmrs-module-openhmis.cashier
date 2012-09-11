package org.openmrs.module.openhmis.cashier.web.controller;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/module/openhmis/cashier/items")
public class ItemsController {
	@RequestMapping(method = RequestMethod.GET)
	public void items(ModelMap model) throws JsonGenerationException, JsonMappingException, IOException {
		model.addAttribute("modelBase", "openhmis.cashier.item");
	}
}