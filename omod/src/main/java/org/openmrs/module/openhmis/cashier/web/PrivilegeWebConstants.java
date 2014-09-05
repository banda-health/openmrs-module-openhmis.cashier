package org.openmrs.module.openhmis.cashier.web;

import org.openmrs.module.openhmis.cashier.api.util.PrivilegeConstants;

public class PrivilegeWebConstants extends PrivilegeConstants {
    public static final String CASHPOINTS_PAGE_PRIVILEDGES = MANAGE_METADATA + "," +VIEW_METADATA;
    public static final String PAYMENTSMODES_PAGE_PRIVILEDGES = MANAGE_METADATA + "," +VIEW_METADATA;
    public static final String BILL_PAGE_PRIVILEDGES = MANAGE_METADATA + "," +VIEW_METADATA;
    
    protected PrivilegeWebConstants() {}
}
