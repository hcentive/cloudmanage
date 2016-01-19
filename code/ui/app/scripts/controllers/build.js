'use strict';

/**
 * @ngdoc function
 * @name cloudmanageApp.controller:BuildCtrl
 * @description
 * # BuildCtrl
 * Controller of the cloudmanageApp
 */
angular.module('cloudmanageApp')
  .controller('BuildCtrl', ['list','$state', function (list, $state) {

    this.list = _.map(list, function(item){return item.name;});
    this.getDetail = function($item, $model){
    	$state.go('build.details',{
    		jobName: $item
    	});
    };
  }]);
