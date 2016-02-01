'use strict';

describe('Directive: stSearchWatch', function () {

  // load the directive's module
  beforeEach(module('cloudmanageApp'));

  var element,
    scope;

  beforeEach(inject(function ($rootScope) {
    scope = $rootScope.$new();
  }));

  it('should make hidden element visible', inject(function ($compile) {
    element = angular.element('<st-search-watch></st-search-watch>');
    element = $compile(element)(scope);
    expect(element.text()).toBe('this is the stSearchWatch directive');
  }));
});
