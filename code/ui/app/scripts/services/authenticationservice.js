'use strict';

/**
 * @ngdoc service
 * @name cloudmanageApp.authenticationService
 * @description
 * # authenticationService
 * Service in the cloudmanageApp.
 */
angular.module('cloudmanageApp')
  .service('authenticationService', ['$q','$http','authenticationSuccessHandler','authenticationFailureHandler',
    function authenticationService($q, $http,authenticationSuccessHandler,authenticationFailureHandler) {
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
                  return principal;
                },
                function(reason){
                  authenticationFailureHandler.handle(reason);
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
  }]);
