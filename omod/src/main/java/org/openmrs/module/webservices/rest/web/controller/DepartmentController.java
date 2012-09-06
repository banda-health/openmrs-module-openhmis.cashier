package org.openmrs.module.webservices.rest.web.controller;

import org.openmrs.module.webservices.rest.resource.DepartmentResource;
import org.openmrs.module.webservices.rest.web.v1_0.controller.BaseCrudController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/rest/department")
public class DepartmentController extends BaseCrudController<DepartmentResource> { }
