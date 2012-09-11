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

import java.util.Set;

/**
 * Class to represent a mode of payment (e.g., cash, check, credit card)
 * @author daniel
 *
 */
public class PaymentMode extends BaseOpenmrsMetadata {
	private Integer paymentModeId;
	private Set<PaymentModeAttributeType> attributeTypes;

	// Getters & setters
	@Override
	public Integer getId() {
		return paymentModeId;
	}
	@Override
	public void setId(Integer id) {
		paymentModeId = id;
	}

	public Integer getPaymentModeId() {
		return paymentModeId;
	}

	public void setPaymentModeId(Integer paymentModeId) {
		this.paymentModeId = paymentModeId;
	}

	public Set<PaymentModeAttributeType> getAttributeTypes() {
		return attributeTypes;
	}
	public void setAttributeTypes(Set<PaymentModeAttributeType> attributeTypes) {
		this.attributeTypes = attributeTypes;
	}
}
