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

/**
 * Metadata class to describe an attribute of a payment mode.  For example, a
 * credit card mode of payment may require a transaction number as an
 * attribute.
 * @author daniel
 *
 */
public class PaymentModeAttribute extends BaseOpenmrsMetadata {
	
	private Integer paymentModeAttributeId;
	private PaymentMode paymentMode;
	private String name;
	private String description;
	private String format;
	private String regExp;
	private Boolean required;
	private Integer order;
	

	// Getters & setters
	public Integer getId() {
		return paymentModeAttributeId;
	}
	public void setId(Integer id) {
		paymentModeAttributeId = id;
	}
	public PaymentMode getPaymentType() {
		return paymentMode;
	}
	public void setPaymentType(PaymentMode paymentType) {
		this.paymentMode = paymentType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public String getRegExp() {
		return regExp;
	}
	public void setRegExp(String regExp) {
		this.regExp = regExp;
	}
	public Boolean getRequired() {
		return required;
	}
	public void setRequired(Boolean required) {
		this.required = required;
	}
	public Integer getOrder() {
		return order;
	}
	public void setOrder(Integer order) {
		this.order = order;
	}
}
