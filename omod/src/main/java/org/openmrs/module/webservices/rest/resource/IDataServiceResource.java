package org.openmrs.module.webservices.rest.resource;

import org.openmrs.OpenmrsData;
import org.openmrs.module.openhmis.cashier.api.IDataService;

public interface IDataServiceResource<T extends OpenmrsData> {

	public Class<IDataService<T>> getServiceClass();
}
