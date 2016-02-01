'use strict';

/**
 * @ngdoc service
 * @name cloudmanageApp.securityContextUtils
 * @description
 * # securityContextUtils
 * Factory in the cloudmanageApp.
 */
angular.module('cloudmanageApp')
  .factory('securityContextUtils', ['securityContextHolder',function (securityContextHolder) {
    return {
      clearSecurityContext: function () {
        securityContextHolder.principal = {};
        securityContextHolder.authenticated = false;
        securityContextHolder.profile = {};
      }
    };
  }]);
