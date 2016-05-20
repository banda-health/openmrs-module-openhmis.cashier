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
 *
 */

(function () {
	'use strict';
	
	var app = angular.module('app.timesheetFunctionsFactory', []);
	app.service('TimesheetFunctions', TimesheetFunctions);
	
	TimesheetFunctions.$inject = [];
	
	function TimesheetFunctions() {
		var service;
		
		service = {
			addMessageLabels: addMessageLabels,
			formatDate: formatDates,
		};
		
		return service;
		
		/**
		 * All message labels used in the UI are defined here
		 * @returns {{}}
		 */
		function addMessageLabels() {
			var messages = {};
			return messages;
		}
		
		function formatDates(date) {
			var formattedDate = ("0"+(date.getMonth()+1)).slice(-2) + "/" + ("0" + date.getDate()).slice(-2) + "/" +
				date.getFullYear() + " " + ("0" + date.getHours()).slice(-2) + ":" + ("0" + date.getMinutes()).slice(-2);
			return formattedDate;
		}
	}
})();
