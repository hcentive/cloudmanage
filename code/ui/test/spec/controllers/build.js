'use strict';

describe('Controller: BuildCtrl', function () {

  // load the controller's module
  beforeEach(module('cloudmanageApp'));

  var BuildCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    BuildCtrl = $controller('BuildCtrl', {
      $scope: scope
      // place here mocked dependencies
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(BuildCtrl.awesomeThings.length).toBe(3);
  });
});
