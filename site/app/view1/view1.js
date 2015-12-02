'use strict';

angular.module('myApp.view1', ['ngRoute'])

    .config(['$routeProvider', function ($routeProvider) {
        $routeProvider.when('/view1', {
            templateUrl: 'view1/view1.html',
            controller: 'View1Ctrl'
        });
    }])

    .controller('View1Ctrl', ['$scope', '$http', function ($scope, $http) {
        $http.get('h2geo.json')
            .then(function successCallback(response) {
                $scope.poiTypes = response.data.data;

            }, function errorCallback(response) {
                // called asynchronously if an error occurs
                // or server returns response with an error status.
            });
        $scope.searchFilter = function (poiType) {
            var search = $scope.search;
            if(search == null) {
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

    }]);
