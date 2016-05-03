'use strict';

/**
 * @ngdoc function
 * @name cloudmanageApp.controller:BillingCtrl
 * @description
 * # BillingCtrl
 * Controller of the cloudmanageApp
 */
angular.module('cloudmanageApp')
  .controller('BillingCtrl', ['ec2Service',function (ec2Service) {
	var that = this;
	this.filter = {
		fromDate: moment().subtract(1, 'months').toDate(),
		toDate: new Date()
	};
	this.filterData = function(){
		ec2Service.getBillingInfo(this.filter)
	    .then(function(data){
			that.dataPoints = data;
		});
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
    this.filterData();    
}]);
