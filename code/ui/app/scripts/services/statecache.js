'use strict';

/**
 * @ngdoc service
 * @name cloudmanageApp.stateCache
 * @description
 * # stateCache
 * Service in the cloudmanageApp.
 */
angular.module('cloudmanageApp')
  .service('stateCache', function stateCache() {
  	var _state = null;
  	this.addState = addState;
  	this.removeState = removeState;
  	this.getState = getState;

  	function addState(state){
  		_state = state;
  	}

  	function removeState(){
  		_state = null;
  	}

  	function getState(){
  		return _state;
  	}

    // AngularJS will instantiate a singleton by calling "new" on this function
  });
