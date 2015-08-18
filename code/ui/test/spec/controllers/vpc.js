'use strict';

describe('Controller: VpcCtrl', function () {

  // load the controller's module
  beforeEach(module('cloudmanageApp'));

  var VpcCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    VpcCtrl = $controller('VpcCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
