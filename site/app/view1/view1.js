'use strict';

angular.module('myApp.view1', ['ngRoute'])

    .config(['$routeProvider', function ($routeProvider) {
        $routeProvider.when('/view1', {
            templateUrl: 'view1/view1.html',
            controller: 'View1Ctrl'
        });
    }])

    .controller('View1Ctrl', ['$scope', '$http', '$window', '$timeout', function ($scope, $http, $window, $timeout) {
        $scope.poiTypesColumn = [];
        var nbColumn = 1;


        $http.get('h2geo.json')
            .then(function successCallback(response) {
                $scope.poiTypes = response.data.data;

                updatePoiTypeColumn($scope.poiTypes);

                console.log($scope.poiTypesColumn)
            }, function errorCallback(response) {
                // called asynchronously if an error occurs
                // or server returns response with an error status.
            });

        $scope.searchFilter = function (poiType) {
            var search = $scope.search;
            if (search == null) {
                return true;
            }

            if ($scope.sf_keywords && poiType.keyWords[$scope.language]) {
                for (var i = 0; i < poiType.keyWords[$scope.language].length; i++) {
                    var keyword = poiType.keyWords[$scope.language][i];
                    if (keyword && keyword.toLowerCase().indexOf(search.toLowerCase()) > -1) {
                        return true;

                    }
                }
            }

            return poiType.name.toLowerCase().indexOf(search.toLowerCase()) > -1;
        };

        $scope.language = navigator.language || navigator.userLanguage;

        var w = angular.element($window);

        $scope.$watch(function () {
            return $window.innerWidth;
        }, function (value) {
            var newNbColumns = Math.floor(value / 500);
            if (nbColumn != newNbColumns) {
                console.log("change column " + nbColumn + " to " + newNbColumns);
                nbColumn = newNbColumns;
                applySearch($scope.search);
            }
        });

        w.bind('resize', function () {
            $scope.$apply();
        });


        $scope.$watch('search', function (search) {
            applySearchWithDelay(search);
        });

        $scope.$watch('sf_keywords', function () {
            if (timeOut) {
                $timeout.cancel(timeOut);
            }
            applySearch($scope.search);
        });

        var timeOut;

        function applySearchWithDelay(search) {
            if (timeOut) {
                $timeout.cancel(timeOut);
            }
            timeOut = $timeout(applySearch, 400, true, search);
        }

        function applySearch(search) {
            console.log(search);

            var poiTypesToOrder = [];

            if (!search) {
                if ($scope.poiTypes != null) {
                    updatePoiTypeColumn($scope.poiTypes);
                }
                return;
            }

            for (var index = 0; index < $scope.poiTypes.length; index++) {
                var poiType = $scope.poiTypes[index];

                // check if search is in name
                if (poiType.name && poiType.name.toLowerCase().indexOf(search.toLowerCase()) > -1) {
                    poiTypesToOrder.push(poiType);
                }
                //check if search is in keywords
                else if ($scope.sf_keywords && poiType.keyWords && poiType.keyWords[$scope.language]) {
                    for (var i = 0; i < poiType.keyWords[$scope.language].length; i++) {
                        var keyword = poiType.keyWords[$scope.language][i];
                        if (keyword && keyword.toLowerCase().indexOf(search.toLowerCase()) > -1) {
                            poiTypesToOrder.push(poiType);
                            break;
                        }
                    }
                }
            }

            updatePoiTypeColumn(poiTypesToOrder);
        }

        // split the data into the various columns
        function updatePoiTypeColumn(poiTypesToOrder) {
            var newPoiTypesColumn = [];
            var i;
            for (i = 0; i < nbColumn; i++) {
                newPoiTypesColumn[i] = [];
            }

            for (i = 0; i < poiTypesToOrder.length; i++) {
                newPoiTypesColumn[i % nbColumn].push(poiTypesToOrder[i]);
            }
            $scope.poiTypesColumn = newPoiTypesColumn;
        }

    }]);
