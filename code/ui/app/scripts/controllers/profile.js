'use strict';

/**
 * @ngdoc function
 * @name cloudmanageApp.controller:ProfileCtrl
 * @description
 * # ProfileCtrl
 * Controller of the cloudmanageApp
 */
angular.module('cloudmanageApp')
  .controller('ProfileCtrl', ['profile',function (profile) {
  	this.profile = profile;
  }]);
