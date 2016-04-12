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
 *
 */
package org.openmrs.module.openhmis.cashier.web.controller;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Privilege;
import org.openmrs.Role;
import org.openmrs.api.APIException;
import org.openmrs.api.UserService;
import org.openmrs.module.openhmis.cashier.api.util.PrivilegeConstants;
import org.openmrs.module.openhmis.cashier.web.CashierWebConstants;
import org.openmrs.module.openhmis.commons.model.RoleCreationViewModel;
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

/**
 * Controller to manage the Cashier Role Creation page.
 */
@Controller
@RequestMapping(CashierWebConstants.CASHIER_ROLE_2X_ROOT)
public class CashierRole2xController {
	private static final Log LOG = LogFactory.getLog(CashierRole2xController.class);

	private UserService userService;

	@Autowired
	public CashierRole2xController(UserService userService) {
		this.userService = userService;
	}

	@RequestMapping(method = RequestMethod.GET)
	public void render(ModelMap model) {
		List<Role> roles = userService.getAllRoles();
		model.addAttribute("roles", roles);
	}

	@RequestMapping(method = RequestMethod.POST)
	public void submit(HttpServletRequest request, RoleCreationViewModel viewModel, Errors errors, ModelMap model) {
		HttpSession session = request.getSession();
		String action = request.getParameter("action");

		if (action.equals("add")) {
			addCashierPrivileges(viewModel.getAddToRole());
			session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "openhmis.cashier.roleCreation.page.feedback.add");
		} else if (action.equals("remove")) {
			removeCashierPrivileges(viewModel.getRemoveFromRole());
			session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "openhmis.cashier.roleCreation.page.feedback.remove");
		} else if (action.equals("new") && newRoleValidated(viewModel, errors)) {
			createRole(viewModel, session);
		}

		render(model);
	}

	private void addCashierPrivileges(String roleUuid) {
		Role role = userService.getRoleByUuid(roleUuid);
		if (role == null) {
			throw new APIException("The role '" + roleUuid + "' could not be found.");
		}

		for (Privilege priv : PrivilegeConstants.getDefaultPrivileges()) {
			if (!role.hasPrivilege(priv.getName())) {
				role.addPrivilege(priv);
			}
		}

		userService.saveRole(role);
	}

	private void removeCashierPrivileges(String roleUuid) {
		Role role = userService.getRoleByUuid(roleUuid);
		if (role == null) {
			throw new APIException("The role '" + roleUuid + "' could not be found.");
		}

		for (Privilege priv : PrivilegeConstants.getDefaultPrivileges()) {
			if (role.hasPrivilege(priv.getName())) {
				role.removePrivilege(priv);
			}
		}

		userService.saveRole(role);
	}

	private void createRole(RoleCreationViewModel viewModel, HttpSession session) {
		Role newRole = new Role();
		newRole.setRole(viewModel.getNewRoleName());
		newRole.setDescription("Users who creates and manage patient bills");
		newRole.setPrivileges(PrivilegeConstants.getDefaultPrivileges());

		Role inheritedRole = userService.getRole(RoleConstants.PROVIDER);
		Set<Role> inheritedRoles = new HashSet<Role>();
		inheritedRoles.add(inheritedRole);
		newRole.setInheritedRoles(inheritedRoles);

		userService.saveRole(newRole);

		session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "openhmis.cashier.roleCreation.page.feedback.new");
	}

	private boolean newRoleValidated(RoleCreationViewModel viewModel, Errors errors) {
		if (viewModel.getNewRoleName().equals(StringUtils.EMPTY)) {
			errors.rejectValue("role", "openhmis.cashier.roleCreation.page.feedback.error.blankRole");
			return false;
		} else if (checkForDuplicateRole(viewModel.getNewRoleName())) {
			errors.rejectValue("role", "openhmis.cashier.roleCreation.page.feedback.error.existingRole");
			return false;
		}

		return true;
	}

	private Boolean checkForDuplicateRole(String role) {
		for (Role name : userService.getAllRoles()) {
			if (name.getRole().equals(role)) {
				return true;
			}
		}

		return false;
	}
}
