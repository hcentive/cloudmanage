'use strict';

/**
 * @ngdoc function
 * @name cloudmanageApp.controller:CpuutilizationCtrl
 * @description
 * # CpuutilizationCtrl
 * Controller of the cloudmanageApp
 */
angular.module('cloudmanageApp')
  .controller('CpuutilizationCtrl', ['instance','ec2Service','$uibModalInstance','promiseTracker',
  	function (instance, ec2Service, $modalInstance, promiseTracker) {
  	var that = this;
  	this.instanceName = ec2Service.getInstanceName(instance);
  	this.filter = {
  		fromDate: moment().subtract(1, 'months').toDate(),
  		toDate: new Date()
  	};
  	this.loadingTracker = promiseTracker();
  	this.filterData = function(){
  		var promise = ec2Service.getCPUUtilization(instance, this.filter)
	    .then(function(data){
	    	that.dataPoints = data;
	    });
	    this.loadingTracker.addPromise(promise);
  	};
    var promise = ec2Service.getCPUUtilization(instance, this.filter)
				    .then(function(data){
				    	that.dataPoints = data;
				    });
    this.loadingTracker.addPromise(promise);
    this.dismiss = function () {
  		$modalInstance.dismiss('cancel');
  	};
  	this.fromDatePopover = {
  		open: function(){
  			this.opened = true;
  		},
  		opened: false
  	};
  	this.toDatePopover = {
  		open: function(){
  			this.opened = true;
  		},
  		opened: false
  	};

  }]);
