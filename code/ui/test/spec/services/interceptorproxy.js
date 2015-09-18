'use strict';

describe('Service: InterceptorProxy', function () {

  // load the service's module
  beforeEach(module('cloudmanageApp'));

  // instantiate service
  var InterceptorProxy;
  beforeEach(inject(function (_InterceptorProxy_) {
    InterceptorProxy = _InterceptorProxy_;
  }));

  it('should do something', function () {
    expect(!!InterceptorProxy).toBe(true);
  });

});
