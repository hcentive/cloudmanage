'use strict';

describe('Directive: groupsList', function () {

  // load the directive's module
  beforeEach(module('cloudmanageApp'));

  var element,
    scope;

  beforeEach(inject(function ($rootScope) {
    scope = $rootScope.$new();
  }));

  it('should make hidden element visible', inject(function ($compile) {
    element = angular.element('<groups-list></groups-list>');
    element = $compile(element)(scope);
    expect(element.text()).toBe('this is the groupsList directive');
  }));
});
