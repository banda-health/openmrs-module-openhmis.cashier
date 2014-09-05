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
package org.openmrs.module.openhmis.cashier.model;

public class CashierRole {
	private String privAdded;
	private String privRemoved; 
	private String newCashierRole;
	private String role;
	
	public String getPrivAdded() {
		return privAdded;
	}
	
	public void setPrivAdded(String privAdded) {
		this.privAdded = privAdded;
	}
	
	public String getPrivRemoved() {
		return privRemoved;
	}
	
	public void setPrivRemoved(String privRemoved) {
		this.privRemoved = privRemoved;
	}
	
	public String getNewCashierRole() {
		return newCashierRole;
	}
	
	public void setNewCashierRole(String newCashierRole) {
		this.newCashierRole = newCashierRole;
	}
	
	public String getRole() {
		return role;
	}
	
	public void setRole(String role) {
		this.role = role;
	}
}
