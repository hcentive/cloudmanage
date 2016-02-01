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
  'smart-table',
  'ui.bootstrap',
  'ajoslin.promise-tracker',
  'ui.select',
  'ui.mask'
  ])
 .config(['routesProvider','$httpProvider','InterceptorProxyProvider',
  function(routesProvider, $httpProvider,InterceptorProxyProvider){

    InterceptorProxyProvider.addUrlsToIgnore('views','ui-grid','template','cronselection.html','selectize','bootstrap');
    InterceptorProxyProvider.addInterceptor('HandleRequestResponseInterceptor','GlobalTrackerInterceptor','serviceUrlInterceptor', 'accessDeniedInterceptor');
    $httpProvider.interceptors.unshift('InterceptorProxy');

  }])
 .run(['$http','utils', function($http, utils){
   $http.defaults.transformRequest = function(data, getHeaders){
        var headers = getHeaders();
        headers["Content-Type"] = "application/x-www-form-urlencoded; charset=utf-8";
        return (data && ! (typeof data === 'string')) ? utils.serializeJSON(data) : data;
    };
 }])
 .value('serviceUrl', '/service')
 .value('pollingInterval',60000);
