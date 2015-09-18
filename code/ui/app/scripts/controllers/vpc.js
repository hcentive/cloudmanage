'use strict';

/**
 * @ngdoc function
 * @name cloudmanageApp.controller:VpcCtrl
 * @description
 * # VpcCtrl
 * Controller of the cloudmanageApp
 */
angular.module('cloudmanageApp')
  .controller('VpcCtrl', ['$scope','ec2Service','vpcs',function ($scope, ec2Service, vpcs) {

  	var that = this;
    that.list = vpcs;
    that.columnDef = [
      {data: 'awsVpc.vpcId'},
      {data: 'awsVpc.state'}
    ];
  }]);
