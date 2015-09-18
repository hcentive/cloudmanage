'use strict';

describe('Service: GlobalTrackerService', function () {

  // load the service's module
  beforeEach(module('cloudmanageApp'));

  // instantiate service
  var GlobalTrackerService;
  beforeEach(inject(function (_GlobalTrackerService_) {
    GlobalTrackerService = _GlobalTrackerService_;
  }));

  it('should do something', function () {
    expect(!!GlobalTrackerService).toBe(true);
  });

});
