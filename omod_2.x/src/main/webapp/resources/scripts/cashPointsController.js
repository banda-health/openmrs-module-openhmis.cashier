var cashPointsApp = angular.module('cashPointsApp', ['ngResource', 'ngSanitize']);

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
            return { "text-decoration": "line-through" };
        } else {
            return {};
        }
    }
    $scope.fetchAllCashPoints = function() {
        CashPointsService.query({
            "includeAll": true
        }, function(response) {
            initialize($scope, response, true);
            $scope.originalCashPointsResponse = response;
        });
    }
    $scope.fetchNonRetiredCashPoints = function() {
        CashPointsService.query(function(response) {
            initialize($scope, response, false);
            $scope.originalCashPointsResponse = response;
        });
    }
    $scope.setLocationFilter = function() {
        if ($scope.selectedLocation === (emr.message('Location.hierarchy.heading') === "Location.hierarchy.heading" ? "All Locations" : emr.message('Location.hierarchy.heading'))) {
            initialize($scope, $scope.originalCashPointsResponse, $scope.includeRetired === undefined ? false : $scope.includeRetired);
        } else {
            initialize($scope, $scope.originalCashPointsResponse, $scope.includeRetired === undefined ? false : $scope.includeRetired, $scope.selectedLocation);
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
});

/*Adds a new a-disabled directive to povide the same functionality as ng-disabled for anchors/links(<a>)*/
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

//initialize a new startFrom filter
cashPointsApp.filter('startFrom', function() {
    return function(input, start) {
        if (!input || !input.length) {
            return;
        }
        start = +start; //parse to int
        return input.slice(start);
    }
});

function initialize(scopeObj, response, includeRetired, location) {
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

function getMactchedCashPoints(uuid, response) {
    var cashPointsArr = [];

    for (j = 0; j < response.results.length; j++) {
        var loc = (response.results[j].location === undefined || response.results[j].location === null) ? "" : response.results[j].location.uuid;

        if (loc === uuid) {
            cashPointsArr.push(response.results[j]);
        }
    }
    return cashPointsArr;
};

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
    	async:false,
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