'use strict';

/**
 * @ngdoc function
 * @name cloudmanageApp.controller:InstancemetalistCtrl
 * @description
 * # InstancemetalistCtrl
 * Controller of the cloudmanageApp
 */
angular.module('cloudmanageApp')
  .controller('InstancemetalistCtrl', ['list',function (list) {
  	this.list = list;
  }]);
