package org.openmrs.module.openhmis.cashier.extension.html;

import org.openmrs.module.openhmis.cashier.web.CashierWebConstants;
import org.openmrs.module.web.extension.HeaderIncludeExt;
import java.util.ArrayList;
import java.util.List;

public class CashierHeaderFullIncludeExt extends HeaderIncludeExt {
    @Override
    public List<String> getHeaderFiles() {
        List<String> files = new ArrayList<String>();
        files.add(CashierWebConstants.MODULE_RESOURCES_ROOT + "css/global.css");
        return files;
    }
}