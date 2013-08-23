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
