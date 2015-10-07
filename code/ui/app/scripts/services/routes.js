'use strict';

/**
 * @ngdoc service
 * @name cloudmanageApp.routes
 * @description
 * # routes
 * Provider in the cloudmanageApp.
 */
angular.module('cloudmanageApp')
  .provider('routes', ['$stateProvider', '$urlRouterProvider',function ($stateProvider, $urlRouterProvider) {

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
    })
    .state('dashboard',{
      url: '/dashboard',
      templateUrl: 'views/dashboard.html',
      resolve:{
        resourcesMetaData : ['ec2Service', function(ec2Service){
          return ec2Service.getResourcesMetaData();
        }]
      },
      controller: 'DashboardCtrl',
      controllerAs: 'dashboardCtrl'
    });
    this.$get = function(){};
  }]);
