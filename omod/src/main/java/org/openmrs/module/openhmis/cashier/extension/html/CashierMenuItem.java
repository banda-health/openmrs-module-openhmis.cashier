package org.openmrs.module.openhmis.cashier.extension.html;

import org.openmrs.module.web.extension.LinkExt;

public class CashierMenuItem extends LinkExt {

	@Override
	public MEDIA_TYPE getMediaType() {
		return MEDIA_TYPE.html;
	}
	
	@Override
	public String getLabel() {
		return "openhmis.cashier.menuItem";
	}

	@Override
	public String getRequiredPrivilege() {
		return "Add Bills";
	}

	@Override
	public String getUrl() {
		return "module/openhmis.cashier/bill.form";
	}

}
