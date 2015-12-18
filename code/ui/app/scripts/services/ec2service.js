'use strict';

/**
 * @ngdoc service
 * @name cloudmanageApp.ec2Service
 * @description
 * # ec2Service
 * Service in the cloudmanageApp.
 */
 angular.module('cloudmanageApp')
 .service('ec2Service', ['$http','$log',function ec2Service($http, $log) {

    var performActionOnInstance = function(action, instanceID){
        var url = '/instances/' + instanceID,
        params = {
            action: action
        };
        return $http.put(url,{},{
            params: params
        },{
             headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            }
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

    var getTagValue = function(instance, tagKey){
        var tags = instance.awsInstance.tags,
        targetTag = _.find(tags, function(tag){
            if(tag.key === tagKey)
              return true;
          return false;
      });
        return (targetTag)? targetTag.value : '-';
    }


    this.getVpcs = function(){
      return $http.get('/vpcs').then(function(response){
        return response.data;
        });
      };

    this.getInstances = function(groups, polling){
        return fetchData('/instances', groups, polling);
    };

    this.getResourcesMetaData = function(groups, polling){
       return fetchData('/dashboard/resourceMetaData', groups, polling);
    };


    this.stopInstance = function(instance){
        return performActionOnInstance('stop', this.getInstanceId(instance));
    };

    this.startInstance = function(instance){
        return performActionOnInstance('start', this.getInstanceId(instance));
    };

    this.terminateInstance = function(instance){
        return performActionOnInstance('terminate', this.getInstanceId(instance));
    };

    this.getInstanceName = function(instance){
        return getTagValue(instance, 'Name');
    };

    this.getCostCenter = function(instance){
        return getTagValue(instance, 'cost-center');            
    };

    this.getInstanceId = function(instance){
        return instance.awsInstance.instanceId
    };

    this.schedule = function(instance, startCron, stopCron){
        var costCenter = this.getCostCenter(instance),
        instanceId = this.getInstanceId(instance);
        return $http.post('/instances/schedule/'+instanceId,{
            costCenter : costCenter,
            startCron : startCron,
            stopCron : stopCron
        },{
             headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            }
          }).catch(function(data){
            $log.error(data);
        });
    };

    this.deleteSchedule = function(instance){
       var costCenter = this.getCostCenter(instance),
       instanceId = this.getInstanceId(instance);
       return $http.post('/instances/schedule/delete/'+instanceId,{
                costCenter : costCenter
            },{
             headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
                }
            }).catch(function(response){
            $log.error(response);
        });
    };

    this.getJobDetails = function(instance){
        var instanceId = this.getInstanceId(instance);
        return $http.get('/instances/schedule/'+ instanceId)
        .then(function(data){
            return data.data;
        }, function(data){
            $log.error(data);
        });
    };


}]);
