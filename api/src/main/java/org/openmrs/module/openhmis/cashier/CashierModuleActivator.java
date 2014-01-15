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
package org.openmrs.module.openhmis.cashier;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.Module;
import org.openmrs.module.ModuleActivator;
import org.openmrs.module.ModuleFactory;
import org.openmrs.module.openhmis.cashier.api.util.RoundingUtil;
import org.openmrs.module.openhmis.cashier.web.CashierWebConstants;
import org.openmrs.module.web.WebModuleUtil;

/**
 * This class contains the logic that is run every time this module is either started or stopped.
 */
public class CashierModuleActivator implements ModuleActivator {
	
    private static final Log LOG = LogFactory.getLog(CashierModuleActivator.class);

	/**
	 * @see ModuleActivator#willRefreshContext()
	 */
	public void willRefreshContext() {
		LOG.info("Refreshing OpenHMIS Cashier Module Module");
	}
	
	/**
	 * @see ModuleActivator#contextRefreshed()
	 */
	public void contextRefreshed() {
		LOG.info("OpenHMIS Cashier Module Module refreshed");
	}
	
	/**
	 * @see ModuleActivator#willStart()
	 */
	public void willStart() {
		LOG.info("Starting OpenHMIS Cashier Module Module");
	}
	
	/**
	 * @see ModuleActivator#started()
	 */
	public void started() {
		RoundingUtil.setupRoundingDeptAndItem(LOG);
		LOG.info("OpenHMIS Cashier Module Module started");
	}
	
	/**
	 * @see ModuleActivator#willStop()
	 */
	public void willStop() {
		LOG.info("Stopping OpenHMIS Cashier Module Module");
	}
	
	/**
	 * @see ModuleActivator#stopped()
	 */
	public void stopped() {
	    Module module = ModuleFactory.getModuleById(CashierWebConstants.OPENHMIS_CASHIER_MODULE_ID);
	    WebModuleUtil.unloadFilters(module);
		LOG.info("OpenHMIS Cashier Module Module stopped");
	}
		
}
