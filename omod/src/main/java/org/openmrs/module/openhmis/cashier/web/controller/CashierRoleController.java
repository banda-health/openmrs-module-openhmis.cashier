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
package org.openmrs.module.openhmis.cashier.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Privilege;
import org.openmrs.Role;
import org.openmrs.api.APIException;
import org.openmrs.api.UserService;
import org.openmrs.module.jasperreport.util.JasperReportPrivilegeConstants;
import org.openmrs.module.openhmis.cashier.api.util.CashierPrivilegeConstants;
import org.openmrs.module.openhmis.cashier.model.CashierRole;
import org.openmrs.module.openhmis.cashier.web.CashierWebConstants;
import org.openmrs.util.RoleConstants;
import org.openmrs.web.WebConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping(CashierWebConstants.CASHIER_ROLE_ROOT)
public class CashierRoleController {

	private static final Log LOG = LogFactory.getLog(CashierRoleController.class);

	private UserService userService;

	@Autowired
	public CashierRoleController(UserService userService) {
		this.userService = userService;
	}

	@RequestMapping(method = RequestMethod.GET)
	public void render(ModelMap model) {
		List<Role> roles = userService.getAllRoles();
		model.addAttribute("roles", roles);
	}

	@RequestMapping(method = RequestMethod.POST)
	public void submit(HttpServletRequest request, CashierRole cashierRole, Errors errors, ModelMap model) throws Exception{
		HttpSession session = request.getSession();
		String param = request.getParameter("role");

		if (param.equals("add")) {
			addCashierPrivileges(cashierRole.getPrivAdded());
			session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "openhmis.cashier.roleCreation.page.feedback.add");
		} else if (param.equals("remove")) {
			removeCashierPrivileges(cashierRole.getPrivRemoved());

			session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "openhmis.cashier.roleCreation.page.feedback.remove");
		} else if (param.equals("new") && newRoleValidated(cashierRole, errors)) {
			createRole(cashierRole, session);
		}

		render(model);
	}

	private boolean newRoleValidated(CashierRole cashierRole, Errors errors) throws Exception {
		if (cashierRole.getNewCashierRole() == "") {
			errors.rejectValue("role", "openhmis.cashier.roleCreation.page.feedback.error.blankRole");
			return false;
		} else if (checkForDuplicateRole(cashierRole.getNewCashierRole())) {
			errors.rejectValue("role", "openhmis.cashier.roleCreation.page.feedback.error.existingRole");
			return false;
		}
		return true;
	}

	private void createRole(CashierRole cashierRole, HttpSession session) throws Exception {
		createNewRole(cashierRole.getNewCashierRole());
		session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "openhmis.cashier.roleCreation.page.feedback.new");
	}

	private Boolean checkForDuplicateRole(String role) {
		for (Role name : userService.getAllRoles()) {
			if (name.getRole().equals(role)) {
				return true;
			}
		}
		return false;
	}

	private void addCashierPrivileges(String selectedRole) throws Exception {
		Role role = userService.getRoleByUuid(selectedRole);
		if (role == null) {
			throw new APIException("The role '" + selectedRole + "' could not be found.");
		}

		for (Privilege priv : getCashierPrivileges()) {
			if (!role.hasPrivilege(priv.getName())) {
				role.addPrivilege(priv);
			}
		}
		saveRole(role);
	}

	private void removeCashierPrivileges(String selectedRole) throws Exception {
		Role role = userService.getRoleByUuid(selectedRole);

		if (role == null) {
			throw new APIException("The role '" + selectedRole + "' could not be found.");
		}

		for (Privilege priv : getCashierPrivileges()) {
			if (role.hasPrivilege(priv.getName())) {
				role.removePrivilege(priv);
			}
		}
		saveRole(role);
	}

	private void createNewRole(String newCashierRole) throws Exception {
		Role inheritedRole = userService.getRole(RoleConstants.PROVIDER);
		Set<Role> inheritedRoles = new HashSet<Role>();
		inheritedRoles.add(inheritedRole);

		Role newRole = new Role();
		newRole.setRole(newCashierRole);
		newRole.setDescription("Creates/manages patient bills");
		newRole.setInheritedRoles(inheritedRoles);
		newRole.setPrivileges(getCashierPrivileges());

		saveRole(newRole);
	}

	private Set<Privilege> getCashierPrivileges() {
		Set<Privilege> privileges = new HashSet<Privilege>();

		privileges.add(userService.getPrivilege(CashierPrivilegeConstants.VIEW_BILLS));
		privileges.add(userService.getPrivilege(CashierPrivilegeConstants.MANAGE_BILLS));
		privileges.add(userService.getPrivilege(CashierPrivilegeConstants.ADJUST_BILLS));
		privileges.add(userService.getPrivilege(CashierPrivilegeConstants.GIVE_REFUND));
		privileges.add(userService.getPrivilege(CashierPrivilegeConstants.REPRINT_RECEIPT));
		privileges.add(userService.getPrivilege(CashierPrivilegeConstants.PURGE_BILLS));
		privileges.add(userService.getPrivilege(CashierPrivilegeConstants.VIEW_CASHIER_ITEMS));
		privileges.add(userService.getPrivilege(CashierPrivilegeConstants.MANAGE_CASHIER_ITEMS));
		privileges.add(userService.getPrivilege(CashierPrivilegeConstants.PURGE_CASHIER_ITEMS));
		privileges.add(userService.getPrivilege(CashierPrivilegeConstants.VIEW_METADATA));
		privileges.add(userService.getPrivilege(CashierPrivilegeConstants.MANAGE_METADATA));
		privileges.add(userService.getPrivilege(CashierPrivilegeConstants.PURGE_METADATA));
		privileges.add(userService.getPrivilege(CashierPrivilegeConstants.VIEW_TIMESHEETS));
		privileges.add(userService.getPrivilege(CashierPrivilegeConstants.MANAGE_TIMESHEETS));
		privileges.add(userService.getPrivilege(CashierPrivilegeConstants.PURGE_TIMESHEETS));
		privileges.add(userService.getPrivilege(JasperReportPrivilegeConstants.VIEW_JASPER_REPORTS));

		return privileges;
	}

	private void saveRole(Role role) throws Exception {
		try {
			userService.saveRole(role);
		} catch(Exception e) {
			LOG.error("The role '" + role.getName() + "' could not be saved.", e);
			throw e;
		}
	}
}
