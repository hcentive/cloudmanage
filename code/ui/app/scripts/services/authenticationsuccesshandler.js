'use strict';

/**
 * @ngdoc service
 * @name cloudmanageApp.authenticationSuccessHandler
 * @description
 * # authenticationSuccessHandler
 * Service in the cloudmanageApp.
 */
angular.module('cloudmanageApp')
  .service('authenticationSuccessHandler',['stateCache','$state','securityContextHolder', 
    function authenticationSuccessHandler(stateCache, $state, securityContextHolder) {
  	this.handle = handle;

  	function handle(data){
      securityContextHolder.principal = data;
      securityContextHolder.authenticated = true;
  		var cachedState = stateCache.getState();
        if(cachedState){
          $state.go(cachedState.name)
          .then(function(){
            stateCache.removeState();
          });
        }
        else{
          $state.go('dashboard');  
        }
  	}
  }]);
