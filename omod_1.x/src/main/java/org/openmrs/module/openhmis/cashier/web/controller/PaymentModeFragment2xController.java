/*
 * The contents of this file are subject to the OpenMRS Public License
 * Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See
 * the License for the specific language governing rights and
 * limitations under the License.
 *
 * Copyright (C) OpenHMIS.  All Rights Reserved.
 */
package org.openmrs.module.openhmis.cashier.web.controller;

import java.util.List;
import java.util.ArrayList;

import org.codehaus.jackson.map.ObjectMapper;
import org.openmrs.Concept;
import org.openmrs.ConceptAnswer;
import org.openmrs.Drug;
import org.openmrs.Location;
import org.openmrs.User;
import org.openmrs.Provider;
import org.openmrs.Program;
import org.openmrs.ProgramWorkflow;
import org.openmrs.api.ConceptService;
import org.openmrs.api.LocationService;
import org.openmrs.api.ProviderService;
import org.openmrs.api.UserService;
import org.openmrs.api.ProgramWorkflowService;
import org.openmrs.api.context.Context;
import org.openmrs.module.webservices.rest.SimpleObject;
import org.openmrs.util.PrivilegeConstants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller to manage the payment mode 2.x fragments.
 */
@Controller(value = "paymentModeFragment2x")
@RequestMapping("/module/openhmis/cashier/paymentModeFragment2x")
public class PaymentModeFragment2xController {

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET)
	public String get(@RequestParam(value = "type") String type,
	        @RequestParam(value = "foreignKey", required = false) String foreignKey) {
		if (type.equalsIgnoreCase("location")) {
			return getLocations();
		} else if (type.equalsIgnoreCase("drug")) {
			return getDrugs();
		} else if (type.equalsIgnoreCase("concept")) {
			return getConcepts(foreignKey);
		} else if (type.equalsIgnoreCase("encounter")) {
			return "encounters";
		} else if (type.equalsIgnoreCase("user")) {
			return getUsers();
		} else if (type.equalsIgnoreCase("provider")) {
			return getProviders();
		} else if (type.equalsIgnoreCase("programworkflow")) {
			return getProgramWorkflows();
		}
		return "";
	}

	/**
	 * get concepts given a foreign key
	 * @param foreignKey
	 * @return
	 */
	private String getConcepts(String foreignKey) {
		SimpleObject results = new SimpleObject();
		if (Context.hasPrivilege(PrivilegeConstants.VIEW_CONCEPTS)) {
			try {
				ConceptService conceptService = Context.getConceptService();
				Integer requestForeignKey = Integer.valueOf(foreignKey);
				List<SimpleObject> answers = new ArrayList<SimpleObject>();
				Concept concept = conceptService.getConcept(requestForeignKey);
				if (concept != null) {
					for (ConceptAnswer answer : concept.getAnswers()) {
						SimpleObject answerObject = new SimpleObject();
						answerObject.put("id", String.valueOf(answer.getAnswerConcept().getConceptId()));
						answerObject.put("name", answer.getAnswerConcept().getName().getName());
						answers.add(answerObject);
					}
				}

				results.put("foreignKey", requestForeignKey);
				results.put("results", answers);

			} catch (Exception ex) {
				results.put("error", ex.getMessage());
			}
		} else {
			results.put("error", "Privilege required: " + PrivilegeConstants.VIEW_CONCEPTS);
		}

		return convertToJSON(results);
	}

	/**
	 * get locations
	 * @return
	 */
	private String getLocations() {
		SimpleObject results = new SimpleObject();
		if (Context.hasPrivilege(PrivilegeConstants.VIEW_LOCATIONS)) {
			try {
				LocationService locationService = Context.getLocationService();
				List<Location> locations = locationService.getAllLocations(false);
				List<SimpleObject> locationObjects = new ArrayList<SimpleObject>();
				for (Location location : locations) {
					SimpleObject locationObject = new SimpleObject();
					locationObject.put("id", String.valueOf(location.getLocationId()));
					locationObject.put("name", location.getName());
					locationObjects.add(locationObject);
				}

				results.put("results", locationObjects);

			} catch (Exception ex) {
				results.put("error", ex.getMessage());
			}
		} else {
			results.put("error", "Privilege required: " + PrivilegeConstants.VIEW_LOCATIONS);
		}

		return convertToJSON(results);
	}

	/**
	 * get drugs.
	 * @return
	 */
	private String getDrugs() {
		SimpleObject results = new SimpleObject();
		if (Context.hasPrivilege(PrivilegeConstants.VIEW_CONCEPT_CLASSES)) {
			try {
				ConceptService conceptService = Context.getConceptService();
				List<Drug> drugs = conceptService.getAllDrugs();
				List<SimpleObject> drugObjects = new ArrayList<SimpleObject>();
				for (Drug drug : drugs) {
					SimpleObject drugObject = new SimpleObject();
					drugObject.put("id", String.valueOf(drug.getDrugId()));
					drugObject.put("name", drug.getName());
					drugObjects.add(drugObject);
				}

				results.put("results", drugObjects);

			} catch (Exception ex) {
				results.put("error", ex.getMessage());
			}
		} else {
			results.put("error", "Privilege required: " + PrivilegeConstants.VIEW_CONCEPT_CLASSES);
		}

		return convertToJSON(results);
	}

	/**
	 * get users
	 * @return
	 */
	private String getUsers() {
		SimpleObject results = new SimpleObject();
		if (Context.hasPrivilege(PrivilegeConstants.VIEW_USERS)) {
			try {
				UserService userService = Context.getUserService();
				List<User> users = userService.getAllUsers();
				List<SimpleObject> userObjects = new ArrayList<SimpleObject>();
				for (User user : users) {
					SimpleObject userObject = new SimpleObject();
					userObject.put("id", String.valueOf(user.getUserId()));
					userObject.put("name", user.getUsername());
					userObjects.add(userObject);
				}

				results.put("results", userObjects);

			} catch (Exception ex) {
				results.put("error", ex.getMessage());
			}
		}
		else {
			results.put("error", "Privileges required: " + PrivilegeConstants.VIEW_USERS);
		}

		return convertToJSON(results);
	}

	/**
	 * get providers
	 * @return
	 */
	private String getProviders() {
		SimpleObject results = new SimpleObject();
		if (Context.hasPrivilege(PrivilegeConstants.VIEW_PROVIDERS)) {
			try {
				ProviderService providerService = Context.getProviderService();
				List<Provider> providers = providerService.getAllProviders();
				List<SimpleObject> providerObjects = new ArrayList<SimpleObject>();
				for (Provider provider : providers) {
					SimpleObject providerObject = new SimpleObject();
					providerObject.put("id", String.valueOf(provider.getProviderId()));
					providerObject.put("name", provider.getName());
					providerObjects.add(providerObject);
				}

				results.put("results", providerObjects);

			} catch (Exception ex) {
				results.put("error", ex.getMessage());
			}
		} else {
			results.put("error", "Privileges required: " + PrivilegeConstants.VIEW_PROVIDERS);
		}

		return convertToJSON(results);
	}

	private String getProgramWorkflows() {
		SimpleObject results = new SimpleObject();
		try {
			ProgramWorkflowService programWorkflowService = Context.getProgramWorkflowService();
			List<String> programWorkflowObjects = new ArrayList<String>();
			List<Program> programs = programWorkflowService.getAllPrograms();
			List<ProgramWorkflow> programWorkflows = new ArrayList<ProgramWorkflow>();
			for (Program program : programs) {
				programWorkflows.addAll(program.getAllWorkflows());
			}

			for (ProgramWorkflow programWorkflow : programWorkflows) {
				programWorkflowObjects.add(programWorkflow.getConcept().getName().getName());
			}

			results.put("results", programWorkflowObjects);

		} catch (Exception ex) {
			results.put("error", ex.getMessage());
		}

		return convertToJSON(results);
	}

	private String convertToJSON(SimpleObject object) {
		String jsonOutput;
		try {
			ObjectMapper mapper = new ObjectMapper();
			jsonOutput = mapper.writeValueAsString(object);
		} catch (Exception ex) {
			jsonOutput = ex.getMessage();
		}

		return jsonOutput;
	}
}
