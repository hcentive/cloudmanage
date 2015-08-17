'use strict';

/**
 * @ngdoc overview
 * @name uiApp
 * @description
 * # uiApp
 *
 * Main module of the application.
 */
angular
  .module('cloudmanageApp', [
    'ngAnimate',
    'ngCookies',
    'ngResource',
    'ngSanitize',
    'ngTouch',
    'ui.router'
  ])
  .config(['$stateProvider', '$urlRouterProvider', function($stateProvider, $urlRouterProvider){
  	 $urlRouterProvider.otherwise('/');
  	 $stateProvider
	    .state('login', {
	      url: '/login',
	      templateUrl: 'views/login.html'
	    })
	    .state('home',{
	    	url: '/',
	    	templateUrl: 'views/home.html'
	    });
  }])
  .value('serviceUrl', '/service');
