'use strict';

describe('Controller: AuditCtrl', function () {

  // load the controller's module
  beforeEach(module('cloudmanageApp'));

  var AuditCtrl,scope,AuditServiceMock,
    auditData = [{"eventType":"created","args":"audit:args","userName":"dummy_user","eventTime":Date()},
      {"eventType":"updated","args":"audit:args","userName":"dummy_user","eventTime":Date()},
      {"eventType":"deleted","args":"audit:args","userName":"dummy_user","eventTime":Date()},
      {"eventType":"updated","args":"audit:args","userName":"dummy_user","eventTime":Date()},
      {"eventType":"created","args":"audit:args","userName":"dummy_user","eventTime":Date()}];

  // Initialize the controller and it's dependencies
  beforeEach(inject(function ($controller, $rootScope,_auditService_) {
    scope = $rootScope.$new();
    AuditServiceMock = _auditService_;
    spyOn(AuditServiceMock, 'getList').and.returnValue({then:function(callback){
      return callback({data:auditData});
    }});
    AuditCtrl = $controller('AuditCtrl', {
      $scope: scope,
      auditService : AuditServiceMock
    });
  }));

  it("AuditCtrl : should be defined", function () {
    expect(AuditCtrl).toBeDefined();
  });

  it("AuditCtrl : loadMore method",function(){
      AuditCtrl.list = [];
      expect(AuditCtrl.list.length).toBe(0);
      AuditCtrl.loadMore();
      expect(AuditCtrl.list.length).toBe(5);
  });
});
