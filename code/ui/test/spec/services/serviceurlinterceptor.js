'use strict';

describe('Service: serviceUrlInterceptor', function () {

  // load the service's module
  beforeEach(module('cloudmanageApp'));

  // instantiate service
  var serviceUrlInterceptor;
  beforeEach(inject(function (_serviceUrlInterceptor_) {
    serviceUrlInterceptor = _serviceUrlInterceptor_;
  }));

  it('should do something', function () {
    expect(!!serviceUrlInterceptor).toBe(true);
  });

});
