'use strict';

/**
 * @ngdoc function
 * @name uiApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the uiApp
 */
angular.module('cloudmanageApp')
  .controller('MainCtrl', ['$scope','authenticationService','securityContextHolder', 
  	function ($scope, authenticationService, securityContextHolder) {
	  	var vm = this;
	  	vm.securityContextHolder = securityContextHolder;
	  	activate();
	  	function activate(){
	  		authenticationService.authenticate();
	  	}
  }]);
