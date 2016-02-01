'use strict';

/**
 * @ngdoc directive
 * @name cloudmanageApp.directive:stSearchWatch
 * @description
 * # stSearchWatch
 */
angular.module('cloudmanageApp')
  .directive('stSearchWatch', function () {
    return {
    require:'^stTable',
    scope:{
      stSearchWatch:'='
    },
    link:function(scope, ele, attr, ctrl){
      var table=ctrl;
      
      scope.$watch('stSearchWatch',function(val){
        ctrl.search(val);
      });
      
    }
  };
  });
