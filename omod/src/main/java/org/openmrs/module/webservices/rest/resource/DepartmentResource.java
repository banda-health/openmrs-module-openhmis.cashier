package org.openmrs.module.webservices.rest.resource;

import org.openmrs.annotation.Handler;
import org.openmrs.module.openhmis.cashier.api.ICashPointService;
import org.openmrs.module.openhmis.cashier.api.IDepartmentService;
import org.openmrs.module.openhmis.cashier.api.IMetadataService;
import org.openmrs.module.openhmis.cashier.api.model.CashPoint;
import org.openmrs.module.openhmis.cashier.api.model.Department;
import org.openmrs.module.webservices.rest.web.annotation.Resource;

@Resource("department")
@Handler(supports = { Department.class }, order = 0)
public class DepartmentResource extends BaseRestMetadataResource<Department> {

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

