package org.openmrs.module.webservices.rest.resource;

import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;
import org.codehaus.jackson.annotate.JsonTypeInfo.Id;
import org.openmrs.annotation.Handler;
import org.openmrs.module.openhmis.cashier.api.IDepartmentService;
import org.openmrs.module.openhmis.cashier.api.IMetadataService;
import org.openmrs.module.openhmis.cashier.api.model.Department;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.FullRepresentation;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;

@Resource("department")
@Handler(supports = { Department.class }, order = 0)
@JsonTypeInfo(use=Id.CLASS, include=As.PROPERTY, property="class")
public class DepartmentResource extends BaseRestMetadataResource<Department> {

	@Override
	public DelegatingResourceDescription getRepresentationDescription(
			Representation rep) {
		DelegatingResourceDescription description = getDefaultRepresentationDescription();
		description.addProperty("name");
		description.addProperty("description");			
		
		if (rep instanceof FullRepresentation) {
			description.addProperty("auditInfo", findMethod("getAuditInfo"));
		}
		return description;
	}
	
	@Override
	public DelegatingResourceDescription getCreatableProperties() {
		DelegatingResourceDescription description = getDefaultRepresentationDescription();
		description.addProperty("name");
		description.addProperty("description");
		return description;
	}

	@Override
	public Department newDelegate() {
		return new Department();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<IMetadataService<Department>> getServiceClass() {
		return (Class<IMetadataService<Department>>)(Object)IDepartmentService.class;
	}
}