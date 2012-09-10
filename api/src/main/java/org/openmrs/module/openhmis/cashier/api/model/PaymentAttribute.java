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

import org.openmrs.BaseOpenmrsObject;

/**
 * A class to store the value of a payment mode attribute for a particular
 * payment.
 * @author daniel
 *
 */
public class PaymentAttribute extends BaseOpenmrsObject {
	private Integer paymentAttributeId;
	private Payment billPayment;
	private PaymentModeAttributeType typeAttributeType;
	private String value;

	public Integer getId() {
		return paymentAttributeId;
	}
	public void setId(Integer id) {
		paymentAttributeId = id;
	}

	// Getters & setters
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public Payment getBillPayment() {
		return billPayment;
	}
	public void setBillPayment(Payment billPayment) {
		this.billPayment = billPayment;
	}
	public PaymentModeAttributeType getTypeAttributeType() {
		return typeAttributeType;
	}
	public void setTypeAttributeType(PaymentModeAttributeType typeAttributeType) {
		this.typeAttributeType = typeAttributeType;
	}
}
