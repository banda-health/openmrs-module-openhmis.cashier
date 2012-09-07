package org.openmrs.module.webservices.rest.resource;

import org.openmrs.OpenmrsMetadata;
import org.openmrs.module.openhmis.cashier.api.IMetadataService;

public interface IMetadataServiceResource<T extends OpenmrsMetadata> {
	public Class<IMetadataService<T>> getServiceClass();

}
