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

import org.openmrs.Provider;
import org.openmrs.annotation.Handler;
import org.openmrs.module.openhmis.cashier.api.IMetadataService;

@Handler(supports = { Provider.class })
public class ProviderResource extends BaseRestMetadataResource<Provider> {

	@Override
	public Provider newDelegate() {
		return new Provider();
	}

	@Override
	public Class<IMetadataService<Provider>> getServiceClass() { return null; }
}
