'use strict';

describe('Controller: AuditinstanceCtrl', function () {

  // load the controller's module
  beforeEach(module('cloudmanageApp'));

  var AuditinstanceCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    AuditinstanceCtrl = $controller('AuditinstanceCtrl', {
      $scope: scope
      // place here mocked dependencies
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(AuditinstanceCtrl.awesomeThings.length).toBe(3);
  });
});
