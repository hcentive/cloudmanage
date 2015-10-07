'use strict';

describe('Service: pollingService', function () {

  // load the service's module
  beforeEach(module('cloudmanageApp'));

  // instantiate service
  var pollingService;
  beforeEach(inject(function (_pollingService_) {
    pollingService = _pollingService_;
  }));

  it('should do something', function () {
    expect(!!pollingService).toBe(true);
  });

});
