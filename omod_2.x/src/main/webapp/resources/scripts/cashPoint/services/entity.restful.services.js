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

    angular.module('app.restfulServices').service('CashpointRestfulService', CashpointRestfulService);

    CashpointRestfulService.$inject = ['EntityRestFactory', 'PaginationService'];

    function CashpointRestfulService(EntityRestFactory, PaginationService) {
        var service;

        service = {
            searchCashpoints : searchCashpoints,
            loadLocations: loadLocations,
        };

        return service;

        /**
         * search cashpoints
         * @param rest_entity_name
         * @param location_uuid
         * @param currentPage
         * @param limit
         * @param includeRetired
         * @param q
         * @param onSearchStockRoomsSuccessful
         */
        function searchCashpoints(rest_entity_name, location_uuid, currentPage, limit, includeRetired, q, onSearchCashpointsSuccessful){
            var requestParams = PaginationService.paginateParams(currentPage, limit, includeRetired, q);
            requestParams['rest_entity_name'] = rest_entity_name;
            if(angular.isDefined(location_uuid) && location_uuid !== undefined && location_uuid !== ''){
                requestParams['location_uuid'] = location_uuid;
                requestParams['q'] = q;
            }

            EntityRestFactory.loadEntities(requestParams,
                onSearchCashpointsSuccessful,
                errorCallback
            );
        }

        /**
         * Retrieve all locations
         * @param onLoadLocationsSuccessful
         */
        function loadLocations(module_name, onLoadLocationsSuccessful) {
            var requestParams = [];
            requestParams['rest_entity_name'] = '';
            requestParams['limit'] = 100;
            EntityRestFactory.setBaseUrl('location', 'v1');
            EntityRestFactory.loadEntities(requestParams,
                onLoadLocationsSuccessful,
                errorCallback
            );

            //reset base url..
            EntityRestFactory.setBaseUrl(module_name);
        }
    
        function errorCallback(error){
            emr.errorAlert(error);
        }
    }
})();
