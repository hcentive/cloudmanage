'use strict';

/**
 * @ngdoc directive
 * @name cloudmanageApp.directive:hcProcessing
 * @description
 * # hcProcessing
 */
angular.module('cloudmanageApp')
  .directive('hcProcessing', function () {
    return {
      templateUrl: 'templates/_processing.html',
      restrict: 'E',
      controller: ['GlobalTrackerService','$scope',function(GlobalTrackerService, $scope){
      	var that = this;
  		var globalTracker = GlobalTrackerService.getTracker();
      	$scope.$watch(globalTracker.active, function(val){
        	that.active = val;
      	});
      }],
      controllerAs: 'hcProcessingCtrl',
      link: function postLink(scope, element, attrs) {
      	 
      }
    };
  });
