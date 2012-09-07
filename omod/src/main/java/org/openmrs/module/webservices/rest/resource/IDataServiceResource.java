package org.openmrs.module.webservices.rest.resource;

import org.openmrs.OpenmrsObject;
import org.openmrs.module.openhmis.cashier.api.IDataService;

public interface IDataServiceResource<T extends OpenmrsObject> {

	public Class<IDataService<T>> getServiceClass();
}
