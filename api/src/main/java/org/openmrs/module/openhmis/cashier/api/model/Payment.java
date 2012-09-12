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

import java.math.BigDecimal;
import java.util.Set;

/**
 * A class to represent a payment toward a bill
 */
public class Payment extends BaseOpenmrsData {
	private Integer paymentId;
	private Bill bill;
	private PaymentMode paymentMode;
	private Set<PaymentAttribute> attributes;
	private BigDecimal amount;

	public Integer getId() {
		return paymentId;
	}	

	public void setId(Integer id) {
		paymentId = id;
	}

	public PaymentMode getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(PaymentMode mode) {
		this.paymentMode = mode;
	}

	public Set<PaymentAttribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(Set<PaymentAttribute> attributes) {
		this.attributes = attributes;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Bill getBill() {
		return bill;
	}

	public void setBill(Bill bill) {
		this.bill = bill;
	}
}
