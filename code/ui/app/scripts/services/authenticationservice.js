'use strict';

/**
 * @ngdoc service
 * @name cloudmanageApp.authenticationService
 * @description
 * # authenticationService
 * Service in the cloudmanageApp.
 */
angular.module('cloudmanageApp')
  .service('authenticationService', ['$q','$http','authenticationSuccessHandler','authenticationFailureHandler','stateCache','$state',
    function authenticationService($q, $http,authenticationSuccessHandler,authenticationFailureHandler, stateCache, $state) {
      this.authenticate = authenticate;

      function authenticate(credentials){
          if(credentials){
             return $http.post('/login', credentials,{
                 headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
              }).then(
                function(principal){
                  authenticationSuccessHandler.handle(principal);
                  redirect();
                  return principal;
                },
                function(reason){
                  authenticationFailureHandler.handle(reason);
                  if()
                  return $q.reject(reason);
                }
              );
          }
          else {
            return $http.get('/user').then(function(principal){
                    authenticationSuccessHandler.handle(principal);
                    return principal;
                });
          }
         
        }

        function redirect(){
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
