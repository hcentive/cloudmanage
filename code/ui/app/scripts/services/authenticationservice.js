'use strict';

/**
 * @ngdoc service
 * @name cloudmanageApp.authenticationService
 * @description
 * # authenticationService
 * Service in the cloudmanageApp.
 */
angular.module('cloudmanageApp')
  .service('authenticationService', ['$http',function authenticationService($http) {

  	var principal = null;

  	this.getPrincipal = function(credentials){
  		var headers = credentials ? {authorization : "Basic "
        + btoa(credentials.username + ":" + credentials.password)
    	} : {};
  		return $http.get('/user', {
  			headers : headers,
        cache: true
  		});
  	};

  	this.authenticate = function(credentials){
  		return this.getPrincipal(credentials);
  	}
    
  }]);
