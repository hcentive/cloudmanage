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
 		var url = baseUrl + instanceId + "/cloudwatch/alarms";
 		return $http.get(url).then(function(response){
 			return response.data;
 		});
 	}
 }]);