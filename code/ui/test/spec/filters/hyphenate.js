'use strict';

describe('Filter: hyphenate', function () {

  // load the filter's module
  beforeEach(module('cloudmanageApp'));

  // initialize a new instance of the filter before each test
  var hyphenate;
  beforeEach(inject(function ($filter) {
    hyphenate = $filter('hyphenate');
  }));

  it('should return the input prefixed with "hyphenate filter:"', function () {
    var text = 'angularjs';
    expect(hyphenate(text)).toBe('hyphenate filter: ' + text);
  });

});
