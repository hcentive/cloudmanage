'use strict';

describe('Service: GlobalTrackerInterceptor', function () {

  // load the service's module
  beforeEach(module('cloudmanageApp'));

  // instantiate service
  var GlobalTrackerInterceptor;
  beforeEach(inject(function (_GlobalTrackerInterceptor_) {
    GlobalTrackerInterceptor = _GlobalTrackerInterceptor_;
  }));

  it('should do something', function () {
    expect(!!GlobalTrackerInterceptor).toBe(true);
  });

});
