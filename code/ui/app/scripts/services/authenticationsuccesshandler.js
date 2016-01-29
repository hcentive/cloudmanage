'use strict';

/**
 * @ngdoc service
 * @name cloudmanageApp.authenticationSuccessHandler
 * @description
 * # authenticationSuccessHandler
 * Service in the cloudmanageApp.
 */
angular.module('cloudmanageApp')
  .service('authenticationSuccessHandler',['stateCache','$state','securityContextHolder','profileService', 
    function authenticationSuccessHandler(stateCache, $state, securityContextHolder,profileService) {
      this.handle = handle;
    	function handle(data){
        securityContextHolder.principal = data;
        securityContextHolder.authenticated = true;
        profileService.getProfile().then(function(data){
          securityContextHolder.profile = data;
        });
    		var cachedState = stateCache.getState();
          if(cachedState){
            $state.go(cachedState.name)
            .then(function(){
              stateCache.removeState();
            });
          }
          else{
            $state.go('instances');  
          }
    	}
  }]);
