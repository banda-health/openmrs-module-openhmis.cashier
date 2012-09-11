package org.openmrs.module.webservices.rest.web.controller;

import org.openmrs.module.webservices.rest.resource.ItemCodeResource;
import org.openmrs.module.webservices.rest.web.v1_0.controller.BaseSubResourceController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/rest/item/{parentUuid}/code")
public class ItemCodeController extends BaseSubResourceController<ItemCodeResource> {
}
