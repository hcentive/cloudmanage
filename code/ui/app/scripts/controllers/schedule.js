'use strict';

/**
 * @ngdoc function
 * @name cloudmanageApp.controller:ScheduleCtrl
 * @description
 * # ScheduleCtrl
 * Controller of the cloudmanageApp
 */
angular.module('cloudmanageApp')
  .controller('ScheduleCtrl', ['instance','$http','$log','ec2Service','$modalInstance', function (instance, $http, $log, ec2Service, $modalInstance) {
  	this.cron = {
  		start:null,
  		stop:null
  	}
  	this.submit = function(){
  		var startCron = this.cron.start,
  		stopCron = this.cron.stop;
  		ec2Service.schedule(instance, startCron, stopCron).then(function(data){
  			$modalInstance.close(data);
  		});
  	}
	this.cancel = function () {
		$modalInstance.dismiss('cancel');
	};

  	function getCostCenter(instance){
  		return ec2Service.getCostCenter(instance);
  	}

  	function getInstanceName(instance){
  		return ec2Service.getInstanceName(instance);
  	}
    
  }]);
