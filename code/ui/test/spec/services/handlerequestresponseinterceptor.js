'use strict';

describe('Service: HandleRequestResponseInterceptor', function () {

  // load the service's module
  beforeEach(module('cloudmanageApp'));

  // instantiate service
  var HandleRequestResponseInterceptor;
  beforeEach(inject(function (_HandleRequestResponseInterceptor_) {
    HandleRequestResponseInterceptor = _HandleRequestResponseInterceptor_;
  }));

  it('should do something', function () {
    expect(!!HandleRequestResponseInterceptor).toBe(true);
  });

});
