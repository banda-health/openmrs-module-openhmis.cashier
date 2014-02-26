/*
 * The contents of this file are subject to the OpenMRS Public License
 * Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */

package org.openmrs.module.openhmis.cashier.api.util;

import org.openmrs.annotation.AddOnStartup;

public class CashierPrivilegeConstants {
	@AddOnStartup(description = "Able to add and edit bills")
	public static final String MANAGE_BILLS = "Manage Cashier Bills";

	@AddOnStartup(description = "Able to adjust bills")
	public static final String ADJUST_BILLS = "Adjust Cashier Bills";

	@AddOnStartup(description = "Able to create/adjust a bill so that it refunds money")
	public static final String REFUND_MONEY = "Refund Money";

	@AddOnStartup(description = "Able to print bill receipts more than once")
	public static final String REPRINT_RECEIPT = "Reprint Receipt";

	@AddOnStartup(description = "Able to view bills")
	public static final String VIEW_BILLS = "View Cashier Bills";
	public static final String PURGE_BILLS = "Purge Cashier Bills";

	@AddOnStartup(description = "Able to add/edit/delete cashier module metadata")
	public static final String MANAGE_METADATA = "Manage Cashier Metadata";

	@AddOnStartup(description = "Able to view cashier module metadata")
	public static final String VIEW_METADATA = "View Cashier Metadata";
	public static final String PURGE_METADATA = "Purge Cashier Metadata";

	@AddOnStartup(description = "Able to add/edit/delete timesheets")
	public static final String MANAGE_TIMESHEETS = "Manage Cashier Timesheets";

	@AddOnStartup(description = "Able to view timesheets")
	public static final String VIEW_TIMESHEETS = "View Cashier Timesheets";
	public static final String PURGE_TIMESHEETS = "Purge Cashier Timesheets";
	public static final String GIVE_REFUND = "Give Refund";

	public static final String VIEW_CASHIER_ITEMS = "View Cashier Items";
	public static final String MANAGE_CASHIER_ITEMS = "Manage Cashier Items";
	public static final String PURGE_CASHIER_ITEMS = "Purge Cashier Items";

}
