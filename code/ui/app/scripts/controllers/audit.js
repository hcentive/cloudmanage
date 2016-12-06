'use strict';

/**
 * @ngdoc function
 * @name cloudmanageApp.controller:AuditCtrl
 * @description
 * # AuditCtrl
 * Controller of the cloudmanageApp
 */
angular.module('cloudmanageApp')
  .controller('AuditCtrl', ['auditService', function (auditService) {
    var self = this,
        pageSegment = 0,
        pageSize = 50,
        latest = true;

    self.list = [];

    function init(){
      self.loadMore();
    }

    self.loadMore = function(){
      var auditPromise = auditService.getList(pageSegment,pageSize,latest);
      auditPromise.then(function(response){
          pageSegment++;
          var audits = response.data;
          audits.forEach(function(audit){
            self.list.push(audit);
          });
      });
    }
    init();
  }]);
