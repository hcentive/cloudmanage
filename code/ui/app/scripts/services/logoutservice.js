'use strict';

/**
 * @ngdoc service
 * @name cloudmanageApp.logoutService
 * @description
 * # logoutService
 * Factory in the cloudmanageApp.
 */
angular.module('cloudmanageApp')
  .factory('logoutService', ['$http', 'serviceUrl', 'securityContextHolder',function ($http, serviceUrl, securityContextHolder) {
    return {
      logout: function () {
        return $http.post('/logout').then(function(){
            securityContextHolder.principal = null;
            securityContextHolder.authenticated = false;
        });
      }
    };
  }]);
