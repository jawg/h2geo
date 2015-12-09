'use strict';

// Declare app level module which depends on views, and components
angular.module('h2GeoApp', [
  'ngRoute',
  'h2GeoApp.errors',
  'h2GeoApp.types'
]).
config(['$routeProvider', function($routeProvider) {
  $routeProvider.otherwise({redirectTo: '/types'});
}]);
