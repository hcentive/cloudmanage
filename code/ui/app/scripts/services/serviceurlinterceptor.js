'use strict';

/**
 * @ngdoc service
 * @name cloudmanageApp.serviceUrlInterceptor
 * @description
 * # serviceUrlInterceptor
 * Factory in the cloudmanageApp.
 */
angular.module('cloudmanageApp')
  .factory('serviceUrlInterceptor', ['serviceUrl',function (serviceUrl) {
    return {
      request: function (config) {
        var url = config.url;
        if(url.indexOf('views') != 0){
          config.url = serviceUrl + url;
        }
        return config;
      }
    };
  }]);
