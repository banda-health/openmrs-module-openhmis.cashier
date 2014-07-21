package org.openmrs.module.openhmis.cashier.web;

import org.openmrs.module.webservices.rest.web.RestConstants;

/**
 * Created by benjamin on 7/18/14.
 */
public class CashierRestConstants extends CashierWebConstants {
    public static final String CASHIER_REST_ROOT = RestConstants.VERSION_2 + "/cashier/";

    public static final String CASH_POINT_RESOURCE = CASHIER_REST_ROOT + "cashPoint";
}
