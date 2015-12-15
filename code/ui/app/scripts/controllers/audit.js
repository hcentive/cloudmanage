'use strict';

/**
 * @ngdoc function
 * @name cloudmanageApp.controller:AuditCtrl
 * @description
 * # AuditCtrl
 * Controller of the cloudmanageApp
 */
angular.module('cloudmanageApp')
  .controller('AuditCtrl', ['list', function (list) {
    this.list = list;
  }]);
