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
  'ajoslin.promise-tracker'
  ])
 .config(['$stateProvider', '$urlRouterProvider','$httpProvider','InterceptorProxyProvider', 
  function($stateProvider, $urlRouterProvider, $httpProvider,InterceptorProxyProvider){
    $urlRouterProvider.otherwise('/');
    $stateProvider
    .state('login', {
     url: '/login',
     templateUrl: 'views/login.html'
    })
    .state('vpc',{
      url: '/vpc',
      templateUrl: 'views/vpc.html',
      resolve:{
        vpcs: ['ec2Service', function(ec2Service){
          return ec2Service.getVpcs();
        }],
      },
      controller: 'VpcCtrl',
      controllerAs: 'vpc'
    })
    .state('instances',{
      url: '/instances',
      templateUrl: 'views/instances.html',
      resolve: {
        instances: ['groupService', 'ec2Service',function(groupService, ec2Service){
           return ec2Service.getInstances(groupService.vm.selectedGroup);
        }]
      },
      controller: 'InstanceCtrl',
      controllerAs: 'instanceCtrl'
    })
    .state('home',{
      url: '/',
      templateUrl: 'views/home.html'
    });
    InterceptorProxyProvider.addUrlsToIgnore('views','ui-grid','templates');
    InterceptorProxyProvider.addInterceptor('GlobalTrackerInterceptor','serviceUrlInterceptor', 'accessDeniedInterceptor');
    $httpProvider.interceptors.unshift('InterceptorProxy');
  }])
 .value('serviceUrl', '/service');
