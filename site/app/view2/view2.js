'use strict';

angular.module('myApp.view2', ['ngRoute'])

    .config(['$routeProvider', function ($routeProvider) {
        $routeProvider.when('/view2', {
            templateUrl: 'view2/view2.html',
            controller: 'View2Ctrl'
        });
    }])


    .controller('View2Ctrl', ['$scope', '$http', '$window', '$timeout', function ($scope, $http, $window, $timeout) {
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
