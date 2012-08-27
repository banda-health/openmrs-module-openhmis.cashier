package org.openmrs.module.openhmis.cashier.api.model;

import java.util.Set;

import org.openmrs.BaseOpenmrsObject;
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
public class Bill extends BaseOpenmrsObject {
	/**
	 * Database ID
	 */
	private Integer billId;
	
	/**
	 * Bill/receipt number
	 */
	private String receiptNumber;
	
	private Provider cashier;
	private Patient patient;
	private Set<BillLineItem> lineItems; // Set
	private Set<IScheme> schemes; // Set
	private Set<Payment> payments; // Set
	
	// Getters & setters
	@Override
	public Integer getId() {
		return billId;
	}
	@Override
	public void setId(Integer id) {
		billId = id;
	}
	public String getReceiptNumber() {
		return receiptNumber;
	}
	public void setReceiptNumber(String number) {
		this.receiptNumber = number;
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
	public Set<BillLineItem> getLineItems() {
		return lineItems;
	}
	public void setLineItems(Set<BillLineItem> lineItems) {
		this.lineItems = lineItems;
	}
	public Set<Payment> getPayments() {
		return payments;
	}
	public void setPayments(Set<Payment> payments) {
		this.payments = payments;
	}
	public Set<IScheme> getSchemes() {
		return schemes;
	}
	public void setSchemes(Set<IScheme> schemes) {
		this.schemes = schemes;
	}
}
