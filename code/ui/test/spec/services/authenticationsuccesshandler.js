'use strict';

describe('Service: authenticationSuccessHandler', function () {

  // load the service's module
  beforeEach(module('cloudmanageApp'));

  // instantiate service
  var authenticationSuccessHandler;
  beforeEach(inject(function (_authenticationSuccessHandler_) {
    authenticationSuccessHandler = _authenticationSuccessHandler_;
  }));

  it('should do something', function () {
    expect(!!authenticationSuccessHandler).toBe(true);
  });

});
