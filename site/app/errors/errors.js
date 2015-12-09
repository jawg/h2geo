'use strict';

angular.module('h2GeoApp.errors', ['ngRoute', 'h2GeoApp.errors.ebPopover-directive'])

    .config(['$routeProvider', function ($routeProvider) {
        $routeProvider.when('/errors', {
            templateUrl: 'errors/errors.html',
            controller: 'ErrorsCtrl'
        });
    }])


    .controller('ErrorsCtrl', ['$scope', '$http', function ($scope, $http) {
        $scope.nbElementByPage = 15;

        $http.get('h2geo_errors.json')
            .then(function successCallback(response) {
                $scope.errors = response.data.data;
                $scope.page = [];
                $scope.expandCategories = [];
                $scope.nbPage = [];

                for (var i = 0; i < $scope.errors.length; i++) {
                    $scope.expandCategories[i] = false;
                    $scope.page[i] = 0;
                    $scope.nbPage[i] = Math.ceil($scope.errors[i].errors.length / $scope.nbElementByPage);
                    switch ($scope.errors[i].errorCode) {
                        case 1:
                            $scope.errors[i].errorDescription = 'There is at least one element in OSM with the given key/value tag but no wiki page.';
                            break;
                        case 2:
                            $scope.errors[i].errorDescription = 'There was no English wiki page for the given key/value.';
                            break;
                        case 3:
                            $scope.errors[i].errorDescription = 'This type of Poi was not available on nodes.';
                            break;
                        case 4:
                            $scope.errors[i].errorDescription = 'The wikidata link is missing on the osm wiki page. The wikidata is used to have all the translations of the tag.';
                            break;
                    }
                }
                console.log($scope.errors)

            }, function errorCallback(response) {
                // called asynchronously if an error occurs
                // or server returns response with an error status.
            });

        $scope.expandError = function (index) {
            $scope.expandCategories[index] = !$scope.expandCategories[index];
        };

        $scope.nextPage = function (index) {
            $scope.page[index]++;
        };

        $scope.previousPage = function (index) {
            $scope.page[index]--;
        };
    }]);
