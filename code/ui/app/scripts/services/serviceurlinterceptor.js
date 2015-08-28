'use strict';

/**
 * @ngdoc service
 * @name cloudmanageApp.serviceUrlInterceptor
 * @description
 * # serviceUrlInterceptor
 * Provider in the cloudmanageApp.
 */
 angular.module('cloudmanageApp')
 .provider('serviceUrlInterceptor', function () {

    // Private variables
    var _urlsToIgnore = [];

    // Public API for configuration
    this.setUrlsToIgnore = function (urlsToIgnore) {
      Array.prototype.push.apply(_urlsToIgnore, urlsToIgnore);
    };

    this.addUrlToIgnore = function(url){
      _urlsToIgnore.push(url);
    };

    var isUrlToIgnore = function(url){
      for(var i = 0; i < _urlsToIgnore.length; i++){
        var pattern = _urlsToIgnore[i];
        if(url.indexOf(pattern) != -1)
          return true;
      }
      return false;
    };

    // Method for instantiating
    this.$get = ['serviceUrl',function (serviceUrl) {
      return {
        request: function (config) {
          var url = config.url;
          if(!isUrlToIgnore(url)){
            config.url = serviceUrl + url;
          }
          return config;
        }
      };
    }];
  });
