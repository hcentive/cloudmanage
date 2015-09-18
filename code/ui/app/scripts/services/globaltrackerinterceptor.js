'use strict';

/**
 * @ngdoc service
 * @name cloudmanageApp.GlobalTrackerInterceptor
 * @description
 * # GlobalTrackerInterceptor
 * Factory in the cloudmanageApp.
 */
angular.module('cloudmanageApp')
  .factory('GlobalTrackerInterceptor', ['GlobalTrackerService',function (GlobalTrackerService) {
    return {
      request: function(config){
       if(! (config.ignoreTracker || config.tracker)){
          config.tracker = GlobalTrackerService.getTracker();
        }
        return config;
      }
    };
  }]);
