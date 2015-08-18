'use strict';

/**
 * @ngdoc service
 * @name cloudmanageApp.accessDeniedInterceptor
 * @description
 * # accessDeniedInterceptor
 * Factory in the cloudmanageApp.
 */
angular.module('cloudmanageApp')
  .factory('accessDeniedInterceptor', ['$q','$injector',function ($q, $injector) {
    return {
      responseError: function (rejection) {
        var state = $injector.get('$state');
        if(rejection.status === 401) {
          state.go('login');
        }
        else {
          return $q.reject(rejection);  
        }
        
      }
    };
  }]);
