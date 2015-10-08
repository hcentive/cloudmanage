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
          return $http.post('/login', credentials,{
             headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            transformRequest: function(data, getHeaders){
                return (! (typeof data === 'string')) ? $.param(data) : data;
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
  }]);
