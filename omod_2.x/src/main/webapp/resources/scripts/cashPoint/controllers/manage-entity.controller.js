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

    var base = angular.module('app.genericManageController');
    base.controller("ManageCashpointsController", ManageCashpointsController);
    ManageCashpointsController.$inject = ['$injector', '$scope', '$filter', 'EntityRestFactory', 'CssStylesFactory',
        'PaginationService', 'CashpointModel', 'CookiesService', 'CashpointRestfulService'];

    function ManageCashpointsController($injector, $scope, $filter, EntityRestFactory, CssStylesFactory, PaginationService, CashpointModel, CookiesService, CashpointRestfulService) {

        var self = this;

        var entity_name = emr.message("openhmis.cashier.cashPoint.name");
        var REST_ENTITY_NAME = "cashPoint";

        // @Override
        self.getModelAndEntityName = self.getModelAndEntityName || function() {
                self.checkPrivileges(TASK_MANAGE_METADATA);
                self.bindBaseParameters(CASHIER_MODULE_NAME, REST_ENTITY_NAME, entity_name);
            }

        // @Override
        self.bindExtraVariablesToScope = self.bindExtraVariablesToScope || function() {
                self.loadLocations();
                $scope.searchCashpointsByName = self.searchCashpointsByName;
                $scope.searchCashpoints = self.searchCashpoints;
                $scope.searchField = CookiesService.get('searchField') || '';
                $scope.postSearchMessage = $filter('EmrFormat')(emr.message("openhmis.commons.general.postSearchMessage"),
                    [self.entity_name]);

                $scope.location = CookiesService.get('location');
                self.searchCashpointsByName(1);
            }

        // @Override
        self.paginate = self.paginate || function(page){
            }

        self.loadLocations = self.loadLocations || function(){
                CashpointRestfulService.loadLocations(CASHIER_MODULE_NAME, self.onLoadLocationsSuccessful);
            }

        self.searchCashpointsByName = self.searchCashpointsByName || function(currentPage){
                // reset current page when the search field is cleared
                if($scope.searchField === undefined || $scope.searchField === ''){
                    currentPage = 1;
                }
                $scope.currentPage = currentPage;
                self.searchCashpoints(currentPage);
            }

        self.searchCashpoints = self.searchCashpoints || function(currentPage){
                var searchField = $scope.searchField || '';
                CookiesService.set('searchField', searchField);
                CookiesService.set('limit', $scope.limit);
                CookiesService.set('includeRetired', $scope.includeRetired);
                CookiesService.set('currentPage', currentPage);

                var location_uuid = '';
                if($scope.location !== "" && $scope.location !== null && $scope.location !== undefined){
                    location_uuid = $scope.location.uuid || $scope.location;
                }
                CookiesService.set('location', location_uuid);
                CashpointRestfulService.searchCashpoints(REST_ENTITY_NAME, location_uuid, currentPage, $scope.limit, $scope.includeRetired, searchField, self.onSearchCashpointSuccessful);
            }

        // call back
        self.onLoadLocationsSuccessful = self.onLoadLocationsSuccessful || function(data){
                $scope.locations = data.results;
                var location_uuid = CookiesService.get('location');
                if(location_uuid !== null && location_uuid !== undefined && location_uuid !== ''){
                    self.setDisplayLocation($scope.locations, location_uuid);
                }
            }

        self.setDisplayLocation = self.setDisplayLocation || function(locations, uuid){
            for(var i = 0; i < locations.length; i++){
                var location = locations[i];
                if(location.uuid === uuid){
                    $scope.location = location;
                    break;
                }
            }
        }

        self.onSearchCashpointSuccessful = self.onSearchCashpointSuccessful || function(data){
                $scope.fetchedEntities = data.results;
                $scope.totalNumOfResults = data.length;
            }

        /* ENTRY POINT: Instantiate the base controller which loads the page */
        $injector.invoke(base.GenericManageController, self, {
            $scope: $scope,
            $filter: $filter,
            EntityRestFactory: EntityRestFactory,
            PaginationService: PaginationService,
            CssStylesFactory: CssStylesFactory,
            GenericMetadataModel: CashpointModel,
            CookiesService: CookiesService
        });
    }
})();
