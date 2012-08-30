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

package org.openmrs.module.openhmis.cashier.api.model;

import org.openmrs.BaseOpenmrsData;
import org.openmrs.Patient;
import org.openmrs.Provider;
import org.openmrs.module.openhmis.cashier.api.IScheme;

import java.util.Set;

/**
 * A Bill is a list of {@link BillLineItem}s created by a cashier for a
 * patient.  It can have BillPayments associated with it.
 * 
 * @author daniel
 *
 */
public class Bill extends BaseOpenmrsData {
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
