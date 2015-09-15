'use strict';

/**
 * @ngdoc service
 * @name cloudmanageApp.groupService
 * @description
 * # groupService
 * Service in the cloudmanageApp.
 */
angular.module('cloudmanageApp')
  .service('groupService', ['$http','$q',function groupService($http, $q) {
    this.vm = {
      selectedGroup: null
    }
  	this.getList = function(){
  		return $http.get('/groups')
  				.then(
  					function(response){
  						return $q.resolve(response.data);
  					},
  					function(){
  						return $q.reject("No groups available");
	  				});
  	}
  }]);
