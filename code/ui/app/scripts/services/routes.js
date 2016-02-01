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

  $urlRouterProvider.otherwise('/login');
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
      instances: ['ec2Service',function(ec2Service){
       return ec2Service.getInstances();
     }]
   },
   controller: 'InstanceCtrl',
   controllerAs: 'instanceCtrl'
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
  })
  .state('audit',{
    url: '/audit',
    templateUrl: 'views/audit.html',
    resolve:{
      list : ['auditService', function(auditService){
        return auditService.getList();
      }]
    },
    controller: 'AuditCtrl',
    controllerAs: 'auditCtrl'
  })
  .state('build',{
    url: '/build',
    templateUrl: 'views/build.html',
    resolve: {
      list: ['buildService', function(buildService){
        return buildService.getList();
      }]
    },
    controller: 'BuildCtrl',
    controllerAs: 'buildCtrl'
  })
  .state('build.details',{
    url: '/{jobName}',
    templateUrl: 'views/build_details.html',
    controller: 'BuilddetailCtrl',
    controllerAs: 'buildDetailCtrl'
  })
  .state('profile',{
    url: '/profile',
    templateUrl: 'views/profile.html',
    controller: 'ProfileCtrl',
    controllerAs: 'profileCtrl',
    resolve: {
      profile: ['securityContextHolder', function(securityContextHolder){
        return securityContextHolder.profile;
      }]
    }
  })
  .state('instanceMetaList',{
    url: '/instanceMetaList',
    templateUrl: 'views/instancemetalist.html',
    resolve: {
      list: ['ec2Service', function(ec2Service){
        return ec2Service.getInstanceMetaList();
      }]
    },
    controller: 'InstancemetalistCtrl',
    controllerAs: 'instancemetalistCtrl'

  });
  this.$get = function(){};
}]);
