'use strict';

describe('Service: auditService', function () {

  // load the service's module
  beforeEach(module('cloudmanageApp'));

  // instantiate service
  var auditService;
  beforeEach(inject(function (_auditService_) {
    auditService = _auditService_;
  }));

  it('should do something', function () {
    expect(!!auditService).toBe(true);
  });

});
