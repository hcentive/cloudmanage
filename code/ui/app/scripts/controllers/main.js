'use strict';

/**
 * @ngdoc function
 * @name uiApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the uiApp
 */
angular.module('cloudmanageApp')
  .controller('MainCtrl', ['$scope','authenticationService','securityContextHolder','$rootScope', 'pollingService',
  	function ($scope, authenticationService, securityContextHolder, $rootScope, pollingService) {
	  	var vm = this;
	  	vm.securityContextHolder = securityContextHolder;
	  	activate();
	  	function activate(){
	  		authenticationService.authenticate();
	  	}

	  	$rootScope.$on('$stateChangeStart', function(){
	  		pollingService.deregister();
	  	});

  }]);
