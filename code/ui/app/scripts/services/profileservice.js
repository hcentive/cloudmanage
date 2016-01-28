'use strict';

/**
 * @ngdoc service
 * @name cloudmanageApp.profileService
 * @description
 * # profileService
 * Service in the cloudmanageApp.
 */
angular.module('cloudmanageApp')
  .service('profileService', ['$http',function ($http) {
  	this.getProfile = function(){
  		 return $http.get('/user/profile')
  		 .then(function(response){
  		 	return response.data;
  		 });
  	};
  }]);
