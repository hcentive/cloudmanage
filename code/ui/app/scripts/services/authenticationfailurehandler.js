'use strict';

/**
 * @ngdoc service
 * @name cloudmanageApp.authenticationFailureHandler
 * @description
 * # authenticationFailureHandler
 * Service in the cloudmanageApp.
 */
angular.module('cloudmanageApp')
  .service('authenticationFailureHandler', ['$log','securityContextHolder',
  	function authenticationFailureHandler($log, securityContextHolder) {
  	this.handle = handle;

  	function handle(data, credentials){
  		securityContextHolder.principal = {};
      	securityContextHolder.authenticated = false;
  		$log.error(data);
  	}
  }]);
