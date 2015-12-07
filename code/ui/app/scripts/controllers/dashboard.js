'use strict';

/**
 * @ngdoc function
 * @name cloudmanageApp.controller:DashboardCtrl
 * @description
 * # DashboardCtrl
 * Controller of the cloudmanageApp
 */
angular.module('cloudmanageApp')
  .controller('DashboardCtrl', ['$scope','resourcesMetaData','$log','stompClient',
  	function ($scope, resourcesMetaData, $log, stompClient) {
  		var vm = this;
  		vm.resourcesMetaData = resourcesMetaData;
  		vm.notifications = [];
  		stompClient.connect({}, function(frame) {
            $log.log('Connected: ' + frame);
            stompClient.subscribe('/topic/notifications', function(notifications){
            	var _notifications = JSON.parse(notifications.body);
            	$scope.$apply(function(){
            		Array.prototype.push.apply(vm.notifications, _notifications);
            	});
            });
        });
  }]);
