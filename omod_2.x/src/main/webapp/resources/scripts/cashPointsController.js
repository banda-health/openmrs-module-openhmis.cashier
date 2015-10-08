var cashPointsApp = angular.module('cashPointsApp', ['ngResource', 'ngSanitize']);
var loadedCashPointuuid = window.location.search.split("=")[0] === "?uuid" ? window.location.search.split("=")[1] : ""; //search looks like; '?uuid=09404'

cashPointsApp.factory('CashPointsService', ['$resource',
    function($resource) {
        return $resource('../ws/rest/v2/cashier/cashPoint/:uuid', {
            uuid: '@uuid'
        }, {
            query: {
                method: 'GET',
                isArray: false
            }
        });
    }
]).controller('CashPointsController', function($scope, $http, CashPointsService) {
    var allLocationSelectOption = {
        "uuid": emr.message('Location.hierarchy.heading') === "Location.hierarchy.heading" ? "All Locations" : emr.message('Location.hierarchy.heading'),
        "display": emr.message('Location.hierarchy.heading') === "Location.hierarchy.heading" ? "All Locations" : emr.message('Location.hierarchy.heading')
    };

    $scope.locations = [allLocationSelectOption];

    $scope.triggerIncludeRetiredcashPoints = function() {
        if ($scope.includeRetired) {
            $scope.fetchAllCashPoints();
        } else {
            $scope.fetchNonRetiredCashPoints();
        }
    }
    $scope.strikeThrough = function(retired) {
        if (retired) {
            return {
                "text-decoration": "line-through"
            };
        } else {
            return {};
        }
    }
    $scope.fetchAllCashPoints = function() {
        CashPointsService.query({
            "includeAll": true
        }, function(response) {
            initializeForManagePage($scope, response, true);
            $scope.originalCashPointsResponse = response;
        });
    }
    $scope.fetchNonRetiredCashPoints = function() {
        CashPointsService.query(function(response) {
            initializeForManagePage($scope, response, false);
            $scope.originalCashPointsResponse = response;
        });
    }
    $scope.setLocationFilter = function() {
        if ($scope.selectedLocation === (emr.message('Location.hierarchy.heading') === "Location.hierarchy.heading" ? "All Locations" : emr.message('Location.hierarchy.heading'))) {
            initializeForManagePage($scope, $scope.originalCashPointsResponse, $scope.includeRetired === undefined ? false : $scope.includeRetired);
        } else {
            initializeForManagePage($scope, $scope.originalCashPointsResponse, $scope.includeRetired === undefined ? false : $scope.includeRetired, $scope.selectedLocation);
        }
    }
    $scope.fetchNonRetiredCashPoints();

    $scope.populateLocationsSelect = function() {
        $http.get("../ws/rest/v1/location").success(function(response) {
            var locs = response.results;

            for (i = 0; i < locs.length; i++) {
                $scope.locations.push(locs[i]);
            }
            reOrderLocations($scope.locations, $scope, allLocationSelectOption);
        });
    }

    $scope.populateLocationsSelect();

    /*start support for new/edit cash point page*/
    $scope.updateExistingcashPointNames = function() {
        CashPointsService.query({
            "includeAll": true
        }, function(resp) {
            $scope.fetchedcashPointsNames = [];

            for (i = 0; i < resp.results.length; i++) {
                $scope.fetchedcashPointsNames.push(resp.results[i].name.toLowerCase());
            }
        });
    }
    $scope.getAndInitialize = function(uuid) {
        CashPointsService.get({
            "uuid": uuid
        }, function(cashPoint) {
            initializeEditPage(emr, $scope, cashPoint);
        }, function(responseError) {
            initializeEditPage(emr, $scope, undefined);
            emr.errorMessage(emr.message("openhmis.cashier.cashPoint.error.notFound"));
        });
    }

    $scope.save = function() {
        CashPointsService.save({
            "name": $scope.cashPoint.name,
            "description": $scope.cashPoint.description,
            "location": $scope.cashPoint.location
        }, function(data) {
            initializeEditPage(emr, $scope, data);
            emr.successMessage(emr.message("openhmis.cashier.cashPoint.created.success"));
        });
    }

    $scope.update = function() {
        CashPointsService.save({
            "uuid": $scope.cashPoint.uuid,
            "name": $scope.cashPoint.name,
            "description": $scope.cashPoint.description,
            "location": $scope.cashPoint.location
        }, function(data) {
            initializeEditPage(emr, $scope, data);
            emr.successMessage(emr.message("openhmis.cashier.cashPoint.updated.success"));
        });
    }

    $scope.retire = function() {
        CashPointsService.remove({
            "uuid": $scope.cashPoint.uuid,
            "reason": $scope.cashPoint.retireReason
        }, function() {
            $scope.getAndInitialize($scope.cashPoint.uuid);
            emr.successMessage(emr.message("openhmis.cashier.cashPoint.retired.success"));
        });
    }

    $scope.unretire = function() {
        CashPointsService.save({
                "uuid": $scope.cashPoint.uuid,
                "retired": false
            },
            function(data) {
                $scope.getAndInitialize($scope.cashPoint.uuid);
                emr.successMessage(emr.message("openhmis.cashier.cashPoint.unretired.success"));
            });
    }
    $scope.purge = function() {
        if (confirm(emr.message("openhmis.cashier.cashPoint.confirm.delete"))) {
            CashPointsService.remove({
                "uuid": $scope.cashPoint.uuid,
                "purge": ""
            }, function() {
                window.location = "manageCashPoints.page";
                emr.successMessage(emr.message("openhmis.cashier.cashPoint.deleted.success"));
            });
        }
    }

    $scope.saveOrUpdate = function() {
        if ($scope.cashPoint.name === undefined || $scope.cashPoint.name === "") {
            emr.errorMessage(emr.message("openhmis.cashier.cashPoint.name.required"));
            $scope.nameIsRequiredMsg = emr.message("openhmis.cashier.cashPoint.name.required");
        } else if ($scope.cashPoint.location === "All Locations") {
            emr.errorMessage(emr.message("openhmis.cashier.cashPoint.location.required"));
            $scope.locationIsRequiredMsg = emr.message("openhmis.cashier.cashPoint.location.required");
        } else {
            $scope.nameIsRequiredMsg = "";
            $scope.locationIsRequiredMsg = "";
            if ($scope.cashPoint.uuid === "" || $scope.cashPoint.uuid === undefined) {
                if ($scope.fetchedcashPointsNames.indexOf($scope.cashPoint.name.toLowerCase()) === -1) {
                    $scope.nameIsRequiredMsg = "";
                    $scope.save();
                } else {
                    emr.errorMessage(emr.message("openhmis.cashier.cashPoint.error.duplicate"));
                    $scope.nameIsRequiredMsg = emr.message("openhmis.cashier.cashPoint.error.duplicate");
                }
            } else {
                $scope.update();
            }
        }
    }

    $scope.cancel = function() {
        window.location = "manageCashPoints.page";
    }

    $scope.retireOrUnretireFunction = function() {
        if ($scope.cashPoint.retired === true) {
            $scope.unretire();
        } else {
            if ($scope.cashPoint.retireReason === "") {
                emr.errorMessage(emr.message("openhmis.cashier.cashPoint.retireReason.required"));
                $scope.retireReasonIsRequiredMsg = emr.message("openhmis.cashier.cashPoint.retireReason.required");
            } else {
                $scope.retireReasonIsRequiredMsg = "";
                $scope.retire();
            }
        }
    }

    if (loadedCashPointuuid === "") {
        initializeEditPage(emr, $scope, undefined);
    } else {
        $scope.getAndInitialize(loadedCashPointuuid);
    }
});

/*Adds a new a-disabled directive to provide the same functionality as ng-disabled for anchors/links(<a>)*/
cashPointsApp.directive('aDisabled', function() {
    return {
        compile: function(tElement, tAttrs, transclude) {
            //Disable ngClick
            tAttrs["ngClick"] = "!(" + tAttrs["aDisabled"] + ") && (" + tAttrs["ngClick"] + ")";

            //return a link function
            return function(scope, iElement, iAttrs) {

                //Toggle "disabled" to class when aDisabled becomes true
                scope.$watch(iAttrs["aDisabled"], function(newValue) {
                    if (newValue !== undefined) {
                        iElement.toggleClass("disabled", newValue);
                    }
                });

                //Disable href on click
                iElement.on("click", function(e) {
                    if (scope.$eval(iAttrs["aDisabled"])) {
                        e.preventDefault();
                    }
                });
            };
        }
    };
});

//initializeForManagePage a new startFrom filter
cashPointsApp.filter('startFrom', function() {
    return function(input, start) {
        if (!input || !input.length) {
            return;
        }
        start = +start; //parse to int
        return input.slice(start);
    }
});

function initializeForManagePage(scopeObj, response, includeRetired, location) {
    scopeObj.includeRetired = includeRetired;
    scopeObj.currentPage = 0;
    scopeObj.cashPoints = location === undefined ? response.results : getMactchedCashPoints(location, response);
    scopeObj.length = scopeObj.cashPoints.length;
    if (location === undefined) {
        scopeObj.selectedLocation = emr.message('Location.hierarchy.heading') === "Location.hierarchy.heading" ? "All Locations" : emr.message('Location.hierarchy.heading');
    } else {
        scopeObj.selectedLocation = location;
    }
    scopeObj.loadcashPoint = function(uuid) {
        window.location = "cashPoint.page?uuid=" + uuid;
    }
    scopeObj.numberOfPages = function() {
        return Math.ceil(scopeObj.cashPoints.length / 10);
    }
    scopeObj.pagingFrom = function() {
        return scopeObj.currentPage <= 0 ? 1 : (scopeObj.currentPage) * 10; //TODO must the default number to be paged = 10 be available for change by the user?
    }
    scopeObj.pagingTo = function() {
        return scopeObj.currentPage <= 0 ? 10 : (scopeObj.currentPage + 1) * 10;
    }
    scopeObj.existingPages = function() {
        var pages = [];

        for (i = 1; i <= scopeObj.numberOfPages(); i++) {
            pages.push(i);
        }
        return pages;
    }
    scopeObj.loadPageByNumber = function(page) {
        scopeObj.currentPage = page - 1;
    }
    scopeObj.disAbleSinglePage = function(page) {
        if (page === scopeObj.currentPage + 1 || (page === scopeObj.currentPage + 1 && (page - 1 === 0 || page === scopeObj.numberOfPages()))) {
            return true;
        } else {
            return false;
        }
    }
}

function initializeEditPage(emr, scope, cashPoint) {
    scope.updateExistingcashPointNames();

    scope.h2SubString = (cashPoint === undefined) ? ((emr.message("general.new") === "general.new") ? "New" : emr.message("general.new")) : emr.message("general.edit");
    scope.cashPoint = {};
    scope.cashPoint.name = (cashPoint === undefined) ? "" : cashPoint.name;
    scope.cashPoint.uuid = (cashPoint === undefined) ? "" : cashPoint.uuid;
    scope.cashPoint.description = (cashPoint === undefined) ? "" : cashPoint.description;
    scope.cashPoint.retired = (cashPoint === undefined) ? false : cashPoint.retired;
    scope.cashPoint.retireReason = (cashPoint === undefined || !scope.cashPoint.retired) ? "" : cashPoint.retireReason;
    scope.retireOrUnretire = (scope.cashPoint.retired === true) ? emr.message("openhmis.cashier.cashPoint.unretire") : emr.message("openhmis.cashier.cashPoint.retire");
    scope.cashPoint.location = (cashPoint === undefined) ? (emr.message('Location.hierarchy.heading') === "Location.hierarchy.heading" ? "All Locations" : emr.message('Location.hierarchy.heading')) : cashPoint.location.uuid;
}

function getMactchedCashPoints(uuid, response) {
    var cashPointsArr = [];

    for (j = 0; j < response.results.length; j++) {
        var loc = (response.results[j].location === undefined || response.results[j].location === null) ? "" : response.results[j].location.uuid;

        if (loc === uuid) {
            cashPointsArr.push(response.results[j]);
        }
    }
    return cashPointsArr;
}

function reOrderLocations(locations, scopeObj, allLocationSelectOption) {
    var reorderedLocations = [allLocationSelectOption];
    
    for (i = 0; i < locations.length; i++) {
        var location = locations[i];

        if (location.display !== "All Locations" && location.display !== "All Locations") {
            $.ajax({
                url: location.links[0].uri,
                success: function(fetchedLocation) {
                    if (fetchedLocation !== null && fetchedLocation !== undefined &&
                        (fetchedLocation.parentLocation === null ||
                            fetchedLocation.parentLocation === undefined)) {
                        if (locationDoesnotExist(fetchedLocation.uuid, reorderedLocations)) {
                            var loc = {
                                "uuid": fetchedLocation.uuid,
                                "display": fetchedLocation.display,
                                "links": fetchedLocation.links
                            };

                            reorderedLocations.push(loc);
                            loadChildLocations(loc, reorderedLocations, 0);
                        }
                    }
                    if (reorderedLocations.length === locations.length) {
                        scopeObj.locations = reorderedLocations;
                    } else {
                        scopeObj.locations = locations;
                    }
                }
            });
        }
    }
}

function loadChildLocations(location, reorderedLocations, depth) {
    $.ajax({
        async: false,
        url: location.links[0].uri,
        success: function(fetchedLocation) {
            if (fetchedLocation !== undefined && fetchedLocation !== null) {
                var children = fetchedLocation.childLocations;

                if (children !== null && children !== undefined && children.length > 0) {
                    children.forEach(function(child) {
                        var indentation = "";
                        for (t = 0; t <= depth; t++) {
                            indentation += "&nbsp;&nbsp;";
                        }

                        var childModel = {
                            "uuid": child.uuid,
                            "display": indentation + child.display,
                            "links": child.links
                        };

                        if (locationDoesnotExist(child.uuid, reorderedLocations)) {
                            reorderedLocations.push(childModel);
                        }
                        loadChildLocations(childModel, reorderedLocations, depth + 1);
                    });
                }
            }
        }
    });
}

function locationDoesnotExist(uuid, locationsArray) {
    var exists = true;

    for (i = 0; i < locationsArray.length; i++) {
        if (uuid === locationsArray[i].uuid) {
            exists = false;
        }
    }

    return exists;
}