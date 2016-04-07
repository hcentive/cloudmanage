'use strict';

/**
 * @ngdoc service
 * @name cloudmanageApp.authenticationSuccessHandler
 * @description
 * # authenticationSuccessHandler
 * Service in the cloudmanageApp.
 */
angular.module('cloudmanageApp')
  .service('authenticationSuccessHandler',['securityContextHolder','profileService', 
    function authenticationSuccessHandler(securityContextHolder,profileService) {
      this.handle = handle;
    	function handle(data){
        securityContextHolder.principal = data;
        securityContextHolder.authenticated = true;
        profileService.getProfile().then(function(data){
          securityContextHolder.profile = data;
        });
    	}
  }]);
