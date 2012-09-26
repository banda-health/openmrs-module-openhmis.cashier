package org.openmrs.module.webservices.rest.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openmrs.module.webservices.rest.SimpleObject;
import org.openmrs.module.webservices.rest.resource.ItemResource;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestUtil;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.response.ResourceDoesNotSupportOperationException;
import org.openmrs.module.webservices.rest.web.response.ResponseException;
import org.openmrs.module.webservices.rest.web.v1_0.controller.BaseCrudController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/rest/item")
public class ItemController extends BaseCrudController<ItemResource> {
	/**
	 * @param query
	 * @param request
	 * @param response
	 * @return
	 * @throws ResponseException
	 */
	@RequestMapping(method = RequestMethod.GET, params = {"q", "department_uuid"})
	@ResponseBody
	public SimpleObject search(@RequestParam("q") String query, @RequestParam("department_uuid") String department_uuid, HttpServletRequest request, HttpServletResponse response)
	        throws ResponseException {
		ItemResource itemResource;
		try {
			itemResource = (ItemResource) getResource();
		}
		catch (ClassCastException ex) {
			throw new ResourceDoesNotSupportOperationException(getResource().getClass().getSimpleName()
			        + " is not Searchable", null);
		}
		RequestContext context = RestUtil.getRequestContext(request, Representation.REF);
		return itemResource.search(query, department_uuid, context);
	}

}
