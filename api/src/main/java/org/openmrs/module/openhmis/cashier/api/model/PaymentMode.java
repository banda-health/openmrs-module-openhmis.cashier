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

import org.openmrs.BaseOpenmrsMetadata;

import java.util.List;
import java.util.Vector;

/**
 * Class to represent a mode of payment (e.g., cash, check, credit card)
 * @author daniel
 *
 */
public class PaymentMode extends BaseOpenmrsMetadata {
	private Integer paymentModeId;
	private List<PaymentModeAttributeType> attributeTypes;

	// Getters & setters
	@Override
	public Integer getId() {
		return paymentModeId;
	}

	@Override
	public void setId(Integer id) {
		paymentModeId = id;
	}

	public PaymentModeAttributeType addAttributeType(String name, String format, String regExp, boolean required) {
		PaymentModeAttributeType attributeType = new PaymentModeAttributeType();

		attributeType.setPaymentMode(this);

		attributeType.setName(name);
		attributeType.setFormat(format);
		attributeType.setRegExp(regExp);
		attributeType.setRequired(required);

		addAttributeType(attributeType);

		return attributeType;
	}

	public void addAttributeType(PaymentModeAttributeType attributeType) {
		if (attributeType == null) {
			throw new NullPointerException("The payment mode attribute type to add must be defined.");
		}

		if (attributeType.getPaymentMode() != this) {
			attributeType.setPaymentMode(this);
		}

		if (this.attributeTypes == null) {
			this.attributeTypes = new Vector<PaymentModeAttributeType>();
		}

		this.attributeTypes.add(attributeType);
	}

	public void removeAttributeType(PaymentModeAttributeType attributeType) {
		if (attributeType != null && this.attributeTypes != null) {
			this.attributeTypes.remove(attributeType);
		}
	}

	public List<PaymentModeAttributeType> getAttributeTypes() {
		return attributeTypes;
	}

	public void setAttributeTypes(List<PaymentModeAttributeType> attributeTypes) {
		this.attributeTypes = attributeTypes;
	}
}
