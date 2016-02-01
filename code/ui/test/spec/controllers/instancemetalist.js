'use strict';

describe('Controller: InstancemetalistCtrl', function () {

  // load the controller's module
  beforeEach(module('cloudmanageApp'));

  var InstancemetalistCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    InstancemetalistCtrl = $controller('InstancemetalistCtrl', {
      $scope: scope
      // place here mocked dependencies
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(InstancemetalistCtrl.awesomeThings.length).toBe(3);
  });
});
