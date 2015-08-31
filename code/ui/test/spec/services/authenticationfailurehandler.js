'use strict';

describe('Service: authenticationFailureHandler', function () {

  // load the service's module
  beforeEach(module('cloudmanageApp'));

  // instantiate service
  var authenticationFailureHandler;
  beforeEach(inject(function (_authenticationFailureHandler_) {
    authenticationFailureHandler = _authenticationFailureHandler_;
  }));

  it('should do something', function () {
    expect(!!authenticationFailureHandler).toBe(true);
  });

});
