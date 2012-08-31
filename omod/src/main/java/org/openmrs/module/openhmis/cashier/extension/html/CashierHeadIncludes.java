package org.openmrs.module.openhmis.cashier.extension.html;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.openmrs.module.web.extension.HeaderIncludeExt;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class CashierHeadIncludes extends HeaderIncludeExt {

	@Override
	public List<String> getHeaderFiles() {
		List<String> files = new LinkedList<String>();
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		if (request.getRequestURI().contains("openhmis/cashier")) {
			files.add("/moduleResources/openhmis/cashier/css/style.css");
			files.add("/moduleResources/openhmis/cashier/js/underscore.js");
			files.add("/moduleResources/openhmis/cashier/js/backbone.js");
			files.add("/moduleResources/openhmis/cashier/js/util.js");
			files.add("/moduleResources/openhmis/cashier/js/i18n/english.js");			
			files.add("/moduleResources/openhmis/cashier/js/model/lineItem.js");
			files.add("/moduleResources/openhmis/cashier/js/model/patient.js");
			files.add("/moduleResources/openhmis/cashier/js/view/lineItem.js");
			files.add("/moduleResources/openhmis/cashier/js/view/bill.js");
			files.add("/moduleResources/openhmis/cashier/js/view/patient.js");
		}
		return files;
	}

}
