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
import org.openmrs.module.openhmis.cashier.api.ICashPointService;
import org.openmrs.module.openhmis.cashier.api.IMetadataService;
import org.openmrs.module.openhmis.cashier.api.model.CashPoint;
import org.openmrs.module.webservices.rest.web.annotation.Resource;

@Resource("cashPoint")
@Handler(supports = { CashPoint.class }, order = 0)
public class CashPointResource extends BaseRestMetadataResource<CashPoint> {

	@Override
	public CashPoint newDelegate() {
		return new CashPoint();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<IMetadataService<CashPoint>> getServiceClass() {
		return (Class<IMetadataService<CashPoint>>)(Object)ICashPointService.class;
	}
}
