'use strict';

/**
 * @ngdoc directive
 * @name cloudmanageApp.directive:groupsList
 * @description
 * # groupsList
 */
angular.module('cloudmanageApp')
  .directive('groupsList', ['groupService','$log', function (groupService, $log) {
    return {
      templateUrl: 'templates/_groupList.html',
      restrict: 'E',
      replace:true,
      controller: ['groupService', function(groupService){
      	var vm = this;
        vm.group = null;
        vm.groupModel = groupService.vm;
      	groupService.getList()
      	.then(
      		function(groups){
      			vm.groups = groups;
      		},
      		function(){
      			$log.error("no groups");
      		}
      	);
      }],
      controllerAs: 'groupsListCtrl',
      link: function postLink(scope, element, attrs) {
        
      }
    };
  }]);
