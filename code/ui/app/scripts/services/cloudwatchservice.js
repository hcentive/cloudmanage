'use strict';

/**
 * @ngdoc service
 * @name cloudmanageApp.cloudwatchService
 * @description
 * # cloudwatchService
 * Service in the cloudmanageApp.
 */
angular.module('cloudmanageApp')
 .service('cloudwatchService', ['$http',function cloudwatchService($http) {
 	var baseUrl = '/alarms/';

 	this.getAlarm = function(instanceId){
    // instanceId = "i-072aabf3537e08c62"; // only for testing
 		var url = baseUrl + "instances/" + instanceId;
 		return $http.get(url).then(function(response){
 			return response.data;
 		});
 	};
 	this.updateAlarm = function(alarm){
 		var url = baseUrl;
 		return $http.post(url,alarm,{
             	headers: {
                	'Content-Type': 'application/x-www-form-urlencoded'
                }
            });
 	};
 }]);
