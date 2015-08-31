'use strict';

describe('Service: securityContextHolder', function () {

  // load the service's module
  beforeEach(module('cloudmanageApp'));

  // instantiate service
  var securityContextHolder;
  beforeEach(inject(function (_securityContextHolder_) {
    securityContextHolder = _securityContextHolder_;
  }));

  it('should do something', function () {
    expect(!!securityContextHolder).toBe(true);
  });

});
