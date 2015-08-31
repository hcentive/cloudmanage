'use strict';

/**
 * @ngdoc service
 * @name cloudmanageApp.securityContextHolder
 * @description
 * # securityContextHolder
 * Factory in the cloudmanageApp.
 */
angular.module('cloudmanageApp')
  .factory('securityContextHolder', function () {
    return {
      principal : {},
      authenticated: false
    };
  });
