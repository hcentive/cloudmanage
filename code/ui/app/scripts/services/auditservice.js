'use strict';

/**
 * @ngdoc service
 * @name cloudmanageApp.auditService
 * @description
 * # auditService
 * Service in the cloudmanageApp.
 */
angular.module('cloudmanageApp')
  .service('auditService', ['$http', function ($http) {
    this.getList = function(){
    	return $http.get('/audit/list')
    			.then(function(response){
                	return response.data;
            	});
    };
  }]);
