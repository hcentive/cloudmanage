'use strict';

/**
 * @ngdoc function
 * @name cloudmanageApp.controller:ModalCtrl
 * @description
 * # ModalCtrl
 * Controller of the cloudmanageApp
 */
angular.module('cloudmanageApp')
 .controller('ModalCtrl', ['text', 'type','$uibModalInstance', function (text, type, $uibModalInstance) {
	this.text = text;
	this.type = type;
	this.ok = function () {
		$uibModalInstance.close();
	};
	this.cancel = function () {
		$uibModalInstance.dismiss('cancel');
	};
}]);
