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
package org.openmrs.module.openhmis.cashier.api;

import org.openmrs.api.APIException;

/**
 * This exception is thrown when an entity fails a validation check.
 */
public class EntityValidationException extends APIException {
	private static final long serialVersionUID = -1703463436197564311L;

	private String fieldName;

	public EntityValidationException(String fieldName, String message) {
		super(message);

		this.fieldName = fieldName;
	}

	public EntityValidationException(String fieldName, String message, Throwable throwable) {
		super(message, throwable);

		this.fieldName = fieldName;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
}
