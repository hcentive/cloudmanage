'use strict';

describe('Service: accessDeniedInterceptor', function () {

  // load the service's module
  beforeEach(module('cloudmanageApp'));

  // instantiate service
  var accessDeniedInterceptor;
  beforeEach(inject(function (_accessDeniedInterceptor_) {
    accessDeniedInterceptor = _accessDeniedInterceptor_;
  }));

  it('should do something', function () {
    expect(!!accessDeniedInterceptor).toBe(true);
  });

});
