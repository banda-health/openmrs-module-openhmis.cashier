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
package org.openmrs.module.openhmis.cashier.api.impl;

import org.openmrs.api.APIException;
import org.openmrs.module.openhmis.cashier.api.ISequentialReceiptNumberGeneratorService;
import org.openmrs.module.openhmis.cashier.api.model.SequentialReceiptNumberGeneratorModel;
import org.openmrs.module.openhmis.cashier.api.security.BasicEntityAuthorizationPrivileges;

public class SequentialReceiptNumberGeneratorServiceImpl
		extends BaseEntityServiceImpl<SequentialReceiptNumberGeneratorModel, BasicEntityAuthorizationPrivileges>
		implements ISequentialReceiptNumberGeneratorService{
	@Override
	protected BasicEntityAuthorizationPrivileges getPrivileges() {
		// No authorization required
		return null;
	}

	@Override
	protected void validate(SequentialReceiptNumberGeneratorModel entity) throws APIException {
		return;
	}

	@Override
	public int reserveNextSequence(String group) {
		return 0;
	}
}
