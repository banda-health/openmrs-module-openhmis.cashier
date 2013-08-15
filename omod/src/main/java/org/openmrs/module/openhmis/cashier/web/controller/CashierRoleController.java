package org.openmrs.module.openhmis.cashier.web.controller;

import org.openmrs.Privilege;
import org.openmrs.Role;
import org.openmrs.api.context.Context;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@RequestMapping("/module/openhmis/cashier/role")
public class CashierRoleController {
	@RequestMapping(method = RequestMethod.GET)
	public void render(ModelMap model) {
		List<Role> roles = Context.getUserService().getAllRoles();

		model.addAttribute("roles", roles);
	}

	@RequestMapping(method = RequestMethod.POST)
	public void submit(String selectedRole, String newRoleName) {
		//send role to the server
		Role role = Context.getUserService().getRoleByUuid(selectedRole);

		if (role == null) {
			//log a message
			throw new RuntimeException("Selected role does not exist");
		}

		for (Privilege priv : getCashierPrivileges()) {
			if (!role.hasPrivilege(priv.getName())) {
				role.addPrivilege(priv);
			}
		}


		//server checks for privileges available in the role if any
		//Using the privileges extracted from the role received, the server updates the database table storing privileges for the cashier

		//1. submit()===============================
		//2.1 if(role == null) throw exception
		//3 else List<Privilege> privList= cashier.getAllPrivileges()
		//4  foreach(privList : priv)
		//  if(!role.hasPrivilege(priv))
		//		role.addPrivilege(priv)
		//end
	}

	private List<Privilege> getCashierPrivileges() {
		return null;
	}
}
