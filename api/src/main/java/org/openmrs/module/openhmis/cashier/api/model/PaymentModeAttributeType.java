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
 * Model class to describe an attribute of a payment mode.  For example, a credit card mode of payment may require
 * a transaction number as an attribute.
 */
public class PaymentModeAttributeType extends BaseOpenmrsMetadata {
	/**
	 * Note that this type can not use the org.openmrs.attribute.BaseAttributeType class because these attributes vary
	 * per payment mode, rather than being defined for all payment modes.  In the future we might want to use a
	 * similar design as BaseAttributeType or even extend it to support instance-based attribute types.
	 */

	private Integer paymentModeAttributeTypeId;
	private PaymentMode paymentMode;
	private Integer attributeOrder;

	private String format;
	private Integer foreignKey;

	private String regExp;
	private Boolean required;

	public Integer getId() {
		return paymentModeAttributeTypeId;
	}

	public void setId(Integer id) {
		paymentModeAttributeTypeId = id;
	}

	public PaymentMode getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(PaymentMode paymentMode) {
		this.paymentMode = paymentMode;
	}

	public Integer getAttributeOrder() {
		return attributeOrder;
	}

	public void setAttributeOrder(Integer attributeOrder) {
		this.attributeOrder = attributeOrder;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public Integer getForeignKey() {
		return foreignKey;
	}

	public void setForeignKey(Integer foreignKey) {
		this.foreignKey = foreignKey;
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
}
