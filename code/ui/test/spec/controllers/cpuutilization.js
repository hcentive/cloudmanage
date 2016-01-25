'use strict';

describe('Controller: CpuutilizationCtrl', function () {

  // load the controller's module
  beforeEach(module('cloudmanageApp'));

  var CpuutilizationCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    CpuutilizationCtrl = $controller('CpuutilizationCtrl', {
      $scope: scope
      // place here mocked dependencies
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(CpuutilizationCtrl.awesomeThings.length).toBe(3);
  });
});
