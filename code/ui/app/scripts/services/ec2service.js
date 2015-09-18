'use strict';

/**
 * @ngdoc service
 * @name cloudmanageApp.ec2Service
 * @description
 * # ec2Service
 * Service in the cloudmanageApp.
 */
angular.module('cloudmanageApp')
  .service('ec2Service', ['$http',function ec2Service($http) {

    	this.getVpcs = function(){
    		return $http.get('/vpcs')
            .then(function(response){
                return response.data;
            });
    	};
    	this.getInstances = function(groups){
    		var params = {};
    		if(groups){
    			params.group = groups;
    		}
    		return $http.get('/instances',{
    			params: params
    		}).then(function(response){
                return response.data;
            });
    	};
  }]);
