'use strict';

describe('Service: stateCache', function () {

  // load the service's module
  beforeEach(module('cloudmanageApp'));

  // instantiate service
  var stateCache;
  beforeEach(inject(function (_stateCache_) {
    stateCache = _stateCache_;
  }));

  it('should do something', function () {
    expect(!!stateCache).toBe(true);
  });

});
