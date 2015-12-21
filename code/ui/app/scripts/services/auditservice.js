'use strict';

/**
 * @ngdoc service
 * @name cloudmanageApp.auditService
 * @description
 * # auditService
 * Service in the cloudmanageApp.
 */
angular.module('cloudmanageApp')
  .service('auditService', ['$http','ec2Service','$log', function ($http, ec2Service, $log) {
    this.getList = function(){
    	return $http.get('/audit/list')
    			.then(function(response){
                	return response.data;
            	});
    };
     this.getInstanceAuditList = function(instance){
        var instanceId = ec2Service.getInstanceId(instance);
        return $http.get('/audit/list/latest/'+instanceId).then(function(response){
        	return response.data;
        }, function(error){
        	$log.error(error);
        })
    }
  }]);
