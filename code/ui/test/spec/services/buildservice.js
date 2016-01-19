'use strict';

describe('Service: buildService', function () {

  // load the service's module
  beforeEach(module('cloudmanageApp'));

  // instantiate service
  var buildService;
  beforeEach(inject(function (_buildService_) {
    buildService = _buildService_;
  }));

  it('should do something', function () {
    expect(!!buildService).toBe(true);
  });

});
