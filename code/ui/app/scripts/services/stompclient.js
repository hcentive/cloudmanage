'use strict';

/**
 * @ngdoc service
 * @name cloudmanageApp.stompClient
 * @description
 * # stompClient
 * Provider in the cloudmanageApp.
 */
angular.module('cloudmanageApp')
  .provider('stompClient', function () {

    var _endpoint = null;
    this.setEndPoint = function(endpoint){
      _endpoint = endpoint;
    }

    // Method for instantiating
    this.$get = ['serviceUrl',function (serviceUrl) {
      var socket = new SockJS(serviceUrl+_endpoint);
      return Stomp.over(socket);
    }];
  });
