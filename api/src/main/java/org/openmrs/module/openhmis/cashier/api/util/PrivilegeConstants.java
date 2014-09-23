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
package org.openmrs.module.openhmis.cashier.api.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openmrs.Privilege;
import org.openmrs.api.UserService;
import org.openmrs.api.context.Context;
import org.openmrs.module.jasperreport.util.JasperReportPrivilegeConstants;

public class PrivilegeConstants {
	public static final String MANAGE_BILLS = "Manage Cashier Bills";
	public static final String ADJUST_BILLS = "Adjust Cashier Bills";
	public static final String VIEW_BILLS = "View Cashier Bills";
	public static final String PURGE_BILLS = "Purge Cashier Bills";
	
	public static final String REFUND_MONEY = "Refund Money";
	public static final String REPRINT_RECEIPT = "Reprint Receipt";
	
	public static final String MANAGE_METADATA = "Manage Cashier Metadata";
	public static final String VIEW_METADATA = "View Cashier Metadata";
	public static final String PURGE_METADATA = "Purge Cashier Metadata";
	
	public static final String MANAGE_TIMESHEETS = "Manage Cashier Timesheets";
	public static final String VIEW_TIMESHEETS = "View Cashier Timesheets";
	public static final String PURGE_TIMESHEETS = "Purge Cashier Timesheets";
	
	public static final String[] PRIVILEGE_NAMES = new String[] { MANAGE_BILLS, ADJUST_BILLS, VIEW_BILLS, PURGE_BILLS,
	        REFUND_MONEY, REPRINT_RECEIPT, MANAGE_TIMESHEETS, VIEW_TIMESHEETS, PURGE_TIMESHEETS, MANAGE_METADATA,
	        VIEW_METADATA, PURGE_METADATA };
	
	protected PrivilegeConstants() {}
	
	/**
	 * Gets all the privileges defined by the module.
	 * @return The module privileges.
	 */
	public static Set<Privilege> getModulePrivileges() {
		Set<Privilege> privileges = new HashSet<Privilege>(PRIVILEGE_NAMES.length);
		
		UserService service = Context.getUserService();
		if (service == null) {
			throw new IllegalStateException("The OpenMRS user service cannot be loaded.");
		}
		
		for (String name : PRIVILEGE_NAMES) {
			privileges.add(service.getPrivilege(name));
		}
		
		return privileges;
	}
	
	/**
	 * Gets the default privileges needed to fully use the module.
	 * @return A set containing the default set of privileges.
	 */
	public static Set<Privilege> getDefaultPrivileges() {
		Set<Privilege> privileges = getModulePrivileges();
		
		UserService service = Context.getUserService();
		if (service == null) {
			throw new IllegalStateException("The OpenMRS user service cannot be loaded.");
		}
		
		List<String> names = new ArrayList<String>();
		// Add other required cashier privileges
		names.add(org.openmrs.module.openhmis.inventory.api.util.PrivilegeConstants.VIEW_ITEMS);
		names.add(org.openmrs.module.openhmis.inventory.api.util.PrivilegeConstants.VIEW_METADATA);
		names.add(JasperReportPrivilegeConstants.VIEW_JASPER_REPORTS);
		
		names.add(org.openmrs.util.PrivilegeConstants.ADD_ENCOUNTERS);
		names.add(org.openmrs.util.PrivilegeConstants.ADD_VISITS);
		names.add(org.openmrs.util.PrivilegeConstants.EDIT_ENCOUNTERS);
		names.add(org.openmrs.util.PrivilegeConstants.EDIT_PATIENTS);
		names.add(org.openmrs.util.PrivilegeConstants.EDIT_VISITS);
		names.add(org.openmrs.util.PrivilegeConstants.DASHBOARD_SUMMARY);
		names.add(org.openmrs.util.PrivilegeConstants.DASHBOARD_DEMOGRAPHICS);
		names.add(org.openmrs.util.PrivilegeConstants.DASHBOARD_OVERVIEW);
		names.add(org.openmrs.util.PrivilegeConstants.DASHBOARD_VISITS);
		names.add(org.openmrs.util.PrivilegeConstants.VIEW_ADMIN_FUNCTIONS);
		names.add(org.openmrs.util.PrivilegeConstants.VIEW_CONCEPTS);
		names.add(org.openmrs.util.PrivilegeConstants.VIEW_ENCOUNTERS);
		names.add(org.openmrs.util.PrivilegeConstants.VIEW_NAVIGATION_MENU);
		names.add(org.openmrs.util.PrivilegeConstants.VIEW_OBS);
		names.add(org.openmrs.util.PrivilegeConstants.VIEW_PATIENTS);
		names.add(org.openmrs.util.PrivilegeConstants.VIEW_PROVIDERS);
		names.add(org.openmrs.util.PrivilegeConstants.VIEW_VISITS);
		
		for (String name : names) {
			privileges.add(service.getPrivilege(name));
		}
		
		return privileges;
		
	}
}
