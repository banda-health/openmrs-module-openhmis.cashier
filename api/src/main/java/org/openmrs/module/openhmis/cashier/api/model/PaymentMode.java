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

import org.openmrs.module.openhmis.commons.api.entity.model.BaseInstanceCustomizableType;

/**
 * Model class that represents a mode of payment (e.g., cash, check, credit card).
 */
public class PaymentMode extends BaseInstanceCustomizableType<PaymentModeAttributeType> {
	public static final long serialVersionUID = 0L;

	private Integer sortOrder;

	public Integer getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

	public PaymentModeAttributeType addAttributeType(String name, String format, String regExp, boolean required) {
		PaymentModeAttributeType attributeType = new PaymentModeAttributeType();

		attributeType.setOwner(this);

		attributeType.setName(name);
		attributeType.setFormat(format);
		attributeType.setRegExp(regExp);
		attributeType.setRequired(required);

		addAttributeType(attributeType);

		return attributeType;
	}
}
