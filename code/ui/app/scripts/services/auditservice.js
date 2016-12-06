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
    var baseUrl = "/audits";
    this.getList = function(pageSegment,pageSize,latest){
      var url = baseUrl + "?pageSegment="+ pageSegment + "&pageSize="+ pageSize +"&latest=" + latest;
    	return $http.get(url);
    };
     this.getInstanceAuditList = function(instance){
        var instanceId = ec2Service.getInstanceId(instance),
            url = baseUrl + "/latest/" + instanceId;
        return $http.get(url).then(function(response){
        	return response.data;
        }, function(error){
        	$log.error(error);
        })
    }
  }]);
