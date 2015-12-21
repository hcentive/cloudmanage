'use strict';

/**
 * @ngdoc function
 * @name cloudmanageApp.controller:AuditinstanceCtrl
 * @description
 * # AuditinstanceCtrl
 * Controller of the cloudmanageApp
 */
angular.module('cloudmanageApp')
  .controller('AuditinstanceCtrl', ['auditList','instance','$modalInstance',function (auditList, instance, $modalInstance) {
  	this.list = auditList;
  	this.dismiss = function () {
  		$modalInstance.dismiss('cancel');
  	};
  }]);
