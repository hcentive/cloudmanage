'use strict';

/**
 * @ngdoc service
 * @name cloudmanageApp.authenticationFailureHandler
 * @description
 * # authenticationFailureHandler
 * Service in the cloudmanageApp.
 */
angular.module('cloudmanageApp')
  .service('authenticationFailureHandler', ['$log','securityContextUtils',
  	function authenticationFailureHandler($log, securityContextUtils) {
  	this.handle = handle;

  	function handle(data, credentials){
  		securityContextUtils.clearSecurityContext();
  		$log.error(data);
  	}
  }]);
