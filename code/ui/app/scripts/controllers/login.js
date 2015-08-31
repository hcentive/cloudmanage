'use strict';

/**
 * @ngdoc function
 * @name cloudmanageApp.controller:LoginCtrl
 * @description
 * # LoginCtrl
 * Controller of the cloudmanageApp
 */
angular.module('cloudmanageApp')
  .controller('LoginCtrl', ['authenticationService','$state','authenticationSuccessHandler','authenticationFailureHandler',
    function (authenticationService, $state, authenticationSuccessHandler, authenticationFailureHandler) {
    var vm = this;
  	vm.credentials = {
  	
    };

  	this.login = function(){
  		authenticationService.authenticate(vm.credentials)
  		.then(
        function(data){
          vm.error = false;     
  		  },
  		  function(data){
          vm.error = true;    
  		  });
  	};
    
  }]);
