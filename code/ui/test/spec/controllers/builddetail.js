'use strict';

describe('Controller: BuilddetailCtrl', function () {

  // load the controller's module
  beforeEach(module('cloudmanageApp'));

  var BuilddetailCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    BuilddetailCtrl = $controller('BuilddetailCtrl', {
      $scope: scope
      // place here mocked dependencies
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(BuilddetailCtrl.awesomeThings.length).toBe(3);
  });
});
