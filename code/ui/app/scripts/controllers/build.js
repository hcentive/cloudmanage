'use strict';

/**
 * @ngdoc function
 * @name cloudmanageApp.controller:BuildCtrl
 * @description
 * # BuildCtrl
 * Controller of the cloudmanageApp
 */
angular.module('cloudmanageApp')
  .controller('BuildCtrl', ['list','$state','buildService', function (list, $state, buildService) {
  	var that = this;
    this.list = list;
    this.detailsList = [];
    this.getDetail = function(jobName){
	    buildService.getDetail(jobName).then(function(data){
	    	that.detailsList = data;
	    });
    };
    this.removeDetail = function(jobName){
    	delete this.detailsList[jobName];
    };
  }]);
