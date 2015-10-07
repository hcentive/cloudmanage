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
                                function ($scope, groupService, ec2Service,instances, $log, pollingService) {
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

    pollingService.register(function(){
      getInstances(groupService.vm.selectedGroup, true)
    });

    function getInstances(group, polling){
      ec2Service.getInstances(group, polling)
        .then(function(data){
          that.list = data;
        }); 
    }


  	$scope.$watch(function(){
  		return groupService.vm.selectedGroup;
  	}, function(group){
      if(group){
         getInstances(group); 
      }
  	});
  }]);
