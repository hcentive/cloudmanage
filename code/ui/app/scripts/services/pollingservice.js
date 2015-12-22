'use strict';

/**
 * @ngdoc service
 * @name cloudmanageApp.pollingService
 * @description
 * # pollingService
 * Service in the cloudmanageApp.
 */
angular.module('cloudmanageApp')
  .service('pollingService', ['pollingInterval', '$interval','$timeout',function pollingService(pollingInterval, $interval, $timeout) {
  	var _promise = null;

  	function startPolling(pollingFn, pollingArgs){
  		_promise = $interval(function(){
  			pollingFn.apply(null, pollingArgs);
  		}, pollingInterval);
  	}
  	
  	this.register = function(){
  		var _argumentsArray = Array.prototype.slice.call(arguments),
  		_poll = _argumentsArray.shift(),
  		_arguments = _argumentsArray;
      $timeout(function(){
        startPolling(_poll, _arguments);
      }, pollingInterval);
  	};

  	this.deregister = function(){
  		if(_promise){
  			$interval.cancel(_promise);
  			_promise = null;	
  		}
  	};


    
  }]);
