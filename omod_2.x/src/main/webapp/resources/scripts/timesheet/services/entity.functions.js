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
	
	TimesheetFunctions.$inject = ['$filter','$window'];
	
	function TimesheetFunctions($filter,$window) {
		var service;
		
		service = {
			addMessageLabels: addMessageLabels,
			formatDate: formatDate,
			convertToDate: convertToDate,
			onChangeDatePicker: onChangeDatePicker,
			generateCashierShiftReport:generateCashierShiftReport
		};
		
		return service;
		
		/**
		 * All message labels used in the UI are defined here
		 * @returns {{}}
		 */
		function addMessageLabels() {
			var messages = {};
			messages['openhmis.cashier.page.timesheet.box.clock.in.message'] = emr
				.message('openhmis.cashier.page.timesheet.box.clock.in.message');
			messages['openhmis.cashier.page.timesheet.box.clock.out.message'] = emr
				.message('openhmis.cashier.page.timesheet.box.clock.out.message');
			return messages;
		}
		
		/**
		 * Converts the date to what to what rest accepts for timesheet lookup
		 * @params date
		 * */
		function formatDate(date) {
			var formattedDate = ($filter('date')(new Date(date), 'MM/dd/yyyy HH:mm'));
			return formattedDate;
		}
		
		/**
		 * converts the date to what the service accepts to save the timesheet.
		 * @params date
		 * */
		function convertToDate(date) {
			var convertedDate = ($filter('date')(new Date(date), "yyyy-MM-dd'T'HH:mm:ss"));
			return convertedDate;
		}
		
		/**
		 * Gets the selected for the cashier shift report 
		 * */
		function onChangeDatePicker(id, successfulCallback) {
			var datePicker = angular.element(document.getElementById(id));
			datePicker.bind('keyup change select checked', function () {
				var input = this.value;
				successfulCallback(input);
			});
		}

		/**
		 * Disable and gray-out background when a dialog box opens up.
		 */
		function disableBackground(){
			var backgroundElement = angular.element('.simplemodal-overlay');
			backgroundElement.addClass('disable-background');
		}
		
		/**
		 * Show the generate report popup
		 * @param selectorId - div id
		 * @param $scope
		 */
		function generateCashierShiftReport(selectorId,$scope){
			var dialog = emr.setupConfirmationDialog({
				selector: '#' + selectorId,
				actions: {
					cancel: function(){
						dialog.close();
					},
					confirm: function () {
						$scope.generateReport();
						$window.location.replace('/' + OPENMRS_CONTEXT_PATH + '/openhmis.cashier/cashierLanding.page');
						dialog.close();
					}
				}
			});
			
			dialog.show();
			disableBackground();
		}
	}
})();
