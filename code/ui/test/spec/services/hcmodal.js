'use strict';

describe('Service: hcModal', function () {

  // load the service's module
  beforeEach(module('cloudmanageApp'));

  // instantiate service
  var hcModal;
  beforeEach(inject(function (_hcModal_) {
    hcModal = _hcModal_;
  }));

  it('should do something', function () {
    expect(!!hcModal).toBe(true);
  });

});
