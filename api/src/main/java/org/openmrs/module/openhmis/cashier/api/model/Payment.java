/*
 * The contents of this file are subject to the OpenMRS Public License
 * Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See
 * the License for the specific language governing rights and
 * limitations under the License.
 *
 * Copyright (C) OpenHMIS.  All Rights Reserved.
 */
package org.openmrs.module.openhmis.cashier.api.model;

import java.math.BigDecimal;

import org.openmrs.module.openhmis.commons.api.entity.model.BaseInstanceCustomizableData;

/**
 * Model class that represents the {@link Bill} payment information.
 */
public class Payment extends BaseInstanceCustomizableData<PaymentMode, PaymentAttribute> {
	public static final long serialVersionUID = 0L;

	private Integer paymentId;
	private Bill bill;
	private BigDecimal amount;
	private BigDecimal amountTendered;

	public Integer getId() {
		return paymentId;
	}

	public void setId(Integer id) {
		paymentId = id;
	}

	public PaymentAttribute addAttribute(PaymentModeAttributeType type, String value) {
		if (type == null) {
			throw new NullPointerException("The payment mode attribute type must be defined.");
		}
		if (value == null) {
			throw new NullPointerException(("The payment attribute value must be defined."));
		}

		PaymentAttribute attribute = new PaymentAttribute();
		attribute.setAttributeType(type);
		attribute.setValue(value);

		addAttribute(attribute);

		return attribute;
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
