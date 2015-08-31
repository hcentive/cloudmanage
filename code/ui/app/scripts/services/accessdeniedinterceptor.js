'use strict';

/**
 * @ngdoc service
 * @name cloudmanageApp.accessDeniedInterceptor
 * @description
 * # accessDeniedInterceptor
 * Factory in the cloudmanageApp.
 */
angular.module('cloudmanageApp')
  .factory('accessDeniedInterceptor', ['$q','$injector','stateCache',function ($q, $injector, stateCache) {
    return {
      responseError: function (rejection) {
        var state = $injector.get('$state');
        if(rejection.status === 401) {
          stateCache.addState(state.current);
          state.go('login');
        }
        return $q.reject(rejection);  
      }
    };
  }]);
