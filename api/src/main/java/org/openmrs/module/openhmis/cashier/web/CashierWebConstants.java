/*
 * The contents of this file are subject to the OpenMRS Public License
 * Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See
 * the License for the specific language governing rights and
 * limitations under the License.
 *
 * Copyright (C) OpenHMIS.  All Rights Reserved.
 */
package org.openmrs.module.openhmis.cashier.web;

import org.openmrs.module.openhmis.cashier.api.util.CashierModuleConstants;
import org.openmrs.module.openhmis.commons.web.WebConstants;

/**
 * Constants class for the module web resources.
 */
public class CashierWebConstants extends WebConstants {
	public static final String MODULE_ROOT = WebConstants.MODULE_BASE + CashierModuleConstants.MODULE_NAME + "/";
	public static final String MODULE_RESOURCE_ROOT = WebConstants.MODULE_RESOURCE_BASE
	        + CashierModuleConstants.MODULE_NAME + "/";
	public static final String MODULE_COMMONS_RESOURCE_ROOT = "/moduleResources/openhmis/commons/";
	public static final String CASHIER_PAGE = MODULE_ROOT + "cashier";
	public static final String JASPER_REPORT_PAGE = MODULE_ROOT + "jasperReport";
	public static final String CASHIER_ROLE_ROOT = MODULE_ROOT + "cashierRole";
	public static final String CASHIER_ROLE_PAGE = CASHIER_ROLE_ROOT + ".form";

	public static final String CASHIER_ROLE_2X_ROOT = MODULE_ROOT + "cashierRole2x";
	public static final String CASHIER_ROLE_2X_PAGE = CASHIER_ROLE_ROOT + ".page";

	public static final String CASH_POINTS_ROOT = MODULE_ROOT + "cashPoints";
	public static final String CASH_POINTS_PAGE = CASH_POINTS_ROOT + ".form";
	public static final String PAYMENT_MODES_ROOT = MODULE_ROOT + "paymentModes";
	public static final String PAYMENT_MODES_PAGE = PAYMENT_MODES_ROOT + ".form";
	public static final String CASHIER_SETTINGS_ROOT = MODULE_ROOT + "cashierSettings";
	public static final String CASHIER_SETTINGS_PAGE = CASHIER_SETTINGS_ROOT + ".form";

	public static final String CASHIER_SETTINGS_2X_ROOT = MODULE_ROOT + "cashierSettings2x";
	public static final String CASHIER_SETTINGS_2X_PAGE = CASHIER_SETTINGS_2X_ROOT + ".page";

	public static final String MODULE_SETTINGS_ROOT = MODULE_ROOT + "moduleSettings";
	public static final String MODULE_SETTINGS_PAGE = MODULE_SETTINGS_ROOT + ".page";

	public static final String BILL_PAGE = MODULE_ROOT + "bill";
	public static final String RECEIPT = MODULE_ROOT + "receipt";
	public static final String ADMIN_MODULE_ROOT = WebConstants.MODULE_BASE + CashierModuleConstants.MODULE_NAME
	        + "/admin/";
	public static final String SEQ_RECEIPT_NUMBER_GENERATOR_PAGE = ADMIN_MODULE_ROOT + "seqReceiptNumberGenerator";
	public static final String SEQ_RECEIPT_NUMBER_GENERATOR_PAGE_2X = ADMIN_MODULE_ROOT + "seqReceiptNumberGenerator2x";
	public static final String RECEIPT_NUMBER_GENERATOR_ROOT = ADMIN_MODULE_ROOT + "receiptNumberGenerator";
	public static final String RECEIPT_NUMBER_GENERATOR_ROOT_2X = ADMIN_MODULE_ROOT + "receiptNumberGenerator2x";
	public static final String RECEIPT_NUMBER_GENERATOR_PAGE = RECEIPT_NUMBER_GENERATOR_ROOT + ".form";
	public static final String RECEIPT_NUMBER_GENERATOR_PAGE_2X = RECEIPT_NUMBER_GENERATOR_ROOT_2X + ".page";
	public static final String OPENHMIS_CASHIER_MODULE_ID = "openhmis.cashier";
	public static final String LANDING_PAGE_EXTENSION_POINT_ID = "org.openmrs.module.openhmis.cashier.landing";
	public static final String MANAGE_MODULE_PAGE_EXTENSION_POINT_ID = "org.openmrs.module.openhmis.cashier.manage.module";
	public static final String TASKS_DASHBOARD_PAGE_EXTENSION_POINT_ID =
	        "org.openmrs.module.openhmis.cashier.tasks.dashboard";
	public static final String MESSAGE_PROPERTIES_JS_URI = MODULE_ROOT + "cashierMessageProperties.js";
	public static final String MESSAGE_PAGE = MODULE_ROOT + "cashierMessageProperties";
}
