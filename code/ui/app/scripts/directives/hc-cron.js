'use strict';

/**
 * @ngdoc directive
 * @name cloudmanageApp.directive:hcCron
 * @description
 * # hcCron
 */
angular.module('cloudmanageApp')
  .directive('hcCron', function () {
    return {
      restrict: 'A',
      require: 'ngModel',
      link: function postLink(scope, element, attrs, ngModelController) {
      	function initializeCron(initialValue){
      		var cronOptions = {
      			onChange: function() {
	            	ngModelController.$setViewValue(element.cron('value'));
	            }
      		};
      		if(initialValue){
      			cronOptions.initial = initialValue;
      		}
      		element.cron(cronOptions);
      	}
      	ngModelController.$render = function(){
      		initializeCron(ngModelController.$viewValue);
      	};
      	
      }
    };
  });
