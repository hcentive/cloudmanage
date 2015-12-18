'use strict';

/**
 * @ngdoc overview
 * @name uiApp
 * @description
 * # uiApp
 *
 * Main module of the application.
 */
 angular
 .module('cloudmanageApp', [
  'ngAnimate',
  'ngCookies',
  'ngResource',
  'ngSanitize',
  'ngTouch',
  'ui.router',
  'ui.grid',
  'ui.bootstrap',
  'ajoslin.promise-tracker'
  ])
 .config(['routesProvider','$httpProvider','InterceptorProxyProvider','stompClientProvider',
  function(routesProvider, $httpProvider,InterceptorProxyProvider, stompClientProvider){

    InterceptorProxyProvider.addUrlsToIgnore('views','ui-grid','template');
    InterceptorProxyProvider.addInterceptor('GlobalTrackerInterceptor','serviceUrlInterceptor', 'accessDeniedInterceptor');
    $httpProvider.interceptors.unshift('InterceptorProxy');
    //Transform request into form data
    $httpProvider.defaults.transformRequest = function(data, getHeaders){
        var headers = getHeaders();
        headers["Content-Type"] = "application/x-www-form-urlencoded; charset=utf-8";
        return (data && ! (typeof data === 'string')) ? $.param(data) : data;
    };
    stompClientProvider.setEndPoint('/resources');

  }])
 .value('serviceUrl', '/service')
 .value('pollingInterval',10000);
