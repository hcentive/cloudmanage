'use strict';

/**
 * @ngdoc service
 * @name cloudmanageApp.InterceptorProxy
 * @description
 * # InterceptorProxy
 * Provider in the cloudmanageApp.
 */
angular.module('cloudmanageApp')
  .provider('InterceptorProxy', function () {

    var _interceptors = [],
    _requestInterceptors = [],
    _responseInterceptors = [],
    _urlsToIgnore = [];

    this.addUrlsToIgnore = function(){
      Array.prototype.push.apply(_urlsToIgnore, arguments);
    };
    this.addInterceptor = function () {

      Array.prototype.push.apply(_interceptors, arguments);
    };

    var _addInterceptorInRequest = function(interceptor){
      _requestInterceptors.push(interceptor);
    };

    var _addInterceptorInResponse = function(interceptor){
      _responseInterceptors.push(interceptor);
    };

    var _prepareInterceptors = function(interceptors, $injector){
      angular.forEach(_interceptors, function(_interceptorStr){
        var _interceptor = $injector.get(_interceptorStr);
        if(_interceptor.request || _interceptor.requestError){
          _addInterceptorInRequest(_interceptor);
        }
        if(_interceptor.response || _interceptor.responseError){
          _addInterceptorInResponse(_interceptor);
        }

      });
    }

    var _isUrlToIgnore = function(url){
      for(var i = 0; i < _urlsToIgnore.length; i++){
        var pattern = _urlsToIgnore[i];
        if(url.indexOf(pattern) != -1)
          return true;
      }
      return false;
    };

    
    this.$get = ['$q','$injector',function ($q, $injector) {
      _prepareInterceptors(_interceptors, $injector);
      return {
        request: function (config) {
          var promise = $q.when(config),
          url = config.url;
          if(!_isUrlToIgnore(url)){
              var counter = _requestInterceptors.length;
              while(counter){
                var _requestInterceptor = _requestInterceptors[--counter];
                promise = promise.then(_requestInterceptor.request, _requestInterceptor.requestError); 
              }
          }
          return promise;
        },
        response: function(response){
          var promise = $q.when(response);
          var counter = _responseInterceptors.length;
          while(counter){
            var _responseInterceptor = _responseInterceptors[--counter];
            promise = promise.then(_responseInterceptor.response); 
          }
          return promise;
        },
        responseError: function (rejection) {
          var promise = $q.when(rejection);
          var counter = _responseInterceptors.length;
          while(counter){
            var _responseInterceptor = _responseInterceptors[--counter];
            promise = promise.then(_responseInterceptor.responseError); 
          }
          return promise;
        }
      }
    }];
  });
