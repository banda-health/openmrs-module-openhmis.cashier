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
(function() {
    'use strict';

    var base = angular.module('app.genericEntityController');
    base.controller("PaymentModeController", PaymentModeController);
    PaymentModeController.$inject = ['$stateParams', '$injector', '$scope', '$filter', 'EntityRestFactory', 'PaymentModeModel',
        'PaymentModeFunctions', 'EntityFunctions', 'PaymentModeRestfulService'];

    function PaymentModeController($stateParams, $injector, $scope, $filter, EntityRestFactory,
                                   PaymentModeModel, PaymentModeFunctions, EntityFunctions, PaymentModeRestfulService) {
        var self = this;

        var entity_name_message_key = "openhmis.cashier.paymentMode.name";
        var REST_ENTITY_NAME = "paymentMode";

        // @Override
        self.setRequiredInitParameters = self.setRequiredInitParameters || function() {
                self.checkPrivileges(TASK_MANAGE_METADATA);
                self.bindBaseParameters(CASHIER_MODULE_NAME, REST_ENTITY_NAME, entity_name_message_key, RELATIVE_CANCEL_PAGE_URL);
            };

        /**
         * Initializes and binds any required variable and/or function specific to entity.page
         * @type {Function}
         */
            // @Override
        self.bindExtraVariablesToScope = self.bindExtraVariablesToScope
            || function(uuid) {

                // open dialog box to add an attribute type
                $scope.addAttributeType = function () {
	                $scope.editAttributeTypeTitle = '';
	                $scope.addAttributeTypeTitle = emr.message('openhmis.cashier.paymentMode.add.attributeType');
                    EntityFunctions.addAttributeType($scope);
                    EntityFunctions.disableBackground();
                }

                // deletes an attribute type
                $scope.removeAttributeType = function (attributeType) {
	                EntityFunctions.removeAttributeType(attributeType, $scope.entity.attributeTypes);
                }

                // open dialog box to edit an attribute type
                $scope.editAttributeType = function (attributeType) {
	                $scope.editAttributeTypeTitle = emr.message('openhmis.cashier.paymentMode.edit.attributeType');
	                $scope.addAttributeTypeTitle = '';
	                EntityFunctions.editAttributeType(attributeType, $scope);
                    EntityFunctions.disableBackground();
                }

                // retrieve and load format fields..
                PaymentModeRestfulService.loadFormatFields(CASHIER_MODULE_NAME, self.onLoadFormatFieldsSuccessful);
            };

        /**
         * All post-submit validations are done here.
         * @return boolean
         */
            // @Override
        self.validateBeforeSaveOrUpdate = self.validateBeforeSaveOrUpdate || function() {
                $scope.submitted = false;
                // validate name.
                if (!angular.isDefined($scope.entity.name) || $scope.entity.name === '') {
                    $scope.submitted = true;
                    emr.errorAlert(emr.message("openhmis.cashier.paymentMode.name.required"));
                }

                // validate sort order.
                if (!angular.isDefined($scope.entity.sortOrder) || $scope.entity.sortOrder === '') {
                    $scope.submitted = true;
                    emr.errorAlert(emr.message("openhmis.cashier.paymentMode.sortOrder.required"));
                }

                // validate attribute types.
                if($scope.entity.attributeTypes === ''){
                    $scope.entity.attributeTypes = null;
                }

                if($scope.submitted){
                    return false;
                }

		        // remove temporarily assigned ids from the attribute type array lists.
		        self.removeTemporaryIds();

                return true;
            }

	    /**
	     * Removes the temporarily assigned unique ids before POSTing data
	     * @type {Function}
	     */
	    self.removeTemporaryIds = self.removeTemporaryIds || function () {
			    EntityFunctions.removeTemporaryId($scope.entity.attributeTypes);
		    }

        // @Override
        self.onChangeEntityError = self.onChangeEntityError || function (error) {
                if(error.indexOf("cashier_payment_mode_attribute_type") != -1){
                    emr.errorAlert("openhmis.cashier.general.attributeTypeInUse.error");
                }
                else{
                    emr.errorAlert(error);
                }
            }

        // callbacks..
        self.onLoadFormatFieldsSuccessful = self.onLoadFormatFieldsSuccessful || function (data) {
                $scope.formatFields = data.results;
                return EntityFunctions.addExtraFormatListElements($scope.formatFields);
            }

        /* ENTRY POINT: Instantiate the base controller which loads the page */
        $injector.invoke(base.GenericEntityController, self, {
            $scope: $scope,
            $filter: $filter,
            $stateParams: $stateParams,
            EntityRestFactory: EntityRestFactory,
            GenericMetadataModel: PaymentModeModel
        });
    }
})();
