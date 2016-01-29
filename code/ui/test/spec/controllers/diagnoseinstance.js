'use strict';

describe('Controller: DiagnoseinstanceCtrl', function () {

  // load the controller's module
  beforeEach(module('cloudmanageApp'));

  var DiagnoseinstanceCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    DiagnoseinstanceCtrl = $controller('DiagnoseinstanceCtrl', {
      $scope: scope
      // place here mocked dependencies
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(DiagnoseinstanceCtrl.awesomeThings.length).toBe(3);
  });
});
