'use strict';

describe('Service: securityContextUtils', function () {

  // load the service's module
  beforeEach(module('cloudmanageApp'));

  // instantiate service
  var securityContextUtils;
  beforeEach(inject(function (_securityContextUtils_) {
    securityContextUtils = _securityContextUtils_;
  }));

  it('should do something', function () {
    expect(!!securityContextUtils).toBe(true);
  });

});
