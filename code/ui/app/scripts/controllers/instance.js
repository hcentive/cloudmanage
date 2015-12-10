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
                                'groupService',
                                'ec2Service',
                                'instances',
                                '$log', 
                                'pollingService',
                                '$modal',
                                function ($scope, groupService, ec2Service,instances, $log, pollingService, $modal) {
  	var that = this;
    that.list = instances;

    that.stopInstance = function(instance){
      $log.log('instance stopping', instance);
      ec2Service.stopInstance(instance);
    };

    that.startInstance = function(instance){
      $log.log('instance starting', instance);
      ec2Service.startInstance(instance);
    };

    that.terminateInstance = function(instance){
      $log.log('instance terminating', instance);
      ec2Service.terminateInstance(instance);
    };

    that.getInstanceName = function(instance){
      return ec2Service.getInstanceName(instance);
    }

    that.schedule = function(instance){
        var modalInstance =  $modal.open({
        templateUrl: 'templates/_scheduleInstance.html',
        controller: 'ScheduleCtrl',
        controllerAs: 'scheduleCtrl',
        resolve: {
          instance: function () {
            return instance;
          }
        }
      });
    }
  }]);
