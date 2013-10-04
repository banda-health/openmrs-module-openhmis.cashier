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
package org.openmrs.module.openhmis.cashier.api.model;

import org.openmrs.BaseOpenmrsData;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * Model class that represents the {@link Bill} payment information.
 */
public class Payment extends BaseOpenmrsData {
	private Integer paymentId;
	private Bill bill;
	private PaymentMode paymentMode;
	private Set<PaymentAttribute> attributes;
	private BigDecimal amount;
	private BigDecimal amountTendered;

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

	public PaymentAttribute addAttribute(PaymentModeAttributeType type, String value) {
		if (type == null) {
			throw new NullPointerException("The payment mode attribute type must be defined.");
		}
		if (value == null) {
			throw new NullPointerException(("The payment attribute value must be defined."));
		}

		PaymentAttribute attribute = new PaymentAttribute();
		attribute.setPaymentModeAttributeType(type);
		attribute.setPayment(this);
		attribute.setValue(value);

		addAttribute(attribute);

		return attribute;
	}

	public void addAttribute(PaymentAttribute attribute) {
		if (attribute == null) {
			throw new NullPointerException("The payment attribute to add must be defined.");
		}

		if (this.attributes == null) {
			this.attributes = new HashSet<PaymentAttribute>();
		}

		this.attributes.add(attribute);
	}

	public void removeAttribute(PaymentAttribute attribute) {
		if (attribute != null && this.attributes != null) {
			this.attributes.remove(attribute);
		}
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getAmountTendered() {
		return amountTendered;
	}

	public void setAmountTendered(BigDecimal amountTendered) {
		this.amountTendered = amountTendered;
	}

	public Bill getBill() {
		return bill;
	}

	public void setBill(Bill bill) {
		this.bill = bill;
	}
}
