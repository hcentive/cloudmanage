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
    stompClientProvider.setEndPoint('/resources');

  }])
 .value('serviceUrl', '/service')
 .value('pollingInterval',10000);
