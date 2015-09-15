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
  'ui.grid'
  ])
 .config(['$stateProvider', '$urlRouterProvider','$httpProvider', 'serviceUrlInterceptorProvider', 
  function($stateProvider, $urlRouterProvider, $httpProvider, serviceUrlInterceptorProvider){
    $urlRouterProvider.otherwise('/');
    $stateProvider
    .state('login', {
     url: '/login',
     templateUrl: 'views/login.html'
    })
    .state('vpc',{
      url: '/vpc',
      templateUrl: 'views/vpc.html'
    })
    .state('instances',{
      url: '/instances',
      templateUrl: 'views/instances.html'
    })
    .state('home',{
      url: '/',
      templateUrl: 'views/home.html'
    });
    serviceUrlInterceptorProvider.setUrlsToIgnore(['views','ui-grid','templates']);
    $httpProvider.interceptors.push('serviceUrlInterceptor');
    $httpProvider.interceptors.push('accessDeniedInterceptor');
  }])
 .value('serviceUrl', '/service');
