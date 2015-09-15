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
package org.openmrs.module.openhmis.cashier.api;

import org.openmrs.module.openhmis.cashier.api.model.CashierOptions;

/**
 * Interface that represents classes which perform data operations for {@link CashierOptions}s.
 */
public interface ICashierOptionsService {
	/**
	 * Load cashier options from wherever
	 * @return CashierOptions Loaded options
	 * @should load options
	 * @should throw APIException if a rounding item ID is specified but the item cannot be retrieved
	 * @should revert to defaults if there are problems loading options
	 */
	CashierOptions getOptions();
}
