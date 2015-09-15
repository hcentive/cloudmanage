'use strict';

/**
 * @ngdoc function
 * @name cloudmanageApp.controller:InstanceCtrl
 * @description
 * # InstanceCtrl
 * Controller of the cloudmanageApp
 */
angular.module('cloudmanageApp')
  .controller('InstanceCtrl', ['$scope','groupService','ec2Service', function ($scope, groupService, ec2Service) {
  	var that = this;
  	$scope.$watch(function(){
  		return groupService.vm.selectedGroup;
  	}, function(group){
  		ec2Service.getInstances(group)
	    .then(function(response){
	    	that.list = response.data;
	    });
  	});
    
  }]);
