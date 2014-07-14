package org.openmrs.module.openhmis.cashier.web;

import org.openmrs.module.openhmis.cashier.api.util.CashierPrivilegeConstants;

/**
 * Created by benjamin on 7/14/14.
 */
public class CashierPrivilegeWebConstants extends CashierPrivilegeConstants {

    public static final String CASHPOINTS_PAGE_PRIVILEDGES = MANAGE_METADATA + "," +VIEW_METADATA;
    public static final String PAYMENTSMODES_PAGE_PRIVILEDGES = MANAGE_METADATA + "," +VIEW_METADATA;
    public static final String BILL_PAGE_PRIVILEDGES = MANAGE_METADATA + "," +VIEW_METADATA;
    protected CashierPrivilegeWebConstants() {}
}
