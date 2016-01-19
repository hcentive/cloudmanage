'use strict';

/**
 * @ngdoc service
 * @name cloudmanageApp.buildService
 * @description
 * # buildService
 * Service in the cloudmanageApp.
 */
angular.module('cloudmanageApp')
  .service('buildService', ['$http', function ($http) {
  	 	this.getList = function(){
    		return $http.get('/build/list')
    			.then(function(response){
                	return response.data.jobs;
            	});
    	};
    	this.getDetail = function(jobName){
    		return $http.get('/build/'+jobName)
    			.then(function(response){
    				return response.data;
    			});
    	};

  }]);
