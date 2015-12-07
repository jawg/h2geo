'use strict';

angular.module('myApp.errors', ['ngRoute'])

    .config(['$routeProvider', function ($routeProvider) {
        $routeProvider.when('/errors', {
            templateUrl: 'errors/errors.html',
            controller: 'ErrorsCtrl'
        });
    }])


    .controller('ErrorsCtrl', ['$scope', '$http', '$window', '$timeout', function ($scope, $http, $window, $timeout) {
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
            $scope.page[index] ++;
        };

        $scope.previousPage = function (index) {
            $scope.page[index] --;
        };


    }]);
