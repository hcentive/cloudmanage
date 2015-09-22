'use strict';

/**
 * @ngdoc function
 * @name cloudmanageApp.controller:InstanceCtrl
 * @description
 * # InstanceCtrl
 * Controller of the cloudmanageApp
 */
angular.module('cloudmanageApp')
  .controller('InstanceCtrl', ['$scope','groupService','ec2Service','instances', function ($scope, groupService, ec2Service,instances) {
  	var that = this;
    that.list = instances;
    that.columnDef = [
      {data: 'awsInstance.instanceId'},
      {data: 'awsInstance.instanceType'},
      {data: 'awsInstance.state.name'}
    ];
  	$scope.$watch(function(){
  		return groupService.vm.selectedGroup;
  	}, function(group){
      if(group){
        ec2Service.getInstances(group)
        .then(function(data){
          that.list = data;
        });  
      }
  	});
  }]);
