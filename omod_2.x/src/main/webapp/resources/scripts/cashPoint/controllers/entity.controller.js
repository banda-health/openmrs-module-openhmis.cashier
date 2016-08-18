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
    base.controller("CashpointController", CashpointController);
    CashpointController.$inject = ['$stateParams', '$injector', '$scope', '$filter', 'EntityRestFactory', 'CashpointModel', 'CashpointRestfulService'];

    function CashpointController($stateParams, $injector, $scope, $filter, EntityRestFactory, CashpointModel, CashpointRestfulService) {
        var self = this;

        var entity_name_message_key = "openhmis.cashier.cashPoint.name";
        var REST_ENTITY_NAME = "cashPoint";

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
                self.loadLocations();
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
                    emr.errorAlert(emr.message("openhmis.cashier.cashPoint.name.required"));
                    return false;
                }

                return true;
            }

        self.loadLocations = self.loadLocations || function(){
                CashpointRestfulService.loadLocations(CASHIER_MODULE_NAME, self.onLoadLocationsSuccessful);
            }

        //callback
        self.onLoadLocationsSuccessful = self.onLoadLocationsSuccessful || function(data){
                $scope.locations = data.results;
                $scope.location = $scope.location || emr.message('openhmis.commons.general.anyLocation')
            }

        /* ENTRY POINT: Instantiate the base controller which loads the page */
        $injector.invoke(base.GenericEntityController, self, {
            $scope: $scope,
            $filter: $filter,
            $stateParams: $stateParams,
            EntityRestFactory: EntityRestFactory,
            GenericMetadataModel: CashpointModel
        });
    }
})();
