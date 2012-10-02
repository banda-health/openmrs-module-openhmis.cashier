package org.openmrs.module.openhmis.cashier.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.openmrs.Concept;
import org.openmrs.api.ConceptService;
import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.cashier.api.IPaymentModeService;
import org.openmrs.module.openhmis.cashier.api.model.PaymentMode;
import org.openmrs.module.openhmis.cashier.api.model.PaymentModeAttributeType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/module/openhmis/cashier/paymentModeFragment")
public class PaymentModeFragmentController {
	@RequestMapping(method = RequestMethod.GET)
	public void paymentModeFragment(@RequestParam("uuid") String uuid, ModelMap model) {
		IPaymentModeService service = Context.getService(IPaymentModeService.class);
		PaymentMode paymentMode = service.getByUuid(uuid);
		ConceptService conceptService = Context.getConceptService();
		Map<Integer, Concept> conceptMap = new HashMap<Integer, Concept>();
		for (PaymentModeAttributeType type : paymentMode.getAttributeTypes()) {
			if (type.getFormat().equals("org.openmrs.Concept") && type.getForeignKey() != null)
				conceptMap.put(type.getForeignKey(), conceptService.getConcept(type.getForeignKey()));
		}
		model.addAttribute("paymentMode", paymentMode);
		model.addAttribute("conceptMap", conceptMap);
	}
}
