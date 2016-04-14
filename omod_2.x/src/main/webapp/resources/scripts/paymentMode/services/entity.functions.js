
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

    var app = angular.module('app.paymentModeFunctionsFactory', []);
    app.service('PaymentModeFunctions', PaymentModeFunctions);

    PaymentModeFunctions.$inject = [];

    function PaymentModeFunctions() {
        var service;

        service = {
            addAttributeType: addAttributeType,
            editAttributeType: editAttributeType,
            removeAttributeType: removeAttributeType,
            removeAttributeTypesTemporaryId: removeAttributeTypesTemporaryId,
            addMessageLabels: addMessageLabels,
        };

        return service;

        /**
         * Displays a popup dialog box with the attribute types.
         * @param $scope
         */
        function addAttributeType($scope) {
            $scope.editAttributeTypeTitle = '';
            $scope.addAttributeTypeTitle = emr.message('openhmis.cashier.paymentMode.add.attributeType');
            var dialog = emr
                .setupConfirmationDialog({
                    selector: '#attribute-types-dialog',
                    actions: {
                        confirm: function () {
                            $scope.entity.attributeTypes = $scope.entity.attributeTypes
                                || [];
                            $scope.submitted = true;
                            if (angular.isDefined($scope.attributeType)
                                && $scope.attributeType.name !== "" && $scope.attributeType.format !== "") {
                                $scope.entity.attributeTypes.push($scope.attributeType);
                                insertOperationTypesTemporaryId($scope.entity.attributeTypes, $scope.attributeType);
                                updateAttributeOrder($scope.entity.attributeTypes, $scope.attributeType);
                                $scope.attributeType = {};
                            }

                            $scope.$apply();
                            dialog.close();
                        },
                        cancel: function () {
                            dialog.close();
                        }
                    }
                });

            dialog.show();
        }

        /**
         * Opens a popup dialog box to edit an attribute Type
         * @param attributeType
         * @param ngDialog
         * @param $scope
         */
        function editAttributeType(attributeType, $scope) {
            var tmpAttributeType = attributeType;

            var editAttributeType = {
                foreignKey : attributeType.foreignKey,
                format : attributeType.format,
                name : attributeType.name,
                regExp : attributeType.regExp,
                required : attributeType.required,
            };

            $scope.attributeType = editAttributeType;

            $scope.editAttributeTypeTitle = emr.message('openhmis.cashier.paymentMode.edit.attributeType');
            $scope.addAttributeTypeTitle = '';
            var dialog = emr.setupConfirmationDialog({
                selector: '#attribute-types-dialog',
                actions: {
                    confirm: function () {
                        tmpAttributeType.foreignKey = $scope.attributeType.foreignKey;
                        tmpAttributeType.format = $scope.attributeType.format;
                        tmpAttributeType.name = $scope.attributeType.name;
                        tmpAttributeType.regExp = $scope.attributeType.regExp;
                        tmpAttributeType.required = $scope.attributeType.required;

                        $scope.$apply();

                        updateAttributeOrder($scope.entity.attributeTypes, tmpAttributeType);
                        $scope.attributeType = {};
                        dialog.close();
                    },
                    cancel: function () {
                        $scope.attributeType = {};
                        dialog.close();
                    }
                }
            });

            dialog.show();
        }

        /**
         * ng-repeat requires that every item have a unique identifier.
         * This function sets a temporary unique id for all attribute types in the list.
         * @param operationTypes (attributeTypes)
         * @param operationType - optional
         */
        function insertOperationTypesTemporaryId(attributeTypes, attributeType) {
            var rand = Math.floor((Math.random() * 99) + 1);
            if (angular.isDefined(attributeType)) {
                var index = attributeTypes.indexOf(attributeType);
                attributeType.id = index * rand;
            } else {
                for ( var attributeType in attributeTypes) {
                    var index = attributeTypes.indexOf(attributeType);
                    attributeType.id = index * rand;
                }
            }
        }

        /* We check the index of the attribute type in the attributeTypes array. The Attribute Type
         * attributeOrder is always the same as index of the attribute type then compare and assign the
         * attributeOrder */
        function updateAttributeOrder(attributeTypes, attributeType) {
            if (angular.isDefined(attributeType)) {
                var index = attributeTypes.indexOf(attributeType);
                if (attributeType.attributeOrder != index) {
                    attributeType.attributeOrder = index;
                }
            }
        }

        function updateAttributeTypesOrder(attributeTypes){
            for(var i = 0; i < attributeTypes.length; i++){
                var attributeType = attributeTypes[i];
                if(attributeType != null) {
                    var index = attributeTypes.indexOf(attributeType);
                    if (attributeType.attributeOrder != index) {
                        attributeType.attributeOrder = index;
                    }
                }
            }
        }

        /**
         * Removes an attribute Type from the list
         * @param attribute Type
         * @param attribute Types
         */
        function removeAttributeType(attributeType, attributeTypes) {
            removeFromList(attributeType, attributeTypes);
            updateAttributeTypesOrder(attributeTypes);
        }

        /**
         * Searches an attribute type and removes it from the list
         * @param attribute type
         * @param attribute Types
         */
        function removeFromList(attributeType, attributeTypes) {
            var index = attributeTypes.indexOf(attributeType);
            if (index != -1) {
                attributeTypes.splice(index, 1);
            }
        }

        /**
         * Remove the temporary unique id from all operation types (attributetypes) before submitting.
         * @param items
         */
        function removeAttributeTypesTemporaryId(attributeTypes) {
            for (var index in attributeTypes) {
                var attributeType = attributeTypes[index];
                delete attributeType.id;
            }
        }

        /**
         * All message labels used in the UI are defined here
         * @returns {{}}
         */
        function addMessageLabels(){
            var messages = {};
            return messages;
        }
    }
})();
