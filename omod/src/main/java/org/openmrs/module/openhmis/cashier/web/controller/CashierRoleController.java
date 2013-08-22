package org.openmrs.module.openhmis.cashier.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Privilege;
import org.openmrs.Role;
import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.cashier.model.CashierRole;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.openmrs.util.RoleConstants;
import org.openmrs.web.WebConstants;

@Controller
@RequestMapping("/module/openhmis/cashier/cashierRole")
public class CashierRoleController {
	
	/** Logger for this class and subclasses */
	protected final Log log = LogFactory.getLog(getClass());
	
	@RequestMapping(method = RequestMethod.GET)
	public void render(ModelMap model) {
		List<Role> roles = Context.getUserService().getAllRoles();
		model.addAttribute("roles", roles);
	}

	@RequestMapping(method = RequestMethod.POST)
	public void submit(String privAdded, String privRemoved, String newCashierRole,  
			HttpServletRequest request, CashierRole cashierRole, Errors errors, ModelMap model) throws Exception{
		
		HttpSession session = request.getSession();
		String param = request.getParameter("role");
		
		if (param.equals("add")) {
			addCashierPrivileges(privAdded);
			session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "openhmis.cashier.roleCreation.page.feedback.add");
		}
		else if (param.equals("remove")) {
			removeCashierPrivileges(privRemoved);
			session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "openhmis.cashier.roleCreation.page.feedback.remove");
		}
		else if (param.equals("new") && (newCashierRole != "")) {		
			createNewRole(newCashierRole);
			session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "openhmis.cashier.roleCreation.page.feedback.new");
		}
		else if (param.equals("new") && (newCashierRole == "")) {
			errors.rejectValue("role", "openhmis.cashier.roleCreation.page.feedback.error");
		}
		else {}
		render(model);
	}
	
	private void addCashierPrivileges(String selectedRole) {
		
		Role role = Context.getUserService().getRoleByUuid(selectedRole);
		
		if (role == null) {
			//log a message
			throw new NullPointerException("Selected role does not exist");
		}

		for (Privilege priv : getCashierPrivileges()) {
			if (!role.hasPrivilege(priv.getName())) {
				role.addPrivilege(priv);
			}
		}
		saveRole(role);
	}
	
	private void removeCashierPrivileges(String selectedRole) {
		
		Role role = Context.getUserService().getRoleByUuid(selectedRole);
		
		if (role == null) {
			//log a message
			throw new NullPointerException("Selected role does not exist");
		}

		for (Privilege priv : getCashierPrivileges()) {
			if (role.hasPrivilege(priv.getName())) {
				role.removePrivilege(priv);
			}
		}
		saveRole(role);
	}
	
	private void createNewRole(String newCashierRole) {
		
		Role inheritedRole = Context.getUserService().getRole(RoleConstants.PROVIDER);
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
		
		Set<Privilege> priv_list = new HashSet<Privilege>();
		
		priv_list.add(Context.getUserService().getPrivilege("View Cashier Bills"));
		priv_list.add(Context.getUserService().getPrivilege("Manage Cashier Bills"));
		priv_list.add(Context.getUserService().getPrivilege("Adjust Cashier Bills"));
		priv_list.add(Context.getUserService().getPrivilege("Give Refund"));
		priv_list.add(Context.getUserService().getPrivilege("Reprint Receipt"));
		priv_list.add(Context.getUserService().getPrivilege("Purge Cashier Bills"));
		priv_list.add(Context.getUserService().getPrivilege("View Cashier Items"));
		priv_list.add(Context.getUserService().getPrivilege("Manage Cashier Items"));
		priv_list.add(Context.getUserService().getPrivilege("Purge Cashier Items"));
		priv_list.add(Context.getUserService().getPrivilege("View Cashier Metadata"));
		priv_list.add(Context.getUserService().getPrivilege("Manage Cashier Metadata"));
		priv_list.add(Context.getUserService().getPrivilege("Purge Cashier Metadata"));
		priv_list.add(Context.getUserService().getPrivilege("View Cashier Timesheets"));
		priv_list.add(Context.getUserService().getPrivilege("Manage Cashier Timesheets"));
		priv_list.add(Context.getUserService().getPrivilege("Purge Cashier Timesheets"));
		priv_list.add(Context.getUserService().getPrivilege("View Jasper Reports"));
		
		return priv_list;
	}
	
	private void saveRole(Role role) {
		
		try {
			Context.getUserService().saveRole(role);
		}
		catch(Exception e) {
			log.error("Error saving role", e);
		}
	}
}
