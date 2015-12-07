'use strict';

describe('Service: stompClient', function () {

  // load the service's module
  beforeEach(module('cloudmanageApp'));

  // instantiate service
  var stompClient;
  beforeEach(inject(function (_stompClient_) {
    stompClient = _stompClient_;
  }));

  it('should do something', function () {
    expect(!!stompClient).toBe(true);
  });

});
