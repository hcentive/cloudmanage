'use strict';

describe('Directive: hcProcessing', function () {

  // load the directive's module
  beforeEach(module('cloudmanageApp'));

  var element,
    scope;

  beforeEach(inject(function ($rootScope) {
    scope = $rootScope.$new();
  }));

  it('should make hidden element visible', inject(function ($compile) {
    element = angular.element('<hc-processing></hc-processing>');
    element = $compile(element)(scope);
    expect(element.text()).toBe('this is the hcProcessing directive');
  }));
});
