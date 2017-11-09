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
package org.openmrs.module.openhmis.cashier.web.controller;

import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openmrs.api.APIException;
import org.openmrs.api.AdministrationService;
import org.openmrs.module.openhmis.cashier.ModuleSettings;
import org.openmrs.module.openhmis.cashier.api.ICashierOptionsService;
import org.openmrs.module.openhmis.cashier.api.model.CashierOptions;

public class CashierOptionsControllerTest {

	@Mock
	private AdministrationService adminService;
	@Mock
	private ICashierOptionsService cashierOptionsService;

	private CashierOptions options;
	private CashierOptionsController cashierOptionsController;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		when(adminService.getGlobalProperty(ModuleSettings.ROUNDING_MODE_PROPERTY)).thenReturn("RM");
		cashierOptionsController = new CashierOptionsController(adminService, cashierOptionsService);

	}

	@Rule
	public ExpectedException expectedEx = ExpectedException.none();

	@Test
	public void shouldThrowAPIExceptionIfIdSetAndItemNotFound() throws Exception {
		expectedEx.expect(APIException.class);
		expectedEx.expectMessage("Rounding item ID set in options");
		options = new CashierOptions();
		options.setRoundingItemUuid(null);
		when(cashierOptionsService.getOptions()).thenReturn(options);
		when(adminService.getGlobalProperty(ModuleSettings.ROUNDING_ITEM_ID)).thenReturn("12");
		cashierOptionsController.options();
	}

	@Test
	public void shouldThrowAPIExceptionWhenRoundingIsEnabledButNoRoundingIdSet() throws Exception {
		expectedEx.expect(APIException.class);
		expectedEx.expectMessage("Rounding enabled ");
		options = new CashierOptions();
		options.setRoundToNearest(10);
		when(cashierOptionsService.getOptions()).thenReturn(options);
		when(adminService.getGlobalProperty(ModuleSettings.ROUNDING_ITEM_ID)).thenReturn(null);
		cashierOptionsController.options();
	}

}
