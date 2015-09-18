'use strict';

/**
 * @ngdoc service
 * @name cloudmanageApp.serviceUrlInterceptor
 * @description
 * # serviceUrlInterceptor
 * Provider in the cloudmanageApp.
 */
 angular.module('cloudmanageApp')
 .factory('serviceUrlInterceptor', ['serviceUrl',function (serviceUrl) {
    return {
        request: function (config) {
          config.url = serviceUrl + config.url;
          return config;
        }
      };
  }]);
