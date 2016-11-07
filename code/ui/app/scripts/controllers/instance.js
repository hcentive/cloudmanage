'use strict';

/**
 * @ngdoc function
 * @name cloudmanageApp.controller:InstanceCtrl
 * @description
 * # InstanceCtrl
 * Controller of the cloudmanageApp
 */
angular.module('cloudmanageApp')
 .controller('InstanceCtrl', [ '$scope',
  'ec2Service',
  'instances',
  '$log',
  'pollingService',
  'auditService',
  '$uibModal',
  'promiseTracker',
  'hcModal',
  'cloudwatchService',
  function ($scope, ec2Service,instances, $log, pollingService, auditService, $modal,promiseTracker, hcModal,cloudwatchService) {
    var that = this;
    that.list = instances;

    function isQaDevInstance(instance){
        var tagValue = ec2Service.getTagValue(instance,'Stack');

        if(tagValue === 'qa' || tagValue === 'dev'){
          return true;
        }    
        return false;
    }

    this.stopInstance = function(instance){
      if(!(instance.actionTracker && instance.actionTracker.active())){
        if(isQaDevInstance(instance)){
          // instance is qa or dev
          hcModal.confirm('Are you sure you want to stop the instance?').then(function(){
            instance.actionTracker = promiseTracker();
            var promise = ec2Service.stopInstance(instance).then(function(response){
              var data = response.data;
              instance.awsInstance.state.name = data.name;
            });
            instance.actionTracker.addPromise(promise);
           });  
        }else{
          // instance is other than qa or dev
          hcModal.info('You can stop only QA and Dev instance').then(function(){});
        }
      }
    };

    this.startInstance = function(instance){
    
      if(!(instance.actionTracker && instance.actionTracker.active())){
        if(isQaDevInstance(instance)){
          // instance is qa or dev
        hcModal.confirm('Are you sure you want to start the instance?').then(function(){
          instance.actionTracker = promiseTracker();
          var promise = ec2Service.startInstance(instance).then(function(response){
            var data = response.data;
            instance.awsInstance.state.name = data.name;
          });
          instance.actionTracker.addPromise(promise);
        });  
        }else{
          // instance is other than qa or dev
          hcModal.info('You can start only QA and Dev instance').then(function(){});
        }
      }
    };

    this.terminateInstance = function(instance){
      ec2Service.terminateInstance(instance);
    };

    this.getInstanceName = function(instance){
      return ec2Service.getInstanceName(instance);
    };

    this.audit = function(instance){
      $modal.open({
        templateUrl: 'templates/_auditInstance.html',
        controller: 'AuditinstanceCtrl',
        controllerAs: 'auditInstanceCtrl',
        size: 'lg',
        resolve: {
          instance: function () {
            return instance;
          },
          auditList: function(){
            return auditService.getInstanceAuditList(instance);
          }
        }
      });
    };

    // this.schedule = function(instance){
    //   $modal.open({
    //     templateUrl: 'templates/_scheduleInstance.html',
    //     controller: 'ScheduleCtrl',
    //     controllerAs: 'scheduleCtrl',
    //     resolve: {
    //       instance: function () {
    //         return instance;
    //       },
    //       jobDetails: function(){
    //         return ec2Service.getJobDetails(instance);
    //       }
    //     }
    //   });
    // };


    this.costOptimization = function(instance){
      $modal.open({
        templateUrl: 'templates/_costoptimization.html',
        controller: 'CostOptimizationCtrl',
        controllerAs: 'costOptimizationCtrl',
        resolve: {
          instance: function () {
            return instance;
          },
          jobDetails: function(){
            return ec2Service.getJobDetails(instance);
          },
          alarm : function(){
            return cloudwatchService.getAlarm(instance.awsInstance.instanceId);
          }
        }
      });
    };
    this.cpuUtilization = function(instance){
      $modal.open({
        templateUrl: 'templates/_cpuUtilization.html',
        controller: 'CpuutilizationCtrl',
        controllerAs: 'cPUUtilizationCtrl',
        size: 'lg',
        resolve: {
          instance: function () {
            return instance;
          }
        }
      });
    };

    this.diagnose = function(){
      $modal.open({
        templateUrl: 'templates/_diagnoseInstance.html',
        controller: 'DiagnoseinstanceCtrl',
        controllerAs : 'diagnoseinstanceCtrl'
      });
    };
  }]);
