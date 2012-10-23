package org.openmrs.module.openhmis.cashier.web.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.openmrs.web.taglib.fieldgen.FieldGenHandlerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/fieldgenhandlers.json")
public class FieldGenHandlerController {
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Set<String>> fieldgenhandlers() {
		Map<String, Set<String>> container = new HashMap<String, Set<String>>();
		container.put("results", new TreeSet<String>(FieldGenHandlerFactory.getSingletonInstance().getHandlers().keySet()));
		return container;
	}
}
