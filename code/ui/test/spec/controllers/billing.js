'use strict';

describe('Controller: BillingCtrl', function () {

  // load the controller's module
  beforeEach(module('cloudmanageApp'));

  var BillingCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    BillingCtrl = $controller('BillingCtrl', {
      $scope: scope
      // place here mocked dependencies
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(BillingCtrl.awesomeThings.length).toBe(3);
  });
});
