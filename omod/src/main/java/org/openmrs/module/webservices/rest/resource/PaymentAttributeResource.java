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
package org.openmrs.module.webservices.rest.resource;

import org.openmrs.annotation.Handler;
import org.openmrs.module.openhmis.cashier.api.IDataService;
import org.openmrs.module.openhmis.cashier.api.model.PaymentAttribute;

@Handler(supports = {PaymentAttribute.class}, order = 0)
public class PaymentAttributeResource extends BaseRestDataResource<PaymentAttribute> {

	@Override
	public PaymentAttribute newDelegate() {
		return new PaymentAttribute();
	}

	@Override
	public Class<IDataService<PaymentAttribute>> getServiceClass() {
		throw new RuntimeException("No service class implemented for " + getClass().getSimpleName());
	}
}
