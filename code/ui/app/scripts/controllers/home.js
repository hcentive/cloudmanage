'use strict';

/**
 * @ngdoc function
 * @name cloudmanageApp.controller:HomeCtrl
 * @description
 * # HomeCtrl
 * Controller of the cloudmanageApp
 */
angular.module('cloudmanageApp')
  .controller('HomeCtrl', ['$scope','authenticationService','$log',function ($scope,authenticationService, $log) {
  	var that = this;
    authenticationService.getPrincipal()
		.then(
			function(data){
				that.principal = data;
			},
			function(rejection){
				$log.error(rejection);
			}
		);
  }]);
