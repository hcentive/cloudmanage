'use strict';

/**
 * @ngdoc function
 * @name uiApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the uiApp
 */
angular.module('cloudmanageApp')
  .controller('MainCtrl', ['$scope','authenticationService','securityContextHolder','$rootScope', 
  	'pollingService','logoutService','$state','$log',
  	function ($scope, authenticationService, securityContextHolder, $rootScope, pollingService, logoutService, $state, $log) {
	  	var vm = this;
	  	vm.securityContextHolder = securityContextHolder;
	  	activate();
	  	function activate(){
	  		authenticationService.authenticate();
	  	}

	  	vm.logout = function(){
	  		logoutService.logout().then(function(){
	  			$state.go('login');  
	  		},
	  		function(){
	  			$log.error("unable to logout");
	  		});
	  	}

	  	$rootScope.$on('$stateChangeStart', function(){
	  		pollingService.deregister();
	  	});

  }]);
