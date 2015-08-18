'use strict';

/**
 * @ngdoc function
 * @name cloudmanageApp.controller:VpcCtrl
 * @description
 * # VpcCtrl
 * Controller of the cloudmanageApp
 */
angular.module('cloudmanageApp')
  .controller('VpcCtrl', ['$scope','ec2Service',function ($scope, ec2Service) {
    var that = this;
    ec2Service.getVpcs()
    .then(function(data){
    	that.list = data;
    });
  }]);
