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
package org.openmrs.module.openhmis.cashier.web.controller;

import org.openmrs.Privilege;
import org.openmrs.api.UserService;
import org.openmrs.module.openhmis.cashier.api.util.PrivilegeConstants;
import org.openmrs.module.openhmis.cashier.web.CashierWebConstants;
import org.openmrs.module.openhmis.commons.web.controller.RoleCreationControllerBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Set;

/**
 * Controller to manage the Cashier Role Creation page.
 */
@Controller
@RequestMapping(CashierWebConstants.CASHIER_ROLE_ROOT)
public class CashierRoleController extends RoleCreationControllerBase {
	private UserService userService;

	@Autowired
	public CashierRoleController(UserService userService) {
		this.userService = userService;
	}

	@Override
	public UserService getUserService() {
		return this.userService;
	}

	@Override
	public Set<Privilege> privileges() {
		return PrivilegeConstants.getDefaultPrivileges();
	}
}
