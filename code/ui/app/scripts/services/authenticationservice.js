'use strict';

/**
 * @ngdoc service
 * @name cloudmanageApp.authenticationService
 * @description
 * # authenticationService
 * Service in the cloudmanageApp.
 */
angular.module('cloudmanageApp')
  .service('authenticationService', ['securityContextHolder','$q','$http','authenticationSuccessHandler','authenticationFailureHandler',
    function authenticationService(securityContextHolder, $q, $http,authenticationSuccessHandler,authenticationFailureHandler) {

      this.authenticate = authenticate;

      function authenticate(credentials){
        var headers = credentials ? {authorization : "Basic "
            + btoa(credentials.username + ":" + credentials.password)
          } : {};
          return $http.get('/user', {
            headers : headers
          }).then(
            function(principal){
              authenticationSuccessHandler.handle(principal);
            },
            function(){
              authenticationFailureHandler.handle();
            }
          );
      };
    
  }]);
