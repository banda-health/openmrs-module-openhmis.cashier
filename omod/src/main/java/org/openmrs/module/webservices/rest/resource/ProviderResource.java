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
