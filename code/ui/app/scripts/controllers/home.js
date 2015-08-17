'use strict';

/**
 * @ngdoc function
 * @name cloudmanageApp.controller:HomeCtrl
 * @description
 * # HomeCtrl
 * Controller of the cloudmanageApp
 */
angular.module('cloudmanageApp')
  .controller('HomeCtrl', ['$scope','authenticationService','$state',function ($scope,authenticationService, $state) {
  	var that = this;
    authenticationService.getPrincipal()
		.then(
			function(data){
				that.principal = data;
			},
			function(){
				$state.go('login')
			}
		);
  }]);
