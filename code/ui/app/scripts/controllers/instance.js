'use strict';

/**
 * @ngdoc function
 * @name cloudmanageApp.controller:InstanceCtrl
 * @description
 * # InstanceCtrl
 * Controller of the cloudmanageApp
 */
angular.module('cloudmanageApp')
  .controller('InstanceCtrl', [ '$scope',
                                'ec2Service',
                                'instances',
                                '$log', 
                                'pollingService',
                                'auditService',
                                '$modal',
                                function ($scope, ec2Service,instances, $log, pollingService, auditService, $modal) {
  	var that = this;
    that.list = instances;

    that.stopInstance = function(instance){
      ec2Service.stopInstance(instance).then(function(response){
        var data = response.data;
        instance.awsInstance.state.name = data.stoppingInstances[0].currentState.name;
      });
    };

    that.startInstance = function(instance){
      ec2Service.startInstance(instance).then(function(response){
        var data = response.data;
        instance.awsInstance.state.name = data.startingInstances[0].currentState.name;
      });
    };

    that.terminateInstance = function(instance){
      ec2Service.terminateInstance(instance);
    };

    that.getInstanceName = function(instance){
      return ec2Service.getInstanceName(instance);
    };
    that.audit = function(instance){
      var modalInstance =  $modal.open({
        templateUrl: 'templates/_auditInstance.html',
        controller: 'AuditinstanceCtrl',
        controllerAs: 'auditInstanceCtrl',
        resolve: {
          instance: function () {
            return instance;
          },
          auditList: function(){
            return auditService.getInstanceAuditList(instance);
          }
        }
      });
    };
    that.schedule = function(instance){
        var modalInstance =  $modal.open({
        templateUrl: 'templates/_scheduleInstance.html',
        controller: 'ScheduleCtrl',
        controllerAs: 'scheduleCtrl',
        resolve: {
          instance: function () {
            return instance;
          },
          jobDetails: function(){
            return ec2Service.getJobDetails(instance);
          }
        }
      });
    };
    pollingService.register(function(){
      ec2Service.getInstances(true).then(function(list){
        that.list = list;
      });
    });
  }]);
