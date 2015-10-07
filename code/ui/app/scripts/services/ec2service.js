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

        var performActionOnInstance = function(action, instance){
            var url = '/instances/' + instance.awsInstance.instanceId,
            params = {
                action: action
            };
            return $http.put(url,{},{
                params: params
            });
        };

        var fetchData = function(url, groups, polling){
            var params = {};
            if(groups){
                params.group = groups;
            }
            return $http.get(url,{
                params: params,
                ignoreTracker : polling
            }).then(function(response){
                return response.data;
            });
        }



    	this.getVpcs = function(){
    		return $http.get('/vpcs')
            .then(function(response){
                return response.data;
            });
    	};

    	this.getInstances = function(groups, polling){
            return fetchData('/instances', groups, polling);
    	};

        this.getResourcesMetaData = function(groups, polling){
             return fetchData('/dashboard/resourceMetaData', groups, polling);
        }


        this.stopInstance = function(instance){
            return performActionOnInstance('stop', instance);
        };

        this.startInstance = function(instance){
            return performActionOnInstance('start', instance);
        };

        this.terminateInstance = function(instance){
            return performActionOnInstance('terminate', instance);
        };
  }]);
