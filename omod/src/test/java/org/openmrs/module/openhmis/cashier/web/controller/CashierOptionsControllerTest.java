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

    @Mock private AdministrationService adminService;
    @Mock private ICashierOptionsService cashierOptionsService;

    private CashierOptions options;
    private CashierOptionsController cashierOptionsController;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        when(adminService.getGlobalProperty(ModuleSettings.ROUNDING_MODE_PROPERTY)).thenReturn("RM");
        cashierOptionsController = new CashierOptionsController(adminService, cashierOptionsService);

    }

    @Rule public ExpectedException expectedEx = ExpectedException.none();

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
        options.setRoundToNearest(BigDecimal.TEN);
        when(cashierOptionsService.getOptions()).thenReturn(options);
        when(adminService.getGlobalProperty(ModuleSettings.ROUNDING_ITEM_ID)).thenReturn(null);
        cashierOptionsController.options();
    }


}
