/*
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.1 (the "License"); you may not use this file except in
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

import org.openmrs.Privilege;
import org.openmrs.annotation.AddOnStartup;
import org.openmrs.module.Module;
import org.openmrs.module.ModuleFactory;

import java.util.ArrayList;
import java.util.List;

public class ModulePrivilegeConstantProcessor {
	public void process(Class<?>... classes) {
		// Load list of privileges defined by module
		List<Privilege> privileges = new ArrayList<Privilege>();
		for (Class<?> cls : classes) {
			process(cls, privileges);
		}

		// Add privileges to module privilege list
		Module module = ModuleFactory.getModuleById("");
		module.getPrivileges().addAll(privileges);
	}

	private void process(Class<?> cls, List<Privilege> privileges) {

	}

	public static Privilege createPrivilege(String name, AddOnStartup annotation) {
		return new Privilege(name, annotation.description());
	}
}
