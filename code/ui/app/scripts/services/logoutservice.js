'use strict';

/**
 * @ngdoc service
 * @name cloudmanageApp.logoutService
 * @description
 * # logoutService
 * Factory in the cloudmanageApp.
 */
angular.module('cloudmanageApp')
  .factory('logoutService', ['$http',  'securityContextUtils',function ($http, securityContextUtils) {
    return {
      logout: function () {
        return $http.post('/logout').then(function(){
            securityContextUtils.clearSecurityContext();
        });
      }
    };
  }]);
