'use strict';

/**
 * @ngdoc directive
 * @name cloudmanageApp.directive:hcDatatable
 * @description
 * # hcDatatable
 */
angular.module('cloudmanageApp')
  .directive('hcDatatable', function () {
    return {
      restrict: 'A',
      scope: {
      	"data" : "=",
      	"columnDef":"="
      },
      link: function postLink(scope, element, attrs) {
        var _table = element.DataTable();
        scope.$watch("data", function(newData){
          _table.destroy();
          _table = element.DataTable({
            data: newData,
            columns: scope.columnDef
          });
        });
      }
    };
  });
