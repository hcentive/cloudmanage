'use strict';

/**
 * @ngdoc service
 * @name cloudmanageApp.HandleRequestResponseInterceptor
 * @description
 * # HandleRequestResponseInterceptor
 * Factory in the cloudmanageApp.
 */
angular.module('cloudmanageApp')
  .factory('HandleRequestResponseInterceptor', ['$log','$q',function ($log, $q) {
    return {
      responseError: function(rejection){
         $log.error(rejection);
         return $q.reject(rejection);
      }
    };
  }]);
