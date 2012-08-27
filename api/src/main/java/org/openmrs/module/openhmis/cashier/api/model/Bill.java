package org.openmrs.module.openhmis.cashier.api.model;

import java.util.List;

import org.openmrs.Patient;
import org.openmrs.Provider;
import org.openmrs.module.openhmis.cashier.api.IScheme;

/**
 * A Bill is a list of {@link BillLineItem}s created by a cashier for a
 * patient.  It can have BillPayments associated with it.
 * 
 * @author daniel
 *
 */
public class Bill {
	/**
	 * Database ID
	 */
	private Integer id;
	
	/**
	 * Bill/receipt number
	 */
	private String number;
	
	private Provider cashier;
	private Patient patient;
	private List<BillLineItem> lineItems;
	private List<IScheme> schemes;
	private List<Payment> payments;	
	
	// Getters & setters
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public Provider getCashier() {
		return cashier;
	}
	public void setCashier(Provider cashier) {
		this.cashier = cashier;
	}
	public Patient getPatient() {
		return patient;
	}
	public void setPatient(Patient patient) {
		this.patient = patient;
	}
	public List<BillLineItem> getLineItems() {
		return lineItems;
	}
	public void setLineItems(List<BillLineItem> lineItems) {
		this.lineItems = lineItems;
	}
	public List<Payment> getPayments() {
		return payments;
	}
	public void setPayments(List<Payment> payments) {
		this.payments = payments;
	}
	public List<IScheme> getSchemes() {
		return schemes;
	}
	public void setSchemes(List<IScheme> schemes) {
		this.schemes = schemes;
	}
	public Integer getId() {
		return id;
	}
}
