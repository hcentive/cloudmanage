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
 	var baseUrl = '/instances/';

 	this.getAlarm = function(instanceId){
 	// 	instanceId = "i-072aabf3537e08c62"; // only for testing
 		var url = baseUrl + instanceId + "/cloudwatch/alarms";
 		return $http.get(url).then(function(response){
 			return response.data;
 		});
 	};
 	this.updateAlarm = function(alarm){
 		var url = baseUrl + alarm.instanceId + "/cloudwatch/alarms";
 		return $http.post(url,alarm,{
             	headers: {
                	'Content-Type': 'application/x-www-form-urlencoded'
                }
            });
 	};
 }]);
