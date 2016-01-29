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
      scope:{
        tracker: '='
      },
      controller: ['GlobalTrackerService','$scope',function(GlobalTrackerService, $scope){
      	var that = this;
    		var tracker = $scope.tracker || GlobalTrackerService.getTracker();
        	$scope.$watch(tracker.active, function(val){
          	that.active = val;
        	});
      }],
      controllerAs: 'hcProcessingCtrl',
      link: function postLink(scope, element, attrs) {
      	 
      }
    };
  });
