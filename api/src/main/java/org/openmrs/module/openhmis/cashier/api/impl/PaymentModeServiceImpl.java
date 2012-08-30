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
import org.openmrs.module.openhmis.cashier.api.db.hibernate.IGenericHibernateDAO;
import org.openmrs.module.openhmis.cashier.api.model.PaymentMode;

public class PaymentModeServiceImpl extends BaseMetadataServiceImpl<IGenericHibernateDAO<PaymentMode>, PaymentMode> {
	@Override
	protected void validate(PaymentMode entity) throws APIException {
	}
}
