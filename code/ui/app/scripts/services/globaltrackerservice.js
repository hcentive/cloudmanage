'use strict';

/**
 * @ngdoc service
 * @name cloudmanageApp.GlobalTrackerService
 * @description
 * # GlobalTrackerService
 * Service in the cloudmanageApp.
 */
angular.module('cloudmanageApp')
   .service('GlobalTrackerService', ['promiseTracker',function(promiseTracker){
	  var globalTracker = promiseTracker();
	  this.getTracker = function(){
	    return globalTracker;
	  };
}]);
