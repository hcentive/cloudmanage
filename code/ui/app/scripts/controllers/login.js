'use strict';

/**
 * @ngdoc function
 * @name cloudmanageApp.controller:LoginCtrl
 * @description
 * # LoginCtrl
 * Controller of the cloudmanageApp
 */
angular.module('cloudmanageApp')
  .controller('LoginCtrl', ['authenticationService','$state', function (authenticationService, $state) {
    var that = this;
  	this.credentials = {
  	
    };

  	this.login = function(){
  		authenticationService.authenticate(this.credentials)
  		.then(function(data){
        that.error = false;
  			$state.go('home');
  		},
  		function(){
        that.error = true;
  			console.log(arguments);
  		});
  	};
    
  }]);
