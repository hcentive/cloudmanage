'use strict';

/**
 * @ngdoc function
 * @name cloudmanageApp.controller:BuilddetailCtrl
 * @description
 * # BuilddetailCtrl
 * Controller of the cloudmanageApp
 */
angular.module('cloudmanageApp')
  .controller('BuilddetailCtrl', ['buildService','$stateParams',function (buildService, $stateParams) {
  	var that = this;
  	var jobName = $stateParams.jobName;
  	this.detailsList = [];
    buildService.getDetail(jobName).then(function(data){
    	that.detailsList.push(data);
    });
  }]);
