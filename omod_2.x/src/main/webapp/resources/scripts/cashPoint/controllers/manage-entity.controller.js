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

        var module_name = 'cashier';
        var entity_name = emr.message("openhmis.cashier.cashPoint.name");
        var rest_entity_name = emr.message("openhmis.cashier.cashPoint.rest_name");

        // @Override
        self.getModelAndEntityName = self.getModelAndEntityName || function() {
                self.bindBaseParameters(module_name, rest_entity_name, entity_name);
            }

        // @Override
        self.bindExtraVariablesToScope = self.bindExtraVariablesToScope || function() {
                self.loadLocations();
                $scope.searchCashpointsByName = self.searchCashpointsByName;
                $scope.searchCashpoints = self.searchCashpoints;
                $scope.searchField = CookiesService.get('searchField') || $scope.searchField || '';
                $scope.postSearchMessage = $filter('EmrFormat')(emr.message("openhmis.commons.general.postSearchMessage"),
                    [self.entity_name]);
            }

        self.loadLocations = self.loadLocations || function(){
                CashpointRestfulService.loadLocations(module_name, self.onLoadLocationsSuccessful);
            }

        self.searchCashpointsByName = self.searchCashpointsByName || function(currentPage){
                // reset current page when the search field is cleared
                if($scope.searhField === undefined || $scope.searchField === ''){
                    currentPage = 1;
                    $scope.currentPage = currentPage;
                }
                self.searchCashpoints(currentPage);
            }

        self.searchCashpoints = self.searchCashpoints || function(currentPage){
                CookiesService.set('searchField', $scope.searchField);
                CookiesService.set('limit', $scope.limit);
                CookiesService.set('includeRetired', $scope.includeRetired);
                CookiesService.set('currentPage', currentPage);

                var location_uuid;
                if($scope.location !== "" && $scope.location !== null){
                    location_uuid = $scope.location.uuid;
                }

                var searchField = $scope.searchField || '';

                CashpointRestfulService.searchCashpoints(rest_entity_name, location_uuid, currentPage, $scope.limit, $scope.includeRetired, searchField, self.onSearchCashpointSuccessful);
            }

        // call back
        self.onLoadLocationsSuccessful = self.onLoadLocationsSuccessful || function(data){
                $scope.locations = data.results;
                $scope.location = $scope.location || emr.message('openhmis.commons.general.anyLocation')
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
