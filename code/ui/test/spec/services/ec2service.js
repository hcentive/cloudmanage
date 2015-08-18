'use strict';

describe('Service: ec2Service', function () {

  // load the service's module
  beforeEach(module('cloudmanageApp'));

  // instantiate service
  var ec2Service;
  beforeEach(inject(function (_ec2Service_) {
    ec2Service = _ec2Service_;
  }));

  it('should do something', function () {
    expect(!!ec2Service).toBe(true);
  });

});
